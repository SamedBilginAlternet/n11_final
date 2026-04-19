package payment.methods;

import payment.core.PaymentMethod;
import payment.exception.PaymentException;
import payment.model.PaymentRequest;
import payment.model.PaymentResult;
import payment.model.PaymentStatus;

public class PayPalPayment implements PaymentMethod {
    @Override
    public String getMethodKey() {
        return "paypal";
    }

    @Override
    public PaymentResult pay(PaymentRequest request) throws PaymentException {
        return new PaymentResult(PaymentStatus.SUCCESS, "PayPal ile odeme basarili.");
    }
}
