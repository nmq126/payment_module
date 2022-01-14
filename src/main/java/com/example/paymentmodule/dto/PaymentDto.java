package com.example.paymentmodule.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentDto {

    private Long orderId;
    private Long userId;
    private int checkOut;
    private String message;

    public PaymentDto(Long orderId, Long userId) {
        this.orderId = orderId;
        this.userId = userId;
    }
}
