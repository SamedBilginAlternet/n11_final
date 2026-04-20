package payment.service;

import payment.core.PaymentMethod;
import payment.entity.PaymentTransaction;
import payment.exception.PaymentException;
import payment.exception.ProcessingException;
import payment.exception.ValidationException;
import payment.model.PaymentRequest;
import payment.model.PaymentResult;
import payment.repository.PaymentRepository;
import payment.validation.PaymentHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentProcessor {
    private final Map<String, PaymentMethod> methodsByKey;
    private final PaymentHandler validationChain;
    private final PaymentRepository paymentRepository;

    public PaymentProcessor(List<PaymentMethod> methods, PaymentHandler validationChain, PaymentRepository paymentRepository) {
        this.validationChain = validationChain;
        this.paymentRepository = paymentRepository;
        this.methodsByKey = new HashMap<>();
        for (PaymentMethod method : methods) {
            methodsByKey.put(method.getMethodKey().toLowerCase(), method);
        }
    }

    public List<String> getAvailableMethodKeys() {
        return new ArrayList<>(methodsByKey.keySet());
    }

    public PaymentResult process(String methodKey, PaymentRequest request) throws PaymentException {
        if (methodKey == null || methodKey.trim().isEmpty()) {
            throw new ValidationException("Odeme yontemi bos olamaz.");
        }

        validationChain.handle(request);

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
