package com.poosh.event.management.userloginlogs;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginLogRepository extends CrudRepository <UserLoginLog, Long>{
    void updateUserLoginLogStatus(Long id, boolean status);

}
