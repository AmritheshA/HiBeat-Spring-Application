package com.Hibeat.Hibeat.Repository.User;

import com.Hibeat.Hibeat.Model.User.User;
import com.Hibeat.Hibeat.Model.User.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Integer> {

    Wallet findByUser(User user);
}
