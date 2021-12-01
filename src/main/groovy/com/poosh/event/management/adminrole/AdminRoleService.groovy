package com.poosh.event.management.adminrole

import com.poosh.event.management.apiresponse.BaseApiResponse
import com.poosh.event.management.audit.AuditService
import com.poosh.event.management.exceptions.InternalServerErrorException
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
class AdminRoleService {

    private final AdminRoleRepository adminRoleRepository
    private final DataSource dataSource
    private final AuditService auditService

    @Autowired //inject the object dependency implicitly
    AdminRoleService(AdminRoleRepository adminRoleRepository,
                     DataSource dataSource,
                     AuditService auditService) {
        this.adminRoleRepository = adminRoleRepository
        this.dataSource = dataSource
        this.auditService = auditService
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

    BaseApiResponse addRole(AdminRole role, HttpServletRequest request, Principal principal) {
        Sql sql = new Sql(dataSource);
        BaseApiResponse res = new BaseApiResponse(HttpStatus.OK.value(), "role added successfully")
        def currentUser = principal.getName()
        def insertRes = sql.executeInsert("INSERT INTO admin_roles(name) VALUES (?.name)", role);
        sql.close();
        def newRoleId = insertRes?.get(0)?.get(0)
        if(newRoleId==null){
            throw new InternalServerErrorException("failed to add role")
        }
        auditService.logAuditEvent("Add user role",request.getRemoteAddr(),currentUser,insertRes+"");
        return res
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

    BaseApiResponse allocatePermissions(String permissionsJsonStr,
                                        long roleId,
                                        HttpServletRequest request,
                                        Principal principal) {
        String currentUser = principal.getName()
        long userId = CommonDbFunctions.getUserIdFromEmail(currentUser)
        def sqlParams = [userId: userId, roleId: roleId];
        Sql sql = new Sql(dataSource);
        BaseApiResponse res = new BaseApiResponse(HttpStatus.OK.value(), "allocation successful")
        List permissions = new JsonSlurper().parseText(permissionsJsonStr);
        sql.withTransaction {
            permissions.each {
                def permissionId = it.get('permissionId');
                sqlParams.permissionId = permissionId;
                sql.executeInsert("INSERT INTO admin_role_permissions(role_id, permission_id, allocated_by) VALUES (?.roleId, ?.permissionId, ?.userId)", sqlParams);
            }
        }
        sql.close();
        auditService.logAuditEvent("Allocate user role",request.getRemoteAddr(),currentUser,"User:"+userId+",Role:"+roleId)
        return res

    }

    BaseApiResponse deallocatePermissions(String permissionsJsonStr,
                                          long roleId,
                                          HttpServletRequest request,
                                          Principal principal) {
        Sql sql = new Sql(dataSource);
        BaseApiResponse res = new BaseApiResponse(HttpStatus.OK.value(), "deallocation successful")
        def sqlParams = [roleId: roleId];

        List permissions = new JsonSlurper().parseText(permissionsJsonStr);
        sql.withTransaction {
            permissions.each {
                def permissionId = it.get('permissionId');
                sqlParams.permissionId = permissionId;
                sql.executeInsert("DELETE  FROM admin_role_permissions WHERE role_id = ?.roleId AND permission_id = ?.permissionId", sqlParams);
            }
        }
        sql.close()
        String currentUser = principal.getName()
        long userId = CommonDbFunctions.getUserIdFromEmail(currentUser)
        auditService.logAuditEvent("Deallocate user role",request.getRemoteAddr(),currentUser,"User:"+userId+",Role:"+roleId);
        return res
    }


    BaseApiResponse updateRole(String roleName, Long roleId, HttpServletRequest request, Principal principal) {
        Sql sql = new Sql(dataSource)
        BaseApiResponse res = new BaseApiResponse(HttpStatus.OK.value(), "update successful")
        Map params = new JsonSlurper().parseText(roleName)
        params.put("roleId", roleId)
        int updateRes = sql.executeUpdate("UPDATE admin_roles SET name = ?.name WHERE id = ?.roleId", params)
        if(updateRes < 1){
            throw new InternalServerErrorException("update failed")
        }
        String currentUser = principal.getName()
        auditService.logAuditEvent("Update user role",request.getRemoteAddr(),currentUser," Role:"+roleId)
        sql.close();
        return res;
    }
}
