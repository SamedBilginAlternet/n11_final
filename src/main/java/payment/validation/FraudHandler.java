package payment.validation;

import org.springframework.stereotype.Component;
import payment.exception.PaymentException;
import payment.exception.ProcessingException;
import payment.model.PaymentRequest;

@Component
public class FraudHandler extends PaymentHandler {
    @Override
    public void handle(PaymentRequest request) throws PaymentException {
        if (request.getAmount() > 50_000)
            throw new ProcessingException("Islem fraud kontrolunden gecemedi.");
        super.handle(request);
    }
}
