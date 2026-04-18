package payment.service;

import payment.core.PaymentMethod;
import payment.exception.PaymentException;
import payment.exception.ProcessingException;
import payment.exception.ValidationException;
import payment.model.PaymentRequest;
import payment.model.PaymentResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentProcessor {
    private final Map<String, PaymentMethod> methodsByKey;

    public PaymentProcessor(List<PaymentMethod> methods) {
        this.methodsByKey = new HashMap<>();
        for (PaymentMethod method : methods) {
            methodsByKey.put(method.getMethodKey().toLowerCase(), method);
        }
    }

    public PaymentResult process(String methodKey, PaymentRequest request) throws PaymentException {
        if (methodKey == null || methodKey.trim().isEmpty()) {
            throw new ValidationException("Odeme yontemi bos olamaz.");
        }
        if (request.getAmount() <= 0) {
            throw new ValidationException("Tutar sifirdan buyuk olmalidir.");
        }

        PaymentMethod method = methodsByKey.get(methodKey.toLowerCase());
        if (method == null) {
            throw new ProcessingException("Desteklenmeyen odeme yontemi: " + methodKey);
        }

        return method.pay(request);
    }
}

