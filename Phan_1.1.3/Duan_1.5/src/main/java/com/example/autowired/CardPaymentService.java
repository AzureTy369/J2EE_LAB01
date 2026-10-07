package com.example.autowired;

import org.springframework.stereotype.Component;

@Component("cardPayment")
public class CardPaymentService implements PaymentService {
    public String pay() { return "Thanh toán bằng thẻ"; }
}
