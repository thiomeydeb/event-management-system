package com.poosh.event.management.user

import com.poosh.event.management.apiresponse.BaseApiResponse
import com.poosh.event.management.exceptions.BadRequestException
import com.poosh.event.management.exceptions.InternalServerErrorException
import com.poosh.event.management.passwordmanagement.PasswordManagementService
import com.poosh.event.management.user.dto.UserCreateDto
import com.poosh.event.management.user.dto.UserUpdateDto
import com.poosh.event.management.utils.CommonDbFunctions
import com.poosh.event.management.utils.Constant
import com.poosh.event.management.utils.EmailSender
import com.poosh.event.management.utils.MyUtil
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.sql.Sql
import org.modelmapper.ModelMapper
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

import javax.sql.DataSource

@Service
class UserService {
    private final UserRepository userRepository
    private final ModelMapper modelMapper
    private final PasswordEncoder passwordEncoder
    private final PasswordManagementService passwordManagementService
    private final DataSource dataSource
    private final EmailSender emailSender

    @Autowired
    UserService(UserRepository userRepository,
                ModelMapper modelMapper,
                PasswordEncoder passwordEncoder,
                PasswordManagementService passwordManagementService,
                DataSource dataSource,
                EmailSender emailSender) {
        this.userRepository = userRepository
        this.modelMapper = modelMapper
        this.passwordEncoder = passwordEncoder
        this.passwordManagementService = passwordManagementService
        this.dataSource = dataSource
        this.emailSender = emailSender
    }

    BaseApiResponse addUser(UserCreateDto body, int registrationType) {
        //initialize response object
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value(), "successfully added provider", [])

        def email = body.email
        def optionalUser = userRepository.findUserByEmailEquals(email)

        if (optionalUser.isPresent()) {
            throw new BadRequestException(String.format("User with email %s already exists", email), [])
        } else {
            def user = modelMapper.map(body, User.class)
            /*set default value for isActive field.
         *this is to prevent an error since mapping will insert a null value which is against the table's not null constraint
        */
            user.setActive(true)
            //save new user using UserRepository
            def savedUser = userRepository.save(user)

            if (savedUser) {
                long savedUserId = savedUser.getId();
                if (registrationType == 2) {
                    String hashedPassword = passwordEncoder.encode(user.password);
                    int passwordUpdateRes = userRepository.updateUserPassword(savedUserId, hashedPassword)
                    if (passwordUpdateRes > 0) {
                        passwordManagementService.insertPasswordHistory(savedUserId, hashedPassword);
                    }
                    sendAccountActivationLink(savedUserId);
                } else {
                    userRepository.updateUserStatus(savedUserId, true)
                    sendPasswordResetEmail(savedUserId);
                }
                res.message = "Success. A confirmation email has been sent to " + email + ". Kindly follow the link in your email to complete the registration process"
            } else {
                res.setStatus(0);
                res.setMessage("Could not save user details");
                throw new InternalServerErrorException("Could not save user details")
            }
        }

