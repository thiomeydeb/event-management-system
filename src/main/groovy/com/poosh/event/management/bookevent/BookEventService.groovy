package com.poosh.event.management.bookevent

import com.poosh.event.management.apiresponse.BaseApiResponse
import com.poosh.event.management.audit.AuditService
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

    @Autowired //inject the object dependency implicitly
    BookEventService(BookEventRepository bookedEventRepository,
                            DataSource dataSource,
                            AuditService auditService) {
        this.bookedEventRepository = bookedEventRepository
        this.dataSource = dataSource
        this.auditService = auditService
    }

    Long insertBookedEventDetails(String bookedEventDetails, long userId){
        Sql sql = new Sql(dataSource);
        Map bookedEventMap = new JsonSlurper().parseText(bookedEventDetails);
        bookedEventMap.put("clientId",userId);
        Double totalAmount = Double.parseDouble(bookedEventMap.get("totalAmount"));
        Double managementAmount = Double.parseDouble(bookedEventMap.get("managementAmount"));
        bookedEventMap.totalAmount = totalAmount;
        bookedEventMap.managementAmount = managementAmount;
        def insertRes = sql.executeInsert("INSERT INTO booked_events (title, client_id, event_type_id, management_amount, other_information, " +
                "attendees, total_amount) VALUES (?.name, ?.clientId, ?.eventTypeId, ?.managementAmount, ?.otherInformation, ?.attendees, " +
                "?.totalAmount)", bookedEventMap);
        //get event id
        def bookedEventId = insertRes?.get(0)?.get(0);

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
        int updateRes = sql.executeUpdate("UPDATE booked_events SET title = ?.name,event_type_id = ?.eventTypeId, " +
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
                        WHERE
                        b_events.client_id = ?.clientId
                        LIMIT ?.limit OFFSET ?.start""";
        def countQuery2 = """SELECT
                            COUNT(*)
                            FROM
                            booked_events b_events
                            INNER JOIN event_type ON b_events.event_type_id = event_type."id"
                            WHERE
                            b_events.client_id = ?.clientId
                            LIMIT ?.limit OFFSET ?.start""";

        return CommonDbFunctions.returnJsonFromQueryWithCount(sqlQuery2, countQuery2, sqlParams, countParamStatus);
    }

    BaseApiResponse getAllBookedEvents(Map parameterMap){
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
                        LIMIT ?.limit OFFSET ?.start""";
        def countQuery2 = """SELECT
                            COUNT(*)
                            FROM
                            booked_events b_events
                            INNER JOIN event_type ON b_events.event_type_id = event_type."id"
                            LIMIT ?.limit OFFSET ?.start""";

        return CommonDbFunctions.returnJsonFromQueryWithCount(sqlQuery2, countQuery2, sqlParams, countParamStatus);
    }

    BaseApiResponse bookEvent(String bookedEventDetails, HttpServletRequest request, Principal principal) {
        BaseApiResponse res = new BaseApiResponse(HttpStatus.OK.value(), "", [])
        String currentUser  = principal.getName()
        Long userId = CommonDbFunctions.getUserIdFromEmail(currentUser)
        Long insertRes = insertBookedEventDetails(bookedEventDetails, userId)
        if(insertRes!=null){
            auditService.logAuditEvent("Book Event",request.getRemoteAddr(),currentUser,insertRes+"")
            res.message = "Event booked successfully"
        }
    }
}
