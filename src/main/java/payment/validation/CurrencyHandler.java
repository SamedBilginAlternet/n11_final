package payment.validation;

import org.springframework.stereotype.Component;
import payment.exception.PaymentException;
import payment.exception.ValidationException;
import payment.model.PaymentRequest;
import payment.repository.CurrencyRepository;

import java.util.List;

@Component
public class CurrencyHandler extends PaymentHandler {

    private final CurrencyRepository currencyRepository;

    public CurrencyHandler(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public void handle(PaymentRequest request) throws PaymentException {
        List<String> supported = currencyRepository.findAll()
                .stream()
                .map(c -> c.getCode().toUpperCase())
                .toList();

        if (!supported.contains(request.getCurrency().toUpperCase()))
            throw new ValidationException("Desteklenmeyen para birimi: " + request.getCurrency());

        super.handle(request);
    }
}
