package com.example.paymentmodule.queue;


import com.example.paymentmodule.dto.OrderDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.example.paymentmodule.queue.Config.QUEUE_ORDER;

@Component
public class ReceiveMessage {

        @RabbitListener(queues = {QUEUE_ORDER})
        public void getInfoOrder(OrderDto orderDto) {
            System.out.println("Nhận thông tin order thành công: " + orderDto);
        }

}
