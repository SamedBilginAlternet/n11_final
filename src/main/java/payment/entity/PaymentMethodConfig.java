package payment.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "payment_method_configs")
public class PaymentMethodConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String methodKey;

    @Column(nullable = false)
    private String className;

    public PaymentMethodConfig() {}

    public PaymentMethodConfig(String methodKey, String className) {
        this.methodKey = methodKey;
        this.className = className;
    }

    public Long getId() { return id; }
    public String getMethodKey() { return methodKey; }
    public String getClassName() { return className; }
}
