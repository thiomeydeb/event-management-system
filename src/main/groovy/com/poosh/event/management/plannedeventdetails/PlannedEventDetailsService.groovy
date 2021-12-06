package com.poosh.event.management.plannedeventdetails

import com.poosh.event.management.apiresponse.BaseApiResponse
import com.poosh.event.management.audit.AuditService
import com.poosh.event.management.eventype.EventType
import com.poosh.event.management.eventype.EventTypeRepository
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
class PlannedEventDetailsService {

    private final PlannedEventDetailsRepository plannedEventDetailsRepository
    private final DataSource dataSource
    private final AuditService auditService
    private final CommonDbFunctions commonDbFunctions

    @Autowired //inject the object dependency implicitly
    PlannedEventDetailsService(PlannedEventDetailsRepository plannedEventDetailsRepository,
                               DataSource dataSource,
                               AuditService auditService,
                               CommonDbFunctions commonDbFunctions) {
        this.plannedEventDetailsRepository = plannedEventDetailsRepository
        this.dataSource = dataSource
        this.auditService = auditService
        this.commonDbFunctions = commonDbFunctions
    }

    BaseApiResponse getPlannedProviderDetails(long eventId){
        Sql sql = new Sql(dataSource);
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value(), "success", [])
        Map sqlParams = ["eventId": eventId];
        def providerQuery = """ SELECT
                                (SELECT planned_event_details.event_id
                                FROM
                                planned_event_details
                                INNER JOIN providers ON "public".planned_event_details.provider_id = "public".providers."id"
                                INNER JOIN provider_category ON "public".providers.category_id = "public".provider_category."id"
                                WHERE
                                planned_event_details.event_id = ?.eventId AND provider_category.id = pc."id") AS event_id,
                                pc."name" AS category,
                                (SELECT providers."id"
                                FROM
                                planned_event_details
                                INNER JOIN providers ON "public".planned_event_details.provider_id = "public".providers."id"
                                INNER JOIN provider_category ON "public".providers.category_id = "public".provider_category."id"
                                WHERE
                                planned_event_details.event_id = ?.eventId AND provider_category.id = pc."id") AS provider_id,
                                (SELECT providers."name"
                                FROM
                                planned_event_details
                                INNER JOIN providers ON "public".planned_event_details.provider_id = "public".providers."id"
                                INNER JOIN provider_category ON "public".providers.category_id = "public".provider_category."id"
                                WHERE
                                planned_event_details.event_id = ?.eventId AND provider_category.id = pc."id") AS provider_name,
                                (SELECT providers."cost"
                                FROM
                                planned_event_details
                                INNER JOIN providers ON "public".planned_event_details.provider_id = "public".providers."id"
                                INNER JOIN provider_category ON "public".providers.category_id = "public".provider_category."id"
                                WHERE
                                planned_event_details.event_id = ?.eventId AND provider_category.id = pc."id") AS "cost",
                                (CASE WHEN (SELECT planned_event_details.status
                                FROM
                                planned_event_details
                                INNER JOIN providers ON "public".planned_event_details.provider_id = "public".providers."id"
                                INNER JOIN provider_category ON "public".providers.category_id = "public".provider_category."id"
                                WHERE
                                planned_event_details.event_id = ?.eventId AND provider_category.id = pc."id" AND planned_event_details.status = TRUE) ISNULL
                                THEN FALSE ELSE TRUE END) AS status,
                                (CASE WHEN id > 0 THEN 'p' ELSE 'v' END) AS type
                                FROM
                                provider_category pc
                                """;
        def venueQuery = """SELECT
                            (SELECT event_id FROM
                            planned_event_details
                            INNER JOIN venues ON planned_event_details.venue_id = venues."id"
                            WHERE
                            planned_event_details.event_id = ?.eventId) AS event_id,
                            (CASE WHEN (SELECT id FROM venues where venues."id" = 1) > 0 THEN 'Venue' ELSE 'Ven' END) AS "category",
                            (SELECT venue_id FROM
                            planned_event_details
                            INNER JOIN venues ON planned_event_details.venue_id = venues."id"
                            WHERE
                            planned_event_details.event_id = ?.eventId) AS provider_id,
                            (SELECT venues."name" FROM
                            planned_event_details
                            INNER JOIN "public".venues ON planned_event_details.venue_id = venues."id"
                            WHERE
                            planned_event_details.event_id = ?.eventId) AS provider_name,
                            (SELECT venues.amount FROM
                            planned_event_details
                            INNER JOIN venues ON planned_event_details.venue_id = venues."id"
                            WHERE
                            planned_event_details.event_id = ?.eventId) AS "cost",
                            (CASE WHEN ((SELECT planned_event_details.status FROM
                            planned_event_details
                            INNER JOIN venues ON planned_event_details.venue_id = venues."id"
                            WHERE
                            planned_event_details.event_id = ?.eventId AND planned_event_details.status = TRUE)) ISNULL THEN FALSE ELSE TRUE END) AS "status",
                            (CASE WHEN (SELECT id FROM venues where id = 1) > 0 THEN 'v' ELSE 'p' END) AS "type"
                            FROM provider_category LIMIT 1 
                            """;

