package com.Hibeat.Hibeat.Repository;

import com.Hibeat.Hibeat.Model.WalletHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletHistoryRepository extends JpaRepository<WalletHistory, Integer> {
}
