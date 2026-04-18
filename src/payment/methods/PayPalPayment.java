package payment.methods;

import payment.core.PaymentMethod;
import payment.exception.ProcessingException;
import payment.exception.ValidationException;
import payment.model.PaymentRequest;
import payment.model.PaymentResult;
import payment.model.PaymentStatus;

public class PayPalPayment implements PaymentMethod {
    @Override
    public String getMethodKey() {
        return "paypal";
    }

    @Override
    public PaymentResult pay(PaymentRequest request) throws ValidationException, ProcessingException {
        String email = request.getPayerInfo();
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new ValidationException("PayPal icin gecerli bir e-posta giriniz.");
        }

        if (email.endsWith("@blocked.com")) {
            throw new ProcessingException("PayPal hesabi gecici olarak kullanilamiyor.");
        }

        return new PaymentResult(PaymentStatus.SUCCESS, "PayPal ile odeme basarili.");
    }
}

