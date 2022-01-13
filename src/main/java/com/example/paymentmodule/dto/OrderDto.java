package com.example.paymentmodule.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDto {
    private Long id;
    private Long userId;
    private double totalPrice;
    private int checkOut;
}
