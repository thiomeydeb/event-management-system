package com.poosh.event.management.adminrole

import com.poosh.event.management.apiresponse.BaseApiResponse
import com.poosh.event.management.utils.MyUtil
import groovy.json.JsonSlurper
import groovy.sql.Sql
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import javax.sql.DataSource

@Service
class AdminRoleService {

    private final AdminRoleRepository adminRoleRepository
    private final DataSource dataSource

    @Autowired //inject the object dependency implicitly
    public AdminRoleService(AdminRoleRepository adminRoleRepository) {
        this.adminRoleRepository = adminRoleRepository
    }

    BaseApiResponse getRoles(Map parameterMap) {
        def params = MyUtil.flattenListParam(parameterMap);
        def start = params.start?.toInteger();
        def limit = params.limit?.toInteger();
        def query = params.query;
        def queryFilter = "";
        def sqlParams = [start: start, limit: limit, query: query];
        def countParamStatus = false;

        def queryStr = "";
        def countStr = "";

        if (query) {
            queryFilter = " AND name ILIKE '%' || ?.query || '%'";
            countParamStatus = true;
        }
        queryStr = "SELECT * FROM admin_roles WHERE id != 0 " + queryFilter + " LIMIT ?.limit OFFSET ?.start"
        countStr = "SELECT COUNT(1) FROM admin_roles WHERE id != 0 " + queryFilter;
        return CommonDbFunctions.returnJsonFromQueryWithCount(queryStr, countStr, sqlParams, countParamStatus);


    }

    public Long addRole(AdminRole role) {
        Sql sql = new Sql(dataSource);
        def insertRes = sql.executeInsert("INSERT INTO admin_roles(name) VALUES (?.name)", role);
        sql.close();
        return insertRes?.get(0)?.get(0);
    }

    BaseApiResponse getAllocatedPemissions(long roleId, Map parameterMap) {
        def params = MyUtil.flattenListParam(parameterMap);
        def start = params.start?.toInteger();
        def limit = params.limit?.toInteger();
        def sqlParams = [roleId: roleId, start: start, limit: limit];
        def query = """SELECT
            admin_permissions.id ,
            admin_permissions.name
            FROM
            admin_permissions,
            admin_role_permissions
            WHERE
            admin_permissions.id = admin_role_permissions.permission_id AND admin_role_permissions.role_id = ?.roleId
            LIMIT ?.limit OFFSET ?.start
        """;
        def countQuery = """SELECT COUNT(1)
                        FROM
                        admin_permissions,
                        admin_role_permissions
                        WHERE
                        admin_permissions.id = admin_role_permissions.permission_id AND admin_role_permissions.role_id = ?.roleId
                """
        return CommonDbFunctions.returnJsonFromQueryWithCount(query, countQuery, sqlParams, true);

    }

    BaseApiResponse getUnAllocatedPemissions(long roleId, Map parameterMap) {

        def params = MyUtil.flattenListParam(parameterMap);
        def start = params.start?.toInteger();
        def limit = params.limit?.toInteger();
        def sqlParams = [roleId: roleId, start: start, limit: limit];

        def query = """SELECT
                        admin_permissions.id,
                        admin_permissions.name
                        FROM
	                    admin_permissions
                        WHERE
	                    NOT EXISTS (SELECT * FROM admin_role_permissions WHERE admin_permissions. ID = admin_role_permissions.permission_id AND admin_role_permissions.role_id = ?.roleId) 
                        LIMIT ?.limit OFFSET ?.start""";

        def countQuery = """SELECT COUNT(1)
                                FROM
                                admin_permissions
                                WHERE
                                NOT EXISTS (SELECT * FROM admin_role_permissions WHERE admin_permissions. ID = admin_role_permissions.permission_id AND admin_role_permissions.role_id = ?.roleId)""";

        return CommonDbFunctions.returnJsonFromQueryWithCount(query, countQuery, sqlParams, true);

    }

    BaseApiResponse allocatePermissions(String permissionsJsonStr, long userId, long roleId) {

        def sqlParams = [userId: userId, roleId: roleId];
        Sql sql = new Sql(dataSource);
        def res = [success: true, status: 0];
        List permissions = new JsonSlurper().parseText(permissionsJsonStr);
        sql.withTransaction {
            permissions.each {
                def permissionId = it.get('permissionId');
                sqlParams.permissionId = permissionId;
                sql.executeInsert("INSERT INTO admin_role_permissions(role_id, permission_id, allocated_by) VALUES (?.roleId, ?.permissionId, ?.userId)", sqlParams);
            }
            res.put('status', 1);
        }
        sql.close();
        return res

    }

    BaseApiResponse deallocatePermissions(String permissionsJsonStr, long roleId) {
        Sql sql = new Sql(dataSource);
        def res = [success: true, status: 0];
        def sqlParams = [roleId: roleId];

        List permissions = new JsonSlurper().parseText(permissionsJsonStr);
        sql.withTransaction {
            permissions.each {
                def permissionId = it.get('permissionId');
                sqlParams.permissionId = permissionId;
                sql.executeInsert("DELETE  FROM admin_role_permissions WHERE role_id = ?.roleId AND permission_id = ?.permissionId", sqlParams);
            }
            res.put('status', 1);
        }
        sql.close();
        return res

    }


    public long updateRole(String roleName, Long roleId) {
        DriverManagerDataSource dataSource = DataSourceFactory.getApplicationDataSource();
        Sql sql = new Sql(dataSource);
        Map res = [success: true, status: 0, message: ""];
        Map params = new JsonSlurper().parseText(roleName);
        params.put("roleId", roleId);
        int updateRes = sql.executeUpdate("UPDATE admin_roles SET name = ?.name WHERE id = ?.roleId", params);
        sql.close();
        return updateRes;
    }
}
