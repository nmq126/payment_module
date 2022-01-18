package com.example.paymentmodule.repo;

import com.example.paymentmodule.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Wallet findWalletByUserId(Long id);
}
