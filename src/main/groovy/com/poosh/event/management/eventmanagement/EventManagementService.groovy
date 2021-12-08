package com.poosh.event.management.eventmanagement

import com.poosh.event.management.apiresponse.BaseApiResponse
import com.poosh.event.management.audit.AuditService
import com.poosh.event.management.exceptions.BadRequestException
import com.poosh.event.management.exceptions.InternalServerErrorException
import com.poosh.event.management.utils.CommonDbFunctions
import com.poosh.event.management.utils.MyUtil
import groovy.json.JsonSlurper
import groovy.sql.Sql
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.context.request.WebRequest

import javax.servlet.http.HttpServletRequest
import javax.sql.DataSource
import java.security.Principal

@Service
class EventManagementService {

    private final DataSource dataSource
    private final AuditService auditService
    private final CommonDbFunctions commonDbFunctions

    @Autowired
    EventManagementService(DataSource dataSource,
                           AuditService auditService,
                           CommonDbFunctions commonDbFunctions) {
        this.dataSource = dataSource
        this.auditService = auditService
        this.commonDbFunctions = commonDbFunctions
    }

    BaseApiResponse linkPlanner(LinkPlannerDto body){
        BaseApiResponse res = new BaseApiResponse(HttpStatus.OK.value(), "success")
        def userId = body.userId
        def eventId = body.eventId
        def sqlParams = [userId: userId, eventId: eventId];
        Sql sql = new Sql(dataSource)
        def duplicateRecords = sql.firstRow("SELECT* FROM event_planner_allocation WHERE event_id = ?.eventId AND user_id = ?.userId AND status = TRUE", sqlParams);
        if(duplicateRecords != null){
            throw new BadRequestException("Planner is already linked to the event")
        }else{
            def existingRecord = sql.firstRow("SELECT * FROM event_planner_allocation WHERE event_id = ?.eventId AND status = TRUE", sqlParams);
            if(existingRecord == null){
                def insertQuery = sql.executeInsert("INSERT INTO event_planner_allocation (event_id, user_id, status) VALUES (?.eventId, ?.userId, 'TRUE')", sqlParams);
                if(insertQuery){
                    sql.executeUpdate("UPDATE booked_event SET status = 1 WHERE id = ?.eventId", sqlParams)
                    res.message = 'Planner linked to the event successfully'
                    return res
                }
            }else{
                def updateQuery = sql.executeUpdate("UPDATE event_planner_allocation SET status = FALSE WHERE event_id = ?.eventId", sqlParams);
                if(updateQuery > 0){
                    def insertQuery = sql.executeInsert("INSERT INTO event_planner_allocation (event_id, user_id, status) VALUES (?.eventId, ?.userId, 'TRUE')", sqlParams);
                    if(insertQuery){
                        sql.executeUpdate("UPDATE booked_event SET status = 1 WHERE id = ?.eventId", sqlParams)
                        res.message = 'Planner linked to the event successfully'
                        return res
                    }
                }else{
                    throw new InternalServerErrorException('Failed to link planner to event')
                }
            }

        }
        sql.close();
        return res
    }

    BaseApiResponse cancelEvent(WebRequest webRequest, HttpServletRequest request, Principal principal){
        BaseApiResponse res = new BaseApiResponse(HttpStatus.OK.value(), "Event cancelled successfully")
        Sql sql = new Sql(dataSource);
        long eventId = Long.parseLong(webRequest.getParameter("eventId"))
        String currentUser  = principal.getName();
        def cancelRes
        def isCanceledEventExist = sql.firstRow("SELECT * FROM  booked_event WHERE id = ? AND status = 4", eventId)
        if(!isCanceledEventExist){
            def updateRes = sql.executeInsert("UPDATE booked_event SET status = 4 WHERE id = ?", eventId)
            cancelRes = updateRes?.get(0)?.get(0)
        }
        sql.close();
        if(cancelRes == null){
            throw new InternalServerErrorException("Failed to cancel event cancelled successfully")
        }else if(cancelRes == 0){
            throw new BadRequestException("Event already cancelled")
        }else if(cancelRes > 0){
            auditService.logAuditEvent("Cancel Event",request.getRemoteAddr(),currentUser,cancelRes+"")
            res.data = "Event cancelled successfully"
            return res
        }
    }