        def providerData = sql.rows(providerQuery, sqlParams)
        def venueDetails = sql.firstRow(venueQuery, sqlParams)
        providerData.add(venueDetails)
        res.data = providerData
        return res
    }

    BaseApiResponse getBookedEventsAssignedToPlanner(Principal principal, Map parameterMap){
        String userName = principal.getName()
        long userId = commonDbFunctions.getUserIdFromEmail(userName)
        def params = MyUtil.flattenListParam(parameterMap)
        Map sqlParams = [start: 0, limit: 5, "plannerId": userId]
        def countParamStatus = true

        def start = params.start?.toInteger()
        def limit = params.limit?.toInteger()

        sqlParams.start =  start;
        sqlParams.limit = limit;

        def sqlQuery2 = """SELECT
                        b_events."id",
                        b_events.title,
                        b_events.client_id,
                        b_events.event_type_id,
                        event_type."name" event_type_name,
                        b_events.management_amount,
                        b_events.status,
                        b_events.other_information,
                        b_events.attendees,
                        b_events.total_amount,
                        b_events.greening_status,
                        (SELECT(SELECT providers."id" FROM planned_event_details 
                        INNER JOIN providers ON planned_event_details.provider_id = providers."id"
                        INNER JOIN provider_category ON providers.category_id = provider_category."id"
                        WHERE providers.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 0) AS security_id,
                        (SELECT(SELECT providers."name" FROM planned_event_details 
                        INNER JOIN providers ON planned_event_details.provider_id = providers."id"
                        INNER JOIN provider_category ON providers.category_id = provider_category."id"
                        WHERE providers.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 0) AS security_name,
                        (SELECT(SELECT providers."cost" FROM planned_event_details 
                        INNER JOIN providers ON planned_event_details.provider_id = providers."id"
                        INNER JOIN provider_category ON providers.category_id = provider_category."id"
                        WHERE providers.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 0) AS security_amount,
                        (SELECT(SELECT planned_event_details.status FROM planned_event_details 
                        INNER JOIN providers ON planned_event_details.provider_id = providers."id"
                        INNER JOIN provider_category ON providers.category_id = provider_category."id"
                        WHERE providers.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 0) AS security_status,
                        
                        (SELECT(SELECT providers."id" FROM planned_event_details 
                        INNER JOIN providers ON planned_event_details.provider_id = providers."id"
                        INNER JOIN provider_category ON providers.category_id = provider_category."id"
                        WHERE providers.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 1) AS catering_id,
                        (SELECT(SELECT providers."name" FROM planned_event_details 
                        INNER JOIN providers ON planned_event_details.provider_id = providers."id"
                        INNER JOIN provider_category ON providers.category_id = provider_category."id"
                        WHERE providers.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 1) AS catering_name,
                        (SELECT(SELECT providers."cost" FROM planned_event_details 
                        INNER JOIN providers ON planned_event_details.provider_id = providers."id"
                        INNER JOIN provider_category ON providers.category_id = provider_category."id"
                        WHERE providers.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 1) AS catering_amount,
                        (SELECT(SELECT planned_event_details.status FROM planned_event_details 
                        INNER JOIN providers ON planned_event_details.provider_id = providers."id"
                        INNER JOIN provider_category ON providers.category_id = provider_category."id"
                        WHERE providers.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 1) AS catering_status,
                        
                        (SELECT(SELECT providers."id" FROM planned_event_details 
                        INNER JOIN providers ON planned_event_details.provider_id = providers."id"
                        INNER JOIN provider_category ON providers.category_id = provider_category."id"
                        WHERE providers.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 2) AS entertainment_id,
                        (SELECT(SELECT providers."name" FROM planned_event_details 
                        INNER JOIN providers ON planned_event_details.provider_id = providers."id"
                        INNER JOIN provider_category ON providers.category_id = provider_category."id"
                        WHERE providers.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 2) AS entertainment_name,
                        (SELECT(SELECT providers."cost" FROM planned_event_details 
                        INNER JOIN providers ON planned_event_details.provider_id = providers."id"
                        INNER JOIN provider_category ON providers.category_id = provider_category."id"
                        WHERE providers.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 2) AS entertainment_amount,
                        (SELECT(SELECT planned_event_details.status FROM planned_event_details 
                        INNER JOIN providers ON planned_event_details.provider_id = providers."id"
                        INNER JOIN provider_category ON providers.category_id = provider_category."id"
                        WHERE providers.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 2) AS entertainment_status,
                        
                        (SELECT(SELECT providers."id" FROM planned_event_details 
                        INNER JOIN providers ON planned_event_details.provider_id = providers."id"
                        INNER JOIN provider_category ON providers.category_id = provider_category."id"
                        WHERE providers.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 3) AS  design_id,
                        (SELECT(SELECT providers."name" FROM planned_event_details 
                        INNER JOIN providers ON planned_event_details.provider_id = providers."id"
                        INNER JOIN provider_category ON providers.category_id = provider_category."id"
                        WHERE providers.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 3) AS design_name,
                        (SELECT(SELECT providers."cost" FROM planned_event_details 
                        INNER JOIN providers ON planned_event_details.provider_id = providers."id"
                        INNER JOIN provider_category ON providers.category_id = provider_category."id"
                        WHERE providers.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 3) AS design_amount,
                        (SELECT(SELECT planned_event_details.status FROM planned_event_details 
                        INNER JOIN providers ON planned_event_details.provider_id = providers."id"
                        INNER JOIN provider_category ON providers.category_id = provider_category."id"
                        WHERE providers.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 3) AS  design_status,
                        
                        (SELECT(SELECT providers."id" FROM planned_event_details 
                        INNER JOIN providers ON planned_event_details.provider_id = providers."id"
                        INNER JOIN provider_category ON providers.category_id = provider_category."id"
                        WHERE providers.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 4) AS  mc_id,
                        (SELECT(SELECT providers."name" FROM planned_event_details 
                        INNER JOIN providers ON planned_event_details.provider_id = providers."id"
                        INNER JOIN provider_category ON providers.category_id = provider_category."id"
                        WHERE providers.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 4) AS mc_name,
                        (SELECT(SELECT providers."cost" FROM planned_event_details 
                        INNER JOIN providers ON planned_event_details.provider_id = providers."id"
                        INNER JOIN provider_category ON providers.category_id = provider_category."id"
                        WHERE providers.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 4) AS mc_amount,
                        (SELECT(SELECT planned_event_details.status FROM planned_event_details 
                        INNER JOIN providers ON planned_event_details.provider_id = providers."id"
                        INNER JOIN provider_category ON providers.category_id = provider_category."id"
                        WHERE providers.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 4) AS  mc_status,
                        
                        (SELECT
                        venues."id"
                        FROM
                        planned_event_details
                        INNER JOIN venues ON planned_event_details.venue_id = venues."id"
                        WHERE
                        planned_event_details.event_id = b_events."id") as venue_id,
                        (SELECT
                        venues."name"
                        FROM
                        planned_event_details
                        INNER JOIN venues ON planned_event_details.venue_id = venues."id"
                        WHERE
                        planned_event_details.event_id = b_events."id") as venue_name,
                        (SELECT
                        venues."location"
                        FROM
                        planned_event_details
                        INNER JOIN venues ON planned_event_details.venue_id = venues."id"
                        WHERE
                        planned_event_details.event_id = b_events."id") as venue_location,
                        (SELECT
                        planned_event_details.status
                        FROM
                        planned_event_details
                        INNER JOIN venues ON planned_event_details.venue_id = venues."id"
                        WHERE
                        planned_event_details.event_id = b_events."id") as venue_status,
                        (SELECT user_id FROM event_planner_allocations WHERE event_id = b_events."id" AND status = TRUE) as planner_id
                        FROM
                        booked_events b_events
                        INNER JOIN event_type ON b_events.event_type_id = event_type."id"
                        INNER JOIN event_planner_allocations ON b_events."id" = event_planner_allocations.event_id
                        WHERE
                        event_planner_allocations.user_id = ?.plannerId AND event_planner_allocations.status = TRUE
                        LIMIT ?.limit OFFSET ?.start""";
        def countQuery2 = """SELECT
                            COUNT(*)
                            FROM
                            booked_events b_events
                            INNER JOIN event_type ON b_events.event_type_id = event_type."id"
                            INNER JOIN event_planner_allocations ON b_events."id" = event_planner_allocations.event_id
                            WHERE
                            event_planner_allocations.user_id = ?.plannerId AND event_planner_allocations.status = TRUE
                            LIMIT ?.limit OFFSET ?.start""";

        return commonDbFunctions.returnJsonFromQueryWithCount(sqlQuery2, countQuery2, sqlParams, countParamStatus);
    }

    int savePlannedEventDetails(String plannedEventDetails, long userId, long eventId){
        Sql sql = new Sql(dataSource);
        List selectedProviderDetails = new JsonSlurper().parseText(plannedEventDetails);
        int eventStatus = 0;

        def providersParams = ["event_id": eventId, "planner_id": userId];

        sql.withTransaction {
            sql.executeInsert("DELETE FROM planned_event_details WHERE event_id = ?", eventId);
            Set<Boolean>providerStatus = new HashSet<>();
            selectedProviderDetails.each {
                def providerId = it.get("provider_id");
                def type = it.get("type");
                def strStatus = ""+it.get("status");
                Boolean status = Boolean.parseBoolean(strStatus);
                providersParams.event_id = it.get("event_id");
                providersParams.put("provider_id", providerId);
                providersParams.status = status;
                providerStatus.add(status);
                if(type == "p"){
                    sql.executeInsert("INSERT INTO planned_event_details (event_id, provider_id, planner_id, status) VALUES (?.event_id, ?.provider_id, ?.planner_id, ?.status)", providersParams);
                }else if (type == "v"){
                    sql.executeInsert("INSERT INTO planned_event_details (event_id, venue_id, planner_id, status) VALUES (?.event_id, ?.provider_id, ?.planner_id, ?.status)", providersParams);
                }
            }
            def overallPlanningStatus = providerStatus.contains(false);
            if(overallPlanningStatus){
                sql.executeUpdate("UPDATE booked_events SET status = 2 WHERE id = ?", eventId);
                eventStatus = 2;
            }else{
                sql.executeUpdate("UPDATE booked_events SET status = 3, completion_timestamp = now() WHERE id = ?", eventId);
                eventStatus = 3;
            }
        }
        sql.close();
        return eventStatus;
    }

    BaseApiResponse updateProviderDetails(String body,
                                          HttpServletRequest request,
                                          WebRequest webRequest,
                                          Principal principal) {
        BaseApiResponse res = new BaseApiResponse([],HttpStatus.OK.value(),"Provider Details Saved successfully", [])
        String userName = principal.getName()
        long userId = commonDbFunctions.getUserIdFromEmail(userName);
        long plannedEventId = Long.parseLong(webRequest.getParameter("eventId"));
        long eventStatus = savePlannedEventDetails(body, userId, plannedEventId);

        auditService.logAuditEvent("Update Provider Status-(Planning)",request.getRemoteAddr(), userName, "Event Id:"+plannedEventId);
        HashMap<String, String> eventDetails= new HashMap<String, String>();
        eventDetails.put("eventStatus", ""+eventStatus);
        res.data = eventDetails
        return res
    }
}
