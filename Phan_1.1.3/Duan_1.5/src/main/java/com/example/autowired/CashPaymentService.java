package com.example.autowired;

import org.springframework.stereotype.Component;

@Component("cashPayment")
public class CashPaymentService implements PaymentService {
    public String pay() { return "Thanh toán tiền mặt"; }
}
