package com.example.paymentmodule.controller;

import com.example.paymentmodule.dto.OrderDto;
import com.example.paymentmodule.dto.PaymentDto;
import com.example.paymentmodule.entity.Wallet;
import com.example.paymentmodule.repo.BalletRepo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import static com.example.paymentmodule.queue.Config.*;

@RestController
public class BalletController {

    @Autowired
    BalletRepo balletRepo;

    @Autowired
    RabbitTemplate rabbitTemplate;

//    @RequestMapping(path = "",method = RequestMethod.GET)
//    public Object find(@RequestParam(name = "userId") int userId){
//        return balletRepo.findBalletByUserId(Long.valueOf(userId)) == null;
//    }

    public void handlerPayment(OrderDto orderDto) {
        Wallet ballet = balletRepo.findBalletByUserId(Long.valueOf(orderDto.getUserId()));
        PaymentDto paymentDto = new PaymentDto(orderDto.getOrderId(), orderDto.getUserId());

        if (orderDto.getCheckOut() == 1) {
            paymentDto.setMessage("Order đã thanh toán");
            System.out.println("Order đã thanh toán");
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_PAY, paymentDto);
            return;
        }

        if (ballet == null) {
            paymentDto.setMessage("Tài khoản thanh toán không đúng");
            System.out.println("Tài khoản thanh toán không đúng");
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_PAY, paymentDto);
            return;
        }

        double totalPrice = orderDto.getTotalPrice();
        double balance = ballet.getBalance();

        if (totalPrice > balance) {
            paymentDto.setMessage("Số dư ví không đủ");
            System.out.println("Số dư ví không đủ");
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_PAY, paymentDto);
            return;
        }
        ballet.setBalance(balance - totalPrice);
        try {
            balletRepo.save(ballet);
            paymentDto.setMessage("Thanh toán thành công");
            System.out.println("Thanh toán thành công");
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_PAY, paymentDto);
        } catch (Exception e) {
            ballet.setBalance(balance);
            balletRepo.save(ballet);
            paymentDto.setMessage("Thanh toán lỗi! Vui lòng thử lại");
            System.out.println("Thanh toán lỗi! Vui lòng thử lại");
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_PAY, paymentDto);
        }

    }
}
