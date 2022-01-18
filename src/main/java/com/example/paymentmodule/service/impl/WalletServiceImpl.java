package com.example.paymentmodule.service.impl;

import com.example.paymentmodule.dto.OrderDto;
import com.example.paymentmodule.dto.PaymentDto;
import com.example.paymentmodule.entity.TransactionHistory;
import com.example.paymentmodule.entity.Wallet;
import com.example.paymentmodule.enums.Status;
import com.example.paymentmodule.exception.NotEnoughBalanceException;
import com.example.paymentmodule.repo.TransactionHistoryRepository;
import com.example.paymentmodule.repo.WalletRepository;
import com.example.paymentmodule.service.WalletService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.paymentmodule.queue.Config.*;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Transactional
    @Override
    public void handlePayment(OrderDto orderDto) {

        PaymentDto paymentDto = new PaymentDto(orderDto.getOrderId(), orderDto.getUserId(), orderDto.getDevice_token());

        if (orderDto.getPaymentStatus().equals(Status.PaymentStatus.PAID.name())) {
            paymentDto.setMessage("Order already paid");
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_PAY, paymentDto);
        }

        Wallet wallet = checkWalletValid(paymentDto);
        if (wallet == null){
            return;
        }

        if (orderDto.getPaymentStatus().equals(Status.PaymentStatus.REFUND.name())) {
            handleRefund(orderDto, paymentDto, wallet);
        }

        double totalPrice = orderDto.getTotalPrice();
        double balance = wallet.getBalance();

        if (totalPrice > balance) {
            paymentDto.setMessage("Account balance not enough");
            paymentDto.setPaymentStatus(Status.PaymentStatus.UNPAID.name());
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_PAY, paymentDto);
            throw new NotEnoughBalanceException("Account balance not enough");
        }

        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.setSenderId(orderDto.getOrderId());
        transactionHistory.setReceiverId(1L); // default
        transactionHistory.setAmount(totalPrice);
        transactionHistory.setMessage("Payment for order " + orderDto.getOrderId());
        transactionHistory.setOrderId(orderDto.getOrderId());
        transactionHistory.setPaymentType(TransactionHistory.PaymentType.TRANSFER);

        try {
            wallet.setBalance(balance - totalPrice);
            walletRepository.save(wallet);
            transactionHistory.setStatus(Status.TransactionStatus.SUCCESS.name());
            transactionHistoryRepository.save(transactionHistory);
            paymentDto.setMessage("Payment success");
            paymentDto.setPaymentStatus(Status.PaymentStatus.PAID.name());

            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_PAY, paymentDto);
        } catch (Exception e) {
            transactionHistory.setStatus(Status.TransactionStatus.FAIL.name());
            transactionHistoryRepository.save(transactionHistory);
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_ORDER, orderDto);
        }
    }

    @Transactional
     public void handleRefund(OrderDto orderDto, PaymentDto paymentDto, Wallet wallet) {

        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.setSenderId(1L);
        transactionHistory.setReceiverId(wallet.getUserId());
        transactionHistory.setAmount(orderDto.getTotalPrice());
        transactionHistory.setMessage("Payment for order " + orderDto.getOrderId());
        transactionHistory.setOrderId(orderDto.getOrderId());
        transactionHistory.setPaymentType(TransactionHistory.PaymentType.REFUND);

        try {
            wallet.setBalance(wallet.getBalance() + orderDto.getTotalPrice());
            walletRepository.save(wallet);
            transactionHistory.setStatus(Status.TransactionStatus.SUCCESS.name());
            transactionHistoryRepository.save(transactionHistory);
            paymentDto.setPaymentStatus(Status.PaymentStatus.REFUNDED.name());
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_PAY, paymentDto);
        }catch (Exception e){
            transactionHistory.setStatus(Status.TransactionStatus.FAIL.name());
            transactionHistoryRepository.save(transactionHistory);
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_ORDER, orderDto);

        }
    }

    private Wallet checkWalletValid(PaymentDto paymentDto){
        if (paymentDto.getUserId() == null){
            paymentDto.setMessage("User id must not be null");
            paymentDto.setPaymentStatus(Status.PaymentStatus.UNPAID.name());
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_PAY, paymentDto);
            return null;
        }
        Wallet wallet = walletRepository.findWalletByUserId(paymentDto.getUserId());

        if (wallet == null) {
            paymentDto.setMessage("Wallet not found");
            paymentDto.setPaymentStatus(Status.PaymentStatus.UNPAID.name());
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_PAY, paymentDto);
            return null;
        }

        return wallet;
    }

}
