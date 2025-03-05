package com.grupoeimsa.sigeim.models.users.model;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface IUser extends JpaRepository<BeanUser, Long> {
    Optional<BeanUser> findBeanUserByEmail (String email);
    boolean existsBeanUserByEmail (String email);
}
