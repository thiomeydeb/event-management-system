package com.poosh.event.management.utils;

import com.poosh.event.management.apiresponse.BaseApiResponse;
import groovy.sql.Sql;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
class CommonDbFunctions {
    private final DataSource dataSource

    @Autowired
    CommonDbFunctions(DataSource dataSource) {
        this.dataSource = dataSource
    }

    BaseApiResponse returnJsonFromQueryWithCount(def sqlQuery, def countQuery, def sqlParams, def countParamStatus){
        Sql sql = new Sql(dataSource);
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value(), "", [])
        def data = sql.rows(sqlQuery, sqlParams);
        def count;
        if (countParamStatus){
            count = sql.firstRow(countQuery, sqlParams).get('count');
        }else{
            count = sql.firstRow(countQuery).get('count');
        }
        res.data = ['data': data, 'total': count]
        sql.close()
        return res
    }

    BaseApiResponse returnJsonFirstRow(def sqlQuery, def sqlParams){
        Sql sql = new Sql(dataSource);
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value(), "", [])
        def data = sql.firstRow(sqlQuery, sqlParams);
        res.data = data
        sql.close()
        return res
    }

    BaseApiResponse returnJsonRows(def sqlQuery, def sqlParams){
        Sql sql = new Sql(dataSource);
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value(), "", [])
        def data = sql.rows(sqlQuery, sqlParams);
        res.data = data
        sql.close();
        return res
    }

    BaseApiResponse returnJsonRowsWithoutParams(def sqlQuery){
        Sql sql = new Sql(dataSource);
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value(), "", [])
        def data = sql.rows(sqlQuery);
        res.data = data
        sql.close();
        return res
    }

    Long getUserIdFromEmail(String email){
        Sql sql = new Sql(dataSource);
        def emailParam = [email: email];
        long userId = sql.firstRow("SELECT id FROM users WHERE email = ?.email", emailParam).get('id');
        sql.close();
        return userId;
    }
}
