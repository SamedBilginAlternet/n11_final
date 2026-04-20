package payment.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_transactions")
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String method;
    private double amount;
    private String currency;
    private String status;
    private String message;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public PaymentTransaction() {}

    public PaymentTransaction(String method, double amount, String currency, String status, String message) {
        this.method = method;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getMethod() { return method; }
    public double getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
