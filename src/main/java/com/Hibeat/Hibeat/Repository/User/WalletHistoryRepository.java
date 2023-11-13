package com.Hibeat.Hibeat.Repository.User;

import com.Hibeat.Hibeat.Model.User.WalletHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletHistoryRepository extends JpaRepository<WalletHistory, Integer> {
}
