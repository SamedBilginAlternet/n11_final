package payment.methods;

import payment.core.PaymentMethod;
import payment.exception.ProcessingException;
import payment.exception.ValidationException;
import payment.model.PaymentRequest;
import payment.model.PaymentResult;
import payment.model.PaymentStatus;

public class CreditCardPayment implements PaymentMethod {
    @Override
    public String getMethodKey() {
        return "creditcard";
    }

    @Override
    public PaymentResult pay(PaymentRequest request) throws ValidationException, ProcessingException {
        String cardNumber = request.getPayerInfo();
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            throw new ValidationException("Kredi karti numarasi bos olamaz.");
        }

        if (cardNumber.startsWith("0000")) {
            throw new ProcessingException("Banka islemi reddetti.");
        }

        return new PaymentResult(PaymentStatus.SUCCESS, "Kredi karti ile odeme basarili.");
    }
}

