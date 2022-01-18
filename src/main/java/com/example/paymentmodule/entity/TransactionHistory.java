package com.example.paymentmodule.entity;

import com.example.paymentmodule.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transaction_histories")
public class TransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "receiver_id")
    private Long receiverId;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "payment_type")
    private PaymentType paymentType;

    @Column(name = "status")
    private String status;

    @Column(name = "amount")
    private double amount;

    @Column(name = "message")
    private String message;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    public enum PaymentType{
        TRANSFER,
        REFUND,
    }
}
