package com.Hibeat.Hibeat.Repository.Admin;

import com.Hibeat.Hibeat.Model.Admin.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
