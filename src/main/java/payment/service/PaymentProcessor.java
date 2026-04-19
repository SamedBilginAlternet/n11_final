package payment.service;

import payment.core.PaymentMethod;
import payment.exception.PaymentException;
import payment.exception.ProcessingException;
import payment.exception.ValidationException;
import payment.model.PaymentRequest;
import payment.model.PaymentResult;
import payment.validation.PaymentHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentProcessor {
    private final Map<String, PaymentMethod> methodsByKey;
    private final PaymentHandler validationChain;

    public PaymentProcessor(List<PaymentMethod> methods, PaymentHandler validationChain) {
        this.validationChain = validationChain;
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

        return method.pay(request);
    }
}