    BaseApiResponse getSavedGreeningDetails(long eventId){
        Sql sql = new Sql(dataSource)
        BaseApiResponse res = new BaseApiResponse(HttpStatus.OK.value(), "success")
        Map sqlParams = ["eventId": eventId]
        def greeningQuery = """ SELECT name,id AS greening_type_id,

                                (SELECT
                                event_greening_progress."id"
                                FROM
                                event_greening_type
                                INNER JOIN event_greening_progress ON event_greening_progress.greening_type_id = event_greening_type."id"
                                WHERE event_greening_progress.event_id = ?.eventId AND event_greening_progress.greening_type_id = egt.id),
                                
                                (CASE WHEN
                                (SELECT
                                event_greening_progress.status
                                FROM
                                event_greening_type
                                INNER JOIN event_greening_progress ON event_greening_progress.greening_type_id = event_greening_type."id"
                                WHERE event_greening_progress.event_id = ?.eventId AND event_greening_progress.greening_type_id = egt.id  
                                AND event_greening_progress.status = TRUE) ISNULL
                                THEN FALSE ELSE TRUE END) AS status,
                                
                                (SELECT "id" FROM booked_events be WHERE be."id" = ?.eventId) AS event_id
                                FROM event_greening_type egt
                                """;

        def greeningData = sql.rows(greeningQuery, sqlParams);

        res.data = greeningData
        return res;
    }

    public BaseApiResponse saveGreeningDetails(String greeningDetails,
                                               HttpServletRequest request,
                                               WebRequest webRequest,
                                               Principal principal){
        BaseApiResponse res = new BaseApiResponse(HttpStatus.OK.value(), "greening update successful")
        String userName = principal.getName()
        long userId = commonDbFunctions.getUserIdFromEmail(userName)
        long eventId = Long.parseLong(webRequest.getParameter("eventId"))
        Sql sql = new Sql(dataSource)
        List selectedGreeningDetails = new JsonSlurper().parseText(greeningDetails)
        int eventGreeningStatus = 0;

        def greeningParams = ["event_id": eventId, "manager_id": userId];

        sql.withTransaction {
            sql.executeInsert("DELETE FROM event_greening_progress WHERE event_id = ?", eventId);
            Set<Boolean>greeningStatus = new HashSet<>();
            selectedGreeningDetails.each {
                def greeningTypeId = it.get("greening_type_id");
                def strStatus = ""+it.get("status");
                Boolean status = Boolean.parseBoolean(strStatus);
                greeningParams.event_id = it.get("event_id");
                greeningParams.put("greening_type_id", greeningTypeId);
                greeningParams.status = status;
                greeningStatus.add(status);
                sql.executeInsert("INSERT INTO event_greening_progress (greening_type_id, event_id, manager_id, status) VALUES (?.greening_type_id, ?.event_id, ?.manager_id, ?.status)", greeningParams);

            }
            def overallPlanningStatus = greeningStatus.contains(false);
            if(overallPlanningStatus){
                sql.executeUpdate("UPDATE booked_event SET greening_status = 0 WHERE id = ?", eventId);
                eventGreeningStatus = 0;
            }else{
                sql.executeUpdate("UPDATE booked_event SET greening_status = 1, greening_completion_timestamp = now() WHERE id = ?", eventId);
                eventGreeningStatus = 1;
            }
        }
        sql.close();
        auditService.logAuditEvent("Update Greening Status-(Management)",request.getRemoteAddr(), userName, "Event Id:"+eventId);
        HashMap<String, String> greeningStatusDetails = new HashMap<String, String>();
        greeningStatusDetails.put("eventGreeningStatus", ""+eventGreeningStatus)
        res.data = greeningStatusDetails
        return res
    }
}
