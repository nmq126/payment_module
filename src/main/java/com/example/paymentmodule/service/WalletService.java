package com.example.paymentmodule.service;

import com.example.paymentmodule.dto.OrderDto;
import org.springframework.http.ResponseEntity;

public interface WalletService {

    void handlePayment(OrderDto orderDto);

}
