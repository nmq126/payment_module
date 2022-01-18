package com.example.paymentmodule.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDto {

    private Long orderId;
    private Long userId;
    private double totalPrice;
    private String paymentStatus;
    private String orderStatus;

    private String device_token;
}
