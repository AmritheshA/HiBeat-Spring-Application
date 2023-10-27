package com.Hibeat.Hibeat.Repository;

import com.Hibeat.Hibeat.Model.Address;
import lombok.Lombok;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressReposotory extends JpaRepository<Address, Long> {

}
