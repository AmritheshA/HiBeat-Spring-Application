package com.Hibeat.Hibeat.Repository;


import com.Hibeat.Hibeat.Model.Admin;
import com.Hibeat.Hibeat.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository <Admin, Integer>{

    Admin findByAdminName(String adminName);



}
