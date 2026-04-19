package payment.validation;

import payment.exception.PaymentException;
import payment.model.PaymentRequest;

public abstract class PaymentHandler {
    private PaymentHandler next;

    public PaymentHandler setNext(PaymentHandler next) {
        this.next = next;
        return next;
    }

    public void handle(PaymentRequest request) throws PaymentException {
        if (next != null) next.handle(request);
    }
}
