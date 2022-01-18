package com.example.paymentmodule.controller;

import com.example.paymentmodule.dto.OrderDto;
import com.example.paymentmodule.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1/payment")
public class WalletController {

    @Autowired
    private WalletService walletService;


//    @RequestMapping(path = "",method = RequestMethod.GET)
//    public Object find(@RequestParam(name = "userId") int userId){
//        return balletRepo.findBalletByUserId(Long.valueOf(userId)) == null;
//    }

    public void handlePayment(OrderDto orderDto) {
         walletService.handlePayment(orderDto);
    }

}
