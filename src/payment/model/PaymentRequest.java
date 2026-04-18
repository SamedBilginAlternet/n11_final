package payment.model;

public class PaymentRequest {
    private final double amount;
    private final String currency;
    private final String payerInfo;

    public PaymentRequest(double amount, String currency, String payerInfo) {
        this.amount = amount;
        this.currency = currency;
        this.payerInfo = payerInfo;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPayerInfo() {
        return payerInfo;
    }
}

