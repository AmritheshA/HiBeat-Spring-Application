package com.Hibeat.Hibeat.Repository.Admin;


import com.Hibeat.Hibeat.Model.Admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository <Admin, Integer>{

    Admin findByAdminName(String adminName);



}
