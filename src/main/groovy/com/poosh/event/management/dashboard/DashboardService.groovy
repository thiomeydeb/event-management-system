package com.poosh.event.management.dashboard

import com.poosh.event.management.apiresponse.BaseApiResponse
import com.poosh.event.management.utils.CommonDbFunctions
import groovy.sql.Sql
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

import javax.sql.DataSource
import java.security.Principal

@Service
class DashboardService {

    private final DataSource dataSource
    private final CommonDbFunctions commonDbFunctions

    DashboardService(DataSource dataSource, CommonDbFunctions commonDbFunctions) {
        this.dataSource = dataSource
        this.commonDbFunctions = commonDbFunctions
    }

    //Admin Methods
    BaseApiResponse getAdminDashboardData(){
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value(), "success", [])
        Map data = [:]
        Sql sql = new Sql(dataSource)

        //collection
        def dailyCollectionResult = sql.firstRow(collectionQuery('day'));
        def monthlyCollectionResult = sql.firstRow(collectionQuery('month'));
        def annualCollectionResult = sql.firstRow(collectionQuery('year'));
        def weeklyCollectionResult = sql.firstRow("""SELECT 
                SUM(management_amount) AS amount FROM booked_event  
                WHERE status = 3 AND EXTRACT(WEEK FROM current_timestamp) = EXTRACT(WEEK FROM timestamp::timestamp)
                AND EXTRACT(YEAR FROM current_timestamp) = EXTRACT(YEAR FROM timestamp::timestamp)
                """);
        def totalCollectionResult = sql.firstRow("SELECT sum(management_amount) as amount FROM booked_event WHERE  status = 3");

        def dailyCollection = dailyCollectionResult.get("amount") == null ? 0.0 : dailyCollectionResult.get("amount");
        def weeklyCollection = weeklyCollectionResult.get("amount") == null ? 0.0 : weeklyCollectionResult.get("amount");
        def monthlyCollection = monthlyCollectionResult.get("amount") == null ? 0.0 : monthlyCollectionResult.get("amount");
        def annualCollection = annualCollectionResult.get("amount") == null ? 0.0 : annualCollectionResult.get("amount");
        def totalCollection = totalCollectionResult.get("amount") == null ? 0.0 : totalCollectionResult.get("amount");

        data.put("dailyCollection", 'KES. '+String.format("%,.2f", dailyCollection));
        data.put("weeklyCollection", 'KES. '+String.format("%,.2f", weeklyCollection));
        data.put("monthlyCollection", 'KES. '+String.format("%,.2f", monthlyCollection));
        data.put("annualCollection", 'KES. '+String.format("%,.2f", annualCollection));
        data.put("totalCollection", 'KES. '+String.format("%,.2f", totalCollection));

        //count
        def totalCompletedEventsResult = sql.firstRow("SELECT count(1) FROM booked_event WHERE status = 3");
        def totalEventsInProgressResult = sql.firstRow("SELECT count(1) FROM booked_event WHERE status = 1 OR status = 2");
        def totalPendingEventsResult = sql.firstRow("SELECT count(1) FROM booked_event WHERE status = 0");
        def totalPlannersResult = sql.firstRow("SELECT count(1) FROM users INNER JOIN user_role_allocation ON users.\"id\" = user_role_allocations.user_id INNER JOIN admin_roles ON admin_roles.\"id\" = user_role_allocations.role_id WHERE admin_roles.\"id\" = 7");
        def registeredUsersResult = sql.firstRow("SELECT count(1) FROM users");
        def activeUsersResult = sql.firstRow("SELECT count(1) FROM users WHERE is_active = TRUE");


        def totalCompletedEvents = totalCompletedEventsResult.get("count") == null ? 0 : totalCompletedEventsResult.get("count");
        def totalEventsInProgress = totalEventsInProgressResult.get("count") == null ? 0 : totalEventsInProgressResult.get("count");
        def totalPendingEvents = totalPendingEventsResult.get("count") == null ? 0 : totalPendingEventsResult.get("count");
        def totalPlanners = totalPlannersResult.get("count") == null ? 0 : totalPlannersResult.get("count");
        def registeredUsers = registeredUsersResult.get("count") == null ? 0 : registeredUsersResult.get("count");
        def activeUsers = activeUsersResult.get("count") == null ? 0 : activeUsersResult.get("count");

