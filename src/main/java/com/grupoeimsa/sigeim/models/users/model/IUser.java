package com.grupoeimsa.sigeim.models.users.model;


import org.springframework.data.jpa.repository.JpaRepository;


public interface IUser extends JpaRepository<BeanUser, Long> {


}
