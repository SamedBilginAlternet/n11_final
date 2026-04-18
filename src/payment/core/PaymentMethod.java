package payment.core;

import payment.exception.PaymentException;
import payment.model.PaymentRequest;
import payment.model.PaymentResult;

public interface PaymentMethod {
    String getMethodKey();

    PaymentResult pay(PaymentRequest request) throws PaymentException;
}

