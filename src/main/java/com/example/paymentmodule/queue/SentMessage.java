package com.example.paymentmodule.queue;

import com.example.paymentmodule.dto.OrderDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.paymentmodule.queue.Config.DIRECT_EXCHANGE;
import static com.example.paymentmodule.queue.Config.DIRECT_ROUTING_KEY_ORDER;

@RestController
@RequestMapping("")
public class SentMessage {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/createOrder")
    public String sendMessage(@RequestBody OrderDto orderDto) {
        rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_ORDER, orderDto);
        return "Message sent to the RabbitMQ JavaInUse Successfully";
    }

}
