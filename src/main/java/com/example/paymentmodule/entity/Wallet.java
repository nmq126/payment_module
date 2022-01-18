package com.example.paymentmodule.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "wallets")
public class Wallet {
    @Id
    @Column(name = "id", nullable = false)
    private Long userId;
    private double balance;

}
