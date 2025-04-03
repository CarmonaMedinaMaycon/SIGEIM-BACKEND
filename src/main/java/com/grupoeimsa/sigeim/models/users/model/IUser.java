package com.grupoeimsa.sigeim.models.users.model;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;


public interface IUser extends JpaRepository<BeanUser, Long>, JpaSpecificationExecutor<BeanUser> {
    Optional<BeanUser> findBeanUserByEmail (String email);
    boolean existsBeanUserByEmail (String email);

    long countByRoleAndStatus(ERole role, boolean status);




}
