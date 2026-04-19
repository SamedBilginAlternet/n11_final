package payment.methods;

import payment.core.PaymentMethod;
import payment.exception.PaymentException;
import payment.model.PaymentRequest;
import payment.model.PaymentResult;
import payment.model.PaymentStatus;

public class CreditCardPayment implements PaymentMethod {
    @Override
    public String getMethodKey() {
        return "creditcard";
    }

    @Override
    public PaymentResult pay(PaymentRequest request) throws PaymentException {
        return new PaymentResult(PaymentStatus.SUCCESS, "Kredi karti ile odeme basarili.");
    }
}
