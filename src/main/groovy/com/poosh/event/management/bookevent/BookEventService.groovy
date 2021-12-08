package com.poosh.event.management.bookevent

import com.poosh.event.management.apiresponse.BaseApiResponse
import com.poosh.event.management.audit.AuditService
import com.poosh.event.management.bookevent.dto.EventCreateDto
import com.poosh.event.management.exceptions.BadRequestException
import com.poosh.event.management.exceptions.InternalServerErrorException
import com.poosh.event.management.globaldto.IntStatusDto
import com.poosh.event.management.utils.CommonDbFunctions
import com.poosh.event.management.utils.MyUtil
import groovy.json.JsonSlurper
import groovy.sql.Sql
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

import javax.servlet.http.HttpServletRequest
import javax.sql.DataSource
import java.security.Principal

@Service
class BookEventService {
    private final BookEventRepository bookedEventRepository
    private final DataSource dataSource
    private final AuditService auditService
    private final CommonDbFunctions commonDbFunctions

    @Autowired //inject the object dependency implicitly
    BookEventService(BookEventRepository bookedEventRepository,
                     DataSource dataSource,
                     AuditService auditService,
                     CommonDbFunctions commonDbFunctions) {
        this.bookedEventRepository = bookedEventRepository
        this.dataSource = dataSource
        this.auditService = auditService
        this.commonDbFunctions = commonDbFunctions
    }

    Long insertBookedEventDetails(EventCreateDto bookedEventDetails, long userId){
        Sql sql = new Sql(dataSource);
        bookedEventDetails.setClientId(userId)
        def insertRes = sql.executeInsert("INSERT INTO booked_event (title, client_id, event_type_id, management_amount, other_information, " +
                "attendees, total_amount) VALUES (?.name, ?.clientId, ?.eventTypeId, ?.managementAmount, ?.otherInformation, ?.attendees, " +
                "?.totalAmount)", bookedEventDetails);
        //get event id
        def bookedEventId = insertRes?.get(0)?.get(0);

        //create list of providers to be used to insert in planned_event_details table
        List <HashMap> providers = new ArrayList<HashMap>();
        def venueId = bookedEventDetails.venueId
        Map venue = ["providerId": venueId, "type": "v"];

        def entertainmentId = bookedEventDetails.entertainmentId
        Map entertainment = ["providerId": entertainmentId, "type": "p"];

        def cateringId = bookedEventDetails.cateringId
        Map catering = ["providerId": cateringId, "type": "p"];

        def securityId = bookedEventDetails.securityId
        Map security = ["providerId": securityId, "type": "p"];

        def designId = bookedEventDetails.designId
        Map design = ["providerId": designId, "type": "p"];

        def mcId = bookedEventDetails.mcId
        Map mc = ["providerId": mcId, "type": "p"];

        //add providers to list
        providers.add(venue);
        providers.add(entertainment);
        providers.add(catering);
        providers.add(security);
        providers.add(design);
        providers.add(mc);

        def providersParams = ["eventId": bookedEventId, "plannerId": userId];
        sql.withTransaction {
            providers.each {
                def providerId = it.get("providerId");
                def type = it.get("type");
                providersParams.put("providerId", providerId);
                if(type == "p"){
                    sql.executeInsert("INSERT INTO planned_event_details (event_id, provider_id, planner_id) VALUES (?.eventId, ?.providerId, ?.plannerId)", providersParams);
                }else if (type == "v"){
                    sql.executeInsert("INSERT INTO planned_event_details (event_id, venue_id, planner_id) VALUES (?.eventId, ?.providerId, ?.plannerId)", providersParams);
                }
            }
        }

        sql.close();
        return bookedEventId;
    }

