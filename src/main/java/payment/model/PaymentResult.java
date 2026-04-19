package payment.model;

public class PaymentResult {
    private final PaymentStatus status;
    private final String message;

    public PaymentResult(PaymentStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}