        data.put("totalCompletedEvents", totalCompletedEvents);
        data.put("totalPlanners", totalPlanners);
        data.put("registeredUsers", registeredUsers);
        data.put("activeUsers", activeUsers);
        data.put("totalEventsInProgress", totalEventsInProgress);
        data.put("totalPendingEvents", totalPendingEvents);

        res.data = data
        sql.close();
        return res
    }

    String collectionQuery(String datePart){
        if(datePart == "day"){
            return "SELECT sum(management_amount) as amount FROM booked_events WHERE status = 3 " +
                    "AND date_part('$datePart', completion_timestamp) = date_part('$datePart', now()) " +
                    "AND date_part('month', completion_timestamp) = date_part('month', now()) " +
                    "AND date_part('year', completion_timestamp) = date_part('year', now())";
        }else if(datePart == "month"){
            return "SELECT sum(management_amount) as amount FROM booked_events WHERE status = 3 " +
                    "AND date_part('$datePart', completion_timestamp) = date_part('$datePart', now()) " +
                    "AND date_part('year', completion_timestamp) = date_part('year', now())";
        }else if(datePart == "year"){
            return "SELECT sum(management_amount) as amount FROM booked_events WHERE status = 3 AND date_part('$datePart', completion_timestamp) = date_part('$datePart', now())";
        }
    }

    BaseApiResponse totalCollectionsPerEventType (){
        def query = """SELECT name, COALESCE((SELECT SUM(booked_events.management_amount) 
                       FROM booked_events WHERE booked_events.status = 3 AND booked_events.event_type_id = event_type.id ),0) AS amount 
                       FROM event_type""";
        return  commonDbFunctions.returnJsonRowsWithoutParams(query);
    }

    BaseApiResponse totalEventCountPerEventType (){
        def query = """SELECT name, COALESCE((SELECT count(1) FROM booked_events WHERE booked_events.status = 3 AND 
                       booked_events.event_type_id = event_type.id ),0) AS count 
                       FROM event_type""";
        return  commonDbFunctions.returnJsonRowsWithoutParams(query);
    }

    //Client Methods
    BaseApiResponse getClientDashboardData(Principal principal){
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value(), "success", [])
        String email = principal.getName()
        long clientId = commonDbFunctions.getUserIdFromEmail(email)
        Map data = [:];
        Sql sql = new Sql(dataSource);
        Map sqlParam = [status:0, clientId: clientId]
        //collection
        def totalEventResult = sql.firstRow(countQuery(5), sqlParam);
        sqlParam.status = 3;
        def totalCompletedEventResult = sql.firstRow(countQuery(3), sqlParam);
        sqlParam.status = 0;
        def totalEventsInProgressResult = sql.firstRow(countQuery(0), sqlParam);
        sqlParam.status = 4;
        def cancelledEventsResult = sql.firstRow(countQuery(4), sqlParam);

        def totalBookedEvents = totalEventResult.get("count") == null ? 0 : totalEventResult.get("count");
        def totalCompletedEvents = totalCompletedEventResult.get("count") == null ? 0.0 : totalCompletedEventResult.get("count");
        def totalEventsInProgress = totalEventsInProgressResult.get("count") == null ? 0.0 : totalEventsInProgressResult.get("count");
        def totalCancelledEvents = cancelledEventsResult.get("count") == null ? 0.0 : cancelledEventsResult.get("count");


        data.put("totalBookedEvents", totalBookedEvents);
        data.put("timeUpdated", new Date(System.currentTimeMillis()));
        data.put("totalCompletedEvents", totalCompletedEvents);
        data.put("totalEventsInProgress", totalEventsInProgress);
        data.put("totalCancelledEvents", totalCancelledEvents);

        res = data
        sql.close();
        return res
    }

    String countQuery(int status){
        if(status == 3 || status == 4){
            return "SELECT count(*) as \"count\" FROM  booked_events WHERE booked_events.status = ?.status AND booked_events.client_id = ?.clientId";
        }else if(status == 0 ){
            return "SELECT count(*) as \"count\" FROM  booked_events WHERE (booked_events.status = 0 OR booked_events.status = 1 OR booked_events.status = 2) AND booked_events.client_id = ?.clientId";
        }else if(status == 5){
            return "SELECT count(*) as \"count\" FROM  booked_events WHERE  booked_events.client_id = ?.clientId";
        }
    }
}