        return res;
    }

     BaseApiResponse getUserDetailsByEmail(String email){
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value(), "success", [])
        def data = getUserDetailsByEmailMap(email)
        def optionalUser = userRepository.findUserByEmailEquals(email)
        if(!optionalUser.isPresent()){
            throw new InternalServerErrorException(String.format("Failed to fetch user with email: %s"), email)
        }
        res.data = optionalUser.get()
        return res

    }

     Long getUserIdFromToken(String token){
        Sql sql = new Sql(dataSource);
        def userId = sql.firstRow("SELECT user_id FROM account_activation_tokens WHERE token = ?",token).get("user_id");
        return userId;
    }

     boolean activateMerchantAccount(long userId,String token){
        Sql sql = new Sql(dataSource);
        boolean status = false;
        sql.withTransaction {
            sql.executeUpdate("UPDATE account_activation_tokens SET is_active = FALSE WHERE token = ?",token);
            sql.executeUpdate("UPDATE users SET is_active = TRUE WHERE id = ?",userId);
            assignClientRole(userId,0,userId);
            status = true;
        }
        sql.close();
        return status;

    }

     boolean assignClientRole(long userId,long roleId,long assignedBy){
        Sql sql = new Sql(dataSource);
        def sqlParams = [userId: userId, roleId: roleId, loggedInUser: assignedBy];
        sql.execute("INSERT INTO user_role_allocation(user_id, role_id, allocated_by) VALUES (?.userId, ?.roleId, ?.loggedInUser)", sqlParams);
        sql.close();
        return true;
    }


     boolean checkTokenValidity(String token){
        Sql sql = new Sql(dataSource);
        def query = "SELECT token FROM account_activation_tokens WHERE token = ? AND is_active = TRUE AND now() < (add_date + interval '1 day')";
        def resQuery = sql.firstRow(query, token);
        sql.close();
        if(resQuery){
            return true;
        }else{
            return false;
        }
    }

     void sendPasswordResetEmail(long userId){
        Sql sql = new Sql(dataSource);
        String tokenStr = UUID.randomUUID().toString();
        passwordManagementService.insertPasswordToken(userId,tokenStr);

        String passwordResetUrl = Constant.BASE_APP_URL+"password/change/"+tokenStr;
        def passwordResetEmailMap = sql.firstRow("SELECT  * FROM  email_template WHERE id = 1");
        String userEmail = getEmailFromUserId(userId);
        sql.close();
        emailSender.sendMail(Constant.FROM_EMAIL, userEmail, passwordResetEmailMap.get("subject"), passwordResetEmailMap.get("message")+passwordResetUrl);

    }

     void sendAccountActivationLink(long userId){
        Sql sql = new Sql(dataSource);
        String tokenStr = UUID.randomUUID().toString();
        insertAccountActivationToken(userId,tokenStr);
        String accountActivationUrl = Constant.BASE_APP_URL+"useraccount/activate/"+tokenStr;
        def accountActivationEmailMap = sql.firstRow("SELECT  * FROM  email_template WHERE id = 3");
        String userEmail = getEmailFromUserId(userId);
        sql.close();
        emailSender.sendMail(Constant.FROM_EMAIL, userEmail, accountActivationEmailMap.get("subject"), accountActivationEmailMap.get("message")+"</br>"+accountActivationUrl);

    }

     String getEmailFromUserId(long id){
        Sql sql = new Sql(dataSource);
        def email = sql.firstRow("SELECT  users.email FROM  users WHERE  id = ?",id).get("email");
        sql.close();
        return email;

    }

     boolean insertAccountActivationToken(long userId,String token){
        Sql sql = new Sql(dataSource);
        def res = false;
        def params = [userId: userId, token: token];
        def queryStatus = sql.executeInsert("INSERT INTO account_activation_tokens(token, user_id) VALUES (?.token, ?.userId)", params);
        sql.close();
        if(queryStatus){
            res = true;
        }
        return res;
    }



     def getUserDetailsByEmailMap(String email){
        Sql sql = new Sql(dataSource);

        def sqlParams = [email: email];
        def sqlQuery = """SELECT
        users.id,
        users.first_name,
        users.middle_name,
        users.last_name,
        users.email,
        users.phone_number,
        users.identification_type,
        users.identification_number,
        users.is_active,
        users.company_name,
        admin_roles.name role_name,
        admin_roles.id role_id
        FROM
        users
        INNER JOIN user_role_allocations ON users.id = user_role_allocations.user_id
        INNER JOIN admin_roles ON user_role_allocations.role_id = admin_roles.id
        WHERE  users.email = ?.email""";

        def data = sql.firstRow(sqlQuery, sqlParams);
        sql.close();
        return data;
    }

     BaseApiResponse getUsers(def parameterMap){

        def params = MyUtil.flattenListParam(parameterMap);
        Map sqlParams = [start: 0, limit: 5];
        def countParamStatus = false;

        def start = params.start?.toInteger();
        def limit = params.limit?.toInteger();
        def paramQuery  = params.query;
        def fetchAdminOnly = params.admin?.toBoolean();

        sqlParams.start =  start;
        sqlParams.limit = limit;
        sqlParams.paramQuery = paramQuery;

        def adminFilterQuery = "";
        def queryFilterQuery = "";
        def queryFilterPrefix = "";

        if(fetchAdminOnly){
            adminFilterQuery  = " WHERE EXISTS (SELECT * FROM admin_users WHERE user_id = users.id AND is_active = TRUE) ";
            queryFilterPrefix = " AND "

        }else{
            queryFilterPrefix = " WHERE "
        }


        if(paramQuery!=null){
            def vectorQuery = paramQuery.replaceAll(' ','&');
            sqlParams.vectorQuery = vectorQuery;
            queryFilterQuery = queryFilterPrefix+ " to_tsvector(users.first_name|| ' ' || users.middle_name || ' ' || users.last_name) @@ to_tsquery(?.vectorQuery) OR users.email ILIKE ?.paramQuery OR users.identification_number = ?.paramQuery ";
            countParamStatus = true;
        }
        def queryFilter = adminFilterQuery+queryFilterQuery;

        def query = """SELECT
                users.id,
                users.first_name  "firstName",
                users.middle_name "middleName",
                users.last_name "lastName",
                users.email,
                users.phone_number "phoneNumber",
                users.identification_type "identificationType",
                users.identification_number "identificationNumber",
                users.is_active "isActive",
                users.company_name "companyName",
                EXISTS (SELECT * FROM admin_users WHERE user_id = users.id AND is_active = TRUE) AS "isAdmin"
                FROM
                users """+queryFilter+" LIMIT ?.limit OFFSET ?.start";

        def countQuery = """SELECT COUNT(1) FROM users """+queryFilter;
        return  CommonDbFunctions.returnJsonFromQueryWithCount(query,countQuery, sqlParams, countParamStatus);

    }

     long getUserIdFromEmail(String email){
        Sql sql = new Sql(dataSource);
        def userId = sql.firstRow("SELECT  users.id FROM  users WHERE  email = ?",email).get("id");
        return userId;

        sql.close();
    }

    BaseApiResponse changeStatus(int userId,boolean value){
        BaseApiResponse res = new BaseApiResponse(HttpStatus.OK.value(), "Could not reset user status")
        def affectedRows = userRepository.updateUserStatus(userId, value)
        if(affectedRows == 1){
            res.message = "The user status has been reset"
        }else{
            throw new IllegalStateException("Could not reset user status")
        }
        res
    }

     BaseApiResponse getAllocatedUserRole(long userId){
        def sqlParams = [userId: userId];
        def query  = """SELECT
        admin_roles.id,
        admin_roles.name
        FROM
        admin_roles
        INNER JOIN user_role_allocations ON user_role_allocations.role_id = admin_roles.id
        WHERE user_role_allocations.user_id = ?.userId
        """;
        return  CommonDbFunctions.returnJsonFirstRow(query, sqlParams);
    }

     BaseApiResponse promoteToAdmin(long userId,long loggedInUser){
        Sql sql = new Sql(dataSource);
        BaseApiResponse res = new BaseApiResponse(HttpStatus.OK.value(), "User Promoted to admin")
        def sqlParams = [userId: userId, loggedInUser: loggedInUser];
        def userExistingRecords = sql.firstRow("SELECT * FROM admin_user WHERE user_id = ?.userId", sqlParams);
        def queryStatus;
        if(userExistingRecords!=null){
            queryStatus =  sql.executeUpdate("UPDATE admin_user SET is_active = TRUE WHERE user_id = ?.userId", sqlParams);
        }else{
            queryStatus = sql.executeInsert("INSERT INTO admin_user(user_id, added_by, is_active) VALUES (?.userId, ?.loggedInUser,TRUE)", sqlParams);
        }
        sql.close();
        if(queryStatus){
            res.message  = 'User Promoted to admin';

        }else{
            throw new InternalServerErrorException("Failed to promote user to admin")
        }
        return res
    }

    BaseApiResponse demoteFromAdmin(long userId,long loggedInUser){
        Sql sql = new Sql(dataSource);
        BaseApiResponse res = new BaseApiResponse(HttpStatus.OK.value(), "User demoted from to admin")
        sql.executeUpdate("UPDATE admin_user SET is_active = FALSE WHERE user_id = ?",userId);
        sql.close();

        return res
    }

    BaseApiResponse assignRole(long userId,long roleId,long loggedInUser){
        Sql sql = new Sql(dataSource);
        def sqlParams = [userId: userId, roleId: roleId, loggedInUser: loggedInUser];
        BaseApiResponse res = new BaseApiResponse(HttpStatus.OK.value(), "User role allocated")
        sql.execute("INSERT INTO user_role_allocation(user_id, role_id, allocated_by) VALUES (?.userId, ?.roleId, ?.loggedInUser)", sqlParams);
        sql.close();

        return res
    }

    BaseApiResponse deallocateRole(long userId,long roleId,long loggedInUser){
        Sql sql = new Sql(dataSource);
        def params = [userId: userId, roleId: roleId];
        BaseApiResponse res = new BaseApiResponse(HttpStatus.OK.value(), "User role deallocated")
        def queryStatus = sql.execute("DELETE FROM  user_role_allocation WHERE user_id = ?.userId AND role_id = ?.roleId", params);
        sql.close();
        return res
    }

     BaseApiResponse getMenuDetails(long userId){
        def sqlParams = [userId: userId];
        def childrenQuery  = """SELECT
                admin_permissions."name" as text,
                admin_permissions.xtype "viewType",
                admin_permissions.parent_menu_id,
                admin_permissions.xtype "routeId",
                admin_permissions.icon_cls "iconCls",
                admin_permissions.leaf AS leaf,
                admin_permissions.menu_priority
                FROM
                admin_roles
                INNER JOIN user_role_allocations ON user_role_allocations.role_id = admin_roles."id"
                INNER JOIN admin_role_permissions ON user_role_allocations.role_id = admin_role_permissions.role_id
                INNER JOIN admin_permissions ON admin_role_permissions.permission_id = admin_permissions."id"
                WHERE
                user_role_allocations.user_id = ?.userId
                """;

        Sql sql = new Sql(dataSource);
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value(), [])
        def childrenPermissions = sql.rows(childrenQuery, sqlParams);

        List parentIds = [];
        childrenPermissions.each {
            if(it.get("parent_menu_id")){
                parentIds.add(it.get("parent_menu_id"));
            }
        }

        def parentMenus = [];

        if(parentIds){
            def commaSeparatedParentIds = parentIds.join(",")
            parentMenus = sql.rows("SELECT * FROM admin_menu_parents WHERE  id IN ("+commaSeparatedParentIds+")");
        }

        List menuResponse = [];
        List parentMenuResponse = [];

        if(parentMenus){
            parentMenus.each {
                def parentId = it.get("id");
                Map parentResponse = [:];
                List parentChildren = childrenPermissions.findAll({
                    it.get("parent_menu_id") == parentId;
                });

                def sortedParentChildren = parentChildren.sort { a, b -> a.menu_priority <=> b.menu_priority }

                parentResponse.put("text",it.get("name"));
                parentResponse.put("iconCls",it.get("icon_cls"));
                parentResponse.put("expanded",it.get("expanded"));
                parentResponse.put("leaf",false);
                parentResponse.put("menu_priority",it.get("menu_priority"));
                parentResponse.put("children",sortedParentChildren);
                parentMenuResponse.add(parentResponse);
            }
        }
        List parentLessChildren = childrenPermissions.findAll({
            !it.get("parent_menu_id");
        });
        parentLessChildren.each {
            it.remove("parent_menu_id");
        }
        menuResponse = parentMenuResponse+parentLessChildren;
        List sortedMenuResponse = menuResponse.sort { a, b -> a.menu_priority <=> b.menu_priority }
        def dataMap = [expanded: true, children: sortedMenuResponse];
        res.data = dataMap;
        sql.close();
        return res;
    }

     boolean logLoginAttempt(def ipAddress,boolean success,String email) {
        Sql sql = new Sql(dataSource);
        Map params = ["ip": ipAddress, "success": success, "email": email];
        def insertRes = sql.executeInsert("INSERT INTO user_login_log(ip_address, success, entered_email,login_time) VALUES (?.ip,?.success,?.email,current_timestamp)", params);
        sql.close();
        return !!insertRes;
    }

     BaseApiResponse updateProfile(long userId, String userDetailsJsonStr){
        Sql sql = new Sql(dataSource);
        BaseApiResponse res = new BaseApiResponse(HttpStatus.OK.value(), "Update successful")

        def userDetails = new JsonSlurper().parseText(userDetailsJsonStr);

        def firstName = userDetails.firstName;
        def middleName = userDetails.middleName;
        def lastName = userDetails.lastName;
        def email = userDetails.email;
        def phoneNumber = userDetails.phoneNumber;
        def identificationType = userDetails.identificationType;
        def identificationNumber = userDetails.identificationNumber;

        Map queryParams = [firstName:firstName, middleName:middleName, lastName:lastName, email:email, phoneNumber:phoneNumber, identificationType:identificationType, identificationNumber:identificationNumber, userId: userId];

        int update = sql.executeUpdate("UPDATE users SET first_name = ?.firstName, middle_name = ?.middleName, last_name = ?.lastName, email = ?.email, phone_number = ?.phoneNumber, identification_type = ?.identificationType, identification_number = ?.identificationNumber WHERE id = ?.userId", queryParams);
        sql.close();
        if(!(update > 0)){
            throw new InternalServerErrorException("Update failed")
        }

        return res
    }

     Boolean addUser(def usersEntity){
        Sql sql = new Sql(dataSource);
        boolean status = false;
        def insertUser = sql.executeInsert("INSERT INTO users (first_name, middle_name, last_name, email, phone_number, identification_type, identification_number, company_name) VALUES(?.firstName, ?.middleName, ?.lastName, ?.email, ?.phoneNumber, ?.identificationType, ?.identificationNumber, ?.companyName)",usersEntity);
        sql.close();
        if(insertUser){
            status = true;
        }
        return status;
    }

     Boolean updateUserStatus(long userId){
        Sql sql = new Sql(dataSource);
        boolean status = false;
        int update = sql.executeUpdate("UPDATE users SET is_active = TRUE WHERE id = ?", userId);
        sql.close();
        if(update > 0){
            status = true
        }
        return status;
    }

    BaseApiResponse getActiveUsers(List<Map<String,String>>userDetails){
        Sql sql = new Sql(dataSource);
        List<Map<String, String>> fetchedUserDetails = new ArrayList<>();
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value, "success", [])
        userDetails.each {
            def email = it.get("email");
            def sessionId = it.get("sessionId");
            String sqlQuery = "SELECT first_name, middle_name, last_name, email, phone_number FROM users WHERE email=?";
            def sqlData = sql.firstRow(sqlQuery, email);
            sqlData.put("session_id", sessionId);
            fetchedUserDetails.add(sqlData);
        }
        res.data = fetchedUserDetails
        sql.close();
        return res
    }

    BaseApiResponse getUsersBasedOnRole(def parameterMap, int roleId){

        def params = MyUtil.flattenListParam(parameterMap);
        Map sqlParams = [start: 0, limit: 5];
        def countParamStatus = true;

        def start = params.start?.toInteger();
        def limit = params.limit?.toInteger();
        def paramQuery  = params.query;
        def fetchAdminOnly = params.admin?.toBoolean();

        sqlParams.start =  start;
        sqlParams.limit = limit;
        sqlParams.paramQuery = paramQuery;
        sqlParams.roleId = roleId;

        def adminFilterQuery = "";
        def queryFilterQuery = "";
        def queryFilterPrefix = "";
        def roleQuery = "";
        def queryFilterSuffix = " WHERE ";

        if(fetchAdminOnly){
            adminFilterQuery  = " WHERE EXISTS (SELECT * FROM admin_users WHERE user_id = users.id AND is_active = TRUE) ";
            queryFilterPrefix = " AND "

        }else{
            queryFilterPrefix = " WHERE "
        }


        if(paramQuery!=null){
            def vectorQuery = paramQuery.replaceAll(' ','&');
            sqlParams.vectorQuery = vectorQuery;
            queryFilterQuery = queryFilterPrefix+ " (to_tsvector(users.first_name|| ' ' || users.middle_name || ' ' || users.last_name) @@ to_tsquery(?.vectorQuery) OR users.email ILIKE ?.paramQuery OR users.identification_number = ?.paramQuery) ";
            queryFilterSuffix = " AND ";
        }
        def queryFilter = adminFilterQuery+queryFilterQuery+queryFilterSuffix;

        def query = """SELECT
                users.id,
                users.first_name  "firstName",
                users.middle_name "middleName",
                users.last_name "lastName",
                users.email,
                users.phone_number "phoneNumber",
                users.identification_type "identificationType",
                users.identification_number "identificationNumber",
                users.is_active "isActive",
                users.company_name "companyName",
                EXISTS (SELECT * FROM admin_users WHERE user_id = users.id AND is_active = TRUE) AS "isAdmin"
                FROM
                users
                INNER JOIN user_role_allocations ON users.id = user_role_allocations.user_id
                """+queryFilter+"user_role_allocations.role_id = ?.roleId LIMIT ?.limit OFFSET ?.start";

        def countQuery = """SELECT COUNT(1) FROM users 
                            INNER JOIN user_role_allocations ON users.id = user_role_allocations.user_id 
                            """+queryFilter+"user_role_allocations.role_id = ?.roleId LIMIT ?.limit OFFSET ?.start";

        return  CommonDbFunctions.returnJsonFromQueryWithCount(query,countQuery, sqlParams, countParamStatus);

    }


    BaseApiResponse updateUser(long userId, UserUpdateDto user) {
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value, "success", [])
        modelMapper.map(user, User.class)
        user.setId(userId);
        def optionalExistingUser = userRepository.findById(userId);
        if(!optionalExistingUser.isPresent()){
            throw new InternalServerErrorException("user not found");
        }
        User existingUser = optionalExistingUser.get()
        String [] ignoreFields = ["password", "email", "active"];
        BeanUtils.copyProperties(user, existingUser, ignoreFields);
        def updatedUser = userRepository.save(existingUser)
        if(!updatedUser){
            throw new InternalServerErrorException("failed to update user")
        }
        res.setMessage("User details updated");
        return res;
    }

    BaseApiResponse registerUser(UserCreateDto user) {
        String password = user.getPassword()
        String confirmPassword = user.getConfirmPassword()
        BaseApiResponse res = new BaseApiResponse([], HttpStatus.OK.value, "", [])
        if(password..equals(confirmPassword)){
            if(passwordManagementService.isPasswordStrong(password)){
                return addUser(user,2);
            }else{
                throw new BadRequestException("The password must have at least one uppercase letter, one lowercase letter, one number, one special character and should have at least eight characters in length")
            }
        }else{
            throw new BadRequestException("Password Mismatch")
        }
    }
}