    Long updateBookedEvent(String bookedEventDetails, long userId){
        Sql sql = new Sql(dataSource);
        Map bookedEventMap = new JsonSlurper().parseText(bookedEventDetails);
        bookedEventMap.put("clientId",userId);
        Double totalAmount = Double.parseDouble(bookedEventMap.get("totalAmount"));
        Double managementAmount = Double.parseDouble(bookedEventMap.get("managementAmount"));
        bookedEventMap.totalAmount = totalAmount;
        bookedEventMap.managementAmount = managementAmount;
        def bookedEventId = bookedEventMap.get("id");
        int updateRes = sql.executeUpdate("UPDATE booked_event SET title = ?.name,event_type_id = ?.eventTypeId, " +
                "management_amount = ?.managementAmount, other_information = ?.otherInformation, attendees = ?.attendees" +
                "total_amount = ?.totalAmount WHERE id = ?.id",bookedEventMap);

        if(updateRes > 0){
            sql.executeInsert("DELETE FROM planned_event_details WHERE event_id = ?.eventId", bookedEventMap);
            //create list of providers to be used to insert in planned_event_details table
            List <HashMap> providers = new ArrayList<HashMap>();
            def venueId = bookedEventMap.get("venueId");
            Map venue = ["providerId": venueId, "type": "v"];

            def entertainmentId = bookedEventMap.get("entertainmentId");
            Map entertainment = ["providerId": entertainmentId, "type": "p"];

            def cateringId = bookedEventMap.get("cateringId");
            Map catering = ["providerId": cateringId, "type": "p"];

            def securityId = bookedEventMap.get("securityId");
            Map security = ["providerId": securityId, "type": "p"];

            def designId = bookedEventMap.get("designId");
            Map design = ["providerId": designId, "type": "p"];

            def mcId = bookedEventMap.get("mcId");
            Map mc = ["providerId": mcId, "type": "p"];

            //add providers to list
            providers.add(venue);
            providers.add(entertainment);
            providers.add(catering);
            providers.add(security);
            providers.add(design);
            providers.add(mc);

            def providersParams = ["eventId": bookedEventId, "plannerId": userId];
            sql.withTransaction {
                providers.each {
                    def providerId = it.get("providerId");
                    def type = it.get("type");
                    providersParams.put("providerId", providerId);
                    if(type == "p"){
                        sql.executeInsert("INSERT INTO planned_event_details (event_id, provider_id, planner_id) VALUES (?.eventId, ?.providerId, ?.plannerId)", providersParams);
                    }else if (type == "v"){
                        sql.executeInsert("INSERT INTO planned_event_details (event_id, venue_id, planner_id) VALUES (?.eventId, ?.providerId, ?.plannerId)", providersParams);
                    }
                }
            }
        }

        sql.close();
        return bookedEventId;
    }

