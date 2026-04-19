package payment.validation;

import payment.exception.PaymentException;
import payment.exception.ValidationException;
import payment.model.PaymentRequest;

import java.util.List;

public class CurrencyHandler extends PaymentHandler {
    private static final List<String> SUPPORTED = List.of("TRY", "USD", "EUR");

    @Override
    public void handle(PaymentRequest request) throws PaymentException {
        if (!SUPPORTED.contains(request.getCurrency().toUpperCase()))
            throw new ValidationException("Desteklenmeyen para birimi: " + request.getCurrency());
        super.handle(request);
    }
}
