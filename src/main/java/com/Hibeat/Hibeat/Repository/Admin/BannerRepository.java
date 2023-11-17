package com.Hibeat.Hibeat.Repository.Admin;

import com.Hibeat.Hibeat.Model.Admin.Banner;
import com.Hibeat.Hibeat.Model.Admin.Brands;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner,Integer> {

}
