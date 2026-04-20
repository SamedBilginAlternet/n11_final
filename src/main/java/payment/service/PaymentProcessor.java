package payment.service;

import payment.core.PaymentMethod;
import payment.entity.PaymentTransaction;
import payment.exception.PaymentException;
import payment.exception.ProcessingException;
import payment.exception.ValidationException;
import payment.factory.PaymentMethodFactory;
import payment.model.PaymentRequest;
import payment.model.PaymentResult;
import payment.repository.CurrencyRepository;
import payment.repository.PaymentRepository;
import payment.validation.PaymentHandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PaymentProcessor {
    private final PaymentMethodFactory factory;
    private final PaymentHandler validationChain;
    private final PaymentRepository paymentRepository;
    private final CurrencyRepository currencyRepository;

    public PaymentProcessor(PaymentMethodFactory factory, PaymentHandler validationChain, PaymentRepository paymentRepository, CurrencyRepository currencyRepository) {
        this.factory = factory;
        this.validationChain = validationChain;
        this.paymentRepository = paymentRepository;
        this.currencyRepository = currencyRepository;
    }

    public List<String> getAvailableMethodKeys() {
        return factory.createAll().stream()
                .map(m -> m.getMethodKey().toLowerCase())
                .collect(Collectors.toList());
    }

    public List<String> getAvailableCurrencies() {
        return currencyRepository.findAll().stream()
                .map(c -> c.getCode().toUpperCase())
                .toList();
    }

    public PaymentResult process(String methodKey, PaymentRequest request) throws PaymentException {
        if (methodKey == null || methodKey.trim().isEmpty()) {
            throw new ValidationException("Odeme yontemi bos olamaz.");
        }

        validationChain.handle(request);

        Map<String, PaymentMethod> methodsByKey = factory.createAll().stream()
                .collect(Collectors.toMap(m -> m.getMethodKey().toLowerCase(), m -> m));

        PaymentMethod method = methodsByKey.get(methodKey.toLowerCase());
        if (method == null) {
            throw new ProcessingException("Desteklenmeyen odeme yontemi: " + methodKey);
        }

        PaymentResult result = method.pay(request);

        paymentRepository.save(new PaymentTransaction(
                methodKey,
                request.getAmount(),
                request.getCurrency(),
                result.getStatus().name(),
                result.getMessage()
        ));

        return result;
    }
}