    BaseApiResponse getBookedEventsByClient(Long clientId, Map parameterMap){
        def params = MyUtil.flattenListParam(parameterMap);
        Map sqlParams = [start: 0, limit: 5, "clientId": clientId];
        def countParamStatus = true;

        def start = params.start?.toInteger();
        def limit = params.limit?.toInteger();

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
                        (SELECT(SELECT provider."id" FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 0) AS security_id,
                        (SELECT(SELECT provider.title FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 0) AS security_name,
                        (SELECT(SELECT provider."cost" FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 0) AS security_amount,
                        (SELECT(SELECT planned_event_details.status FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 0) AS security_status,
                        
                        (SELECT(SELECT provider."id" FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 1) AS catering_id,
                        (SELECT(SELECT provider.title FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 1) AS catering_name,
                        (SELECT(SELECT provider."cost" FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 1) AS catering_amount,
                        (SELECT(SELECT planned_event_details.status FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 1) AS catering_status,
                        
                        (SELECT(SELECT provider."id" FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 2) AS entertainment_id,
                        (SELECT(SELECT provider.title FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 2) AS entertainment_name,
                        (SELECT(SELECT provider."cost" FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 2) AS entertainment_amount,
                        (SELECT(SELECT planned_event_details.status FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 2) AS entertainment_status,
                        
                        (SELECT(SELECT provider."id" FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 3) AS  design_id,
                        (SELECT(SELECT provider.title FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 3) AS design_name,
                        (SELECT(SELECT provider."cost" FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 3) AS design_amount,
                        (SELECT(SELECT planned_event_details.status FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 3) AS  design_status,
                        
                        (SELECT(SELECT provider."id" FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 4) AS  mc_id,
                        (SELECT(SELECT provider.title FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 4) AS mc_name,
                        (SELECT(SELECT provider."cost" FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 4) AS mc_amount,
                        (SELECT(SELECT planned_event_details.status FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 4) AS  mc_status,
                        
                        (SELECT
                        venue."id"
                        FROM
                        planned_event_details
                        INNER JOIN venue ON planned_event_details.venue_id = venue."id"
                        WHERE
                        planned_event_details.event_id = b_events."id") as venue_id,
                        (SELECT
                        venue."name"
                        FROM
                        planned_event_details
                        INNER JOIN venue ON planned_event_details.venue_id = venue."id"
                        WHERE
                        planned_event_details.event_id = b_events."id") as venue_name,
                        (SELECT
                        venue."location"
                        FROM
                        planned_event_details
                        INNER JOIN venue ON planned_event_details.venue_id = venue."id"
                        WHERE
                        planned_event_details.event_id = b_events."id") as venue_location,
                        (SELECT
                        planned_event_details.status
                        FROM
                        planned_event_details
                        INNER JOIN venue ON planned_event_details.venue_id = venue."id"
                        WHERE
                        planned_event_details.event_id = b_events."id") as venue_status,
                        (SELECT user_id FROM event_planner_allocation WHERE event_id = b_events."id" AND status = TRUE) as planner_id
                        FROM
                        booked_event b_events
                        INNER JOIN event_type ON b_events.event_type_id = event_type."id"
                        WHERE
                        b_events.client_id = ?.clientId
                        LIMIT ?.limit OFFSET ?.start""";
        def countQuery2 = """SELECT
                            COUNT(*)
                            FROM
                            booked_event b_events
                            INNER JOIN event_type ON b_events.event_type_id = event_type."id"
                            WHERE
                            b_events.client_id = ?.clientId
                            LIMIT ?.limit OFFSET ?.start""";

        return commonDbFunctions.returnJsonFromQueryWithCount(sqlQuery2, countQuery2, sqlParams, countParamStatus);
    }

    BaseApiResponse getAllBookedEventsDeprecated(Map parameterMap){
        def params = MyUtil.flattenListParam(parameterMap);
        Map sqlParams = [start: 0, limit: 5];
        def countParamStatus = true;

        def start = params.start?.toInteger();
        def limit = params.limit?.toInteger();

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
                        (SELECT(SELECT provider."id" FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 0) AS security_id,
                        (SELECT(SELECT provider.title FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 0) AS security_name,
                        (SELECT(SELECT provider."cost" FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 0) AS security_amount,
                        (SELECT(SELECT planned_event_details.status FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 0) AS security_status,
                        
                        (SELECT(SELECT provider."id" FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 1) AS catering_id,
                        (SELECT(SELECT provider.title FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 1) AS catering_name,
                        (SELECT(SELECT provider."cost" FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 1) AS catering_amount,
                        (SELECT(SELECT planned_event_details.status FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 1) AS catering_status,
                        
                        (SELECT(SELECT provider."id" FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 2) AS entertainment_id,
                        (SELECT(SELECT provider.title FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 2) AS entertainment_name,
                        (SELECT(SELECT provider."cost" FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 2) AS entertainment_amount,
                        (SELECT(SELECT planned_event_details.status FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 2) AS entertainment_status,
                        
                        (SELECT(SELECT provider."id" FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 3) AS  design_id,
                        (SELECT(SELECT provider.title FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 3) AS design_name,
                        (SELECT(SELECT provider."cost" FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 3) AS design_amount,
                        (SELECT(SELECT planned_event_details.status FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 3) AS  design_status,
                        
                        (SELECT(SELECT provider."id" FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 4) AS  mc_id,
                        (SELECT(SELECT provider.title FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 4) AS mc_name,
                        (SELECT(SELECT provider."cost" FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 4) AS mc_amount,
                        (SELECT(SELECT planned_event_details.status FROM planned_event_details 
                        INNER JOIN provider ON planned_event_details.provider_id = provider."id"
                        INNER JOIN provider_category ON provider.category_id = provider_category."id"
                        WHERE provider.category_id = pc."id" AND planned_event_details.event_id = b_events."id") 
                        FROM
                        provider_category pc
                        LIMIT 1 OFFSET 4) AS  mc_status,
                        
                        (SELECT
                        venue."id"
                        FROM
                        planned_event_details
                        INNER JOIN venue ON planned_event_details.venue_id = venue."id"
                        WHERE
                        planned_event_details.event_id = b_events."id") as venue_id,
                        (SELECT
                        venue."name"
                        FROM
                        planned_event_details
                        INNER JOIN venue ON planned_event_details.venue_id = venue."id"
                        WHERE
                        planned_event_details.event_id = b_events."id") as venue_name,
                        (SELECT
                        venue."location"
                        FROM
                        planned_event_details
                        INNER JOIN venue ON planned_event_details.venue_id = venue."id"
                        WHERE
                        planned_event_details.event_id = b_events."id") as venue_location,
                        (SELECT
                        planned_event_details.status
                        FROM
                        planned_event_details
                        INNER JOIN venue ON planned_event_details.venue_id = venue."id"
                        WHERE
                        planned_event_details.event_id = b_events."id") as venue_status,
                        (SELECT user_id FROM event_planner_allocation WHERE event_id = b_events."id" AND status = TRUE) as planner_id
                        FROM
                        booked_event b_events
                        INNER JOIN event_type ON b_events.event_type_id = event_type."id"
                        LIMIT ?.limit OFFSET ?.start""";
        def countQuery2 = """SELECT
                            COUNT(*)
                            FROM
                            booked_event b_events
                            INNER JOIN event_type ON b_events.event_type_id = event_type."id"
                            LIMIT ?.limit OFFSET ?.start""";

        return commonDbFunctions.returnJsonFromQueryWithCount(sqlQuery2, countQuery2, sqlParams, countParamStatus);
    }

    BaseApiResponse bookEvent(EventCreateDto bookedEventDetails, HttpServletRequest request, Principal principal) {
        BaseApiResponse res = new BaseApiResponse(HttpStatus.OK.value(), "Failed to book event")
//        String currentUser  = principal.getName()
//        Long userId = commonDbFunctions.getUserIdFromEmail(currentUser)
        String currentUser = "muriithi91@gmail.com"
        Long userId = 1L
        Long insertRes = insertBookedEventDetails(bookedEventDetails, userId)
        if(insertRes!=null){
            auditService.logAuditEvent("Book Event",request.getRemoteAddr(),currentUser,insertRes+"")
            res.message = "Event booked successfully"
        }

        return res
    }

    BaseApiResponse getAllBookedEvents(Map parameterMap){
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value(), "Success" [])
        Sql sql = new Sql(dataSource)
        def events = sql.rows("""
                                                        SELECT 
                                                               booked_event.id, 
                                                               et.name AS eventType, 
                                                               booked_event.title, 
                                                               booked_event.attendees, 
                                                               booked_event.other_information,
                                                               booked_event.client_id, 
                                                               booked_event.event_type_id, 
                                                               booked_event.greening_status, 
                                                               booked_event.status,
                                                               booked_event.management_amount,
                                                               booked_event.total_amount
                                                        FROM booked_event 
                                                            INNER JOIN event_type et on et.id = booked_event.event_type_id""");
        def providerQuery ="""
                                    SELECT 
                                        category.code, 
                                        category.title AS category_title, 
                                        provider.title, 
                                        provider."cost", 
                                        details.provider_id, 
                                        details.status,
                                        details.id AS planned_details_id
                                    FROM planned_event_details details
                                    INNER JOIN provider ON provider."id" = details.provider_id
                                    INNER JOIN provider_category category ON category.id = provider.category_id
                                    WHERE event_id = ? AND venue_id IS NULL
                                    """

        def venueQuery = """
                                SELECT 
                                    venue_id, 
                                    venue."name", 
                                    venue."location", 
                                    venue.amount, 
                                    details.status,
                                    details.id AS planned_details_id 
                                FROM planned_event_details details
                                INNER JOIN venue ON venue."id" = details.venue_id
                                WHERE venue_id IS NOT NULL and event_id = ?
                             """
        List eventsDetails = []
        events.each{
            def eventId = it.get("id")
            def providerDetails = sql.rows(providerQuery, eventId)
            def venueDetails = sql.firstRow(venueQuery, eventId)
            def plannerId = sql.firstRow("SELECT planner_id FROM planned_event_details WHERE event_id = ? ", eventId).get("planner_id")
            Map groupedProviders = [:]
            providerDetails.each{
                Map structureProvider = [:]
                structureProvider.put("providerId", it.provider_id)
                structureProvider.put("title", it.title)
                structureProvider.put("providerCategory", it.category_title)
                structureProvider.put("cost", it.cost)
                structureProvider.put("plannedStatus", it.status)
                structureProvider.put("plannedDetailsId", it.planned_details_id)
                groupedProviders.put(it.code, structureProvider)
            }
            Map structureVenue = [:]
            structureVenue.put("venueId", venueDetails.get("venue_id"))
            structureVenue.put("name", venueDetails.get("name"))
            structureVenue.put("location", venueDetails.get("location"))
            structureVenue.put("amount", venueDetails.get("amount"))
            structureVenue.put("plannedStatus", venueDetails.get("status"))
            structureVenue.put("plannedDetailsId", venueDetails.get("planned_details_id"))
            groupedProviders.put("venue", structureVenue)
            Map event = [:]
            event.put("eventId", eventId)
            event.put("title", it.get("title"))
            event.put("clientId", it.get("client_id"))
            event.put("eventTypeId", it.get("event_type_id"))
            event.put("eventType", it.get("eventType"))
            event.put("attendees", it.get("attendees"))
            event.put("otherInformation", it.get("other_information"))
            event.put("managementAmount", it.get("management_amount"))
            event.put("totalAmount", it.get("total_amount"))
            event.put("status", it.get("status"))
            event.put("greeningStatus", it.get("greening_status"))
            event.put("plannerId", plannerId)
            event.put("providers", groupedProviders)
            eventsDetails.add(event)
        }
        sql.close()
        res.data = eventsDetails
        res
    }

    BaseApiResponse updateEventStatus(long eventId, IntStatusDto body) {
        //initialize response object
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value(), "update successful", [])

        //fetch event type by id from db using Event Repository
        def optionalEvent= bookedEventRepository.findById(eventId)

        /*
         *check if a record was returned.
         *if a record is not present throw an exception
         *if a record is present, update the status
        */
        if (optionalEvent.isPresent()) {
            //retrieve event type from optionalEventType
            def event = optionalEvent.get()

            //perform update by saving using eventTypeRepository
            def update = bookedEventRepository.updateBookedEventStatus(eventId, body.status)
            if(update < 1){
                throw new InternalServerErrorException("Update failed")
            }

            event.setStatus(body.status)

            //assign updated event details to response data field
            res.data = event
        } else {
            //throw
            throw new BadRequestException(String.format("record with id %s not found", eventId), [])
        }
        return res
    }
}
