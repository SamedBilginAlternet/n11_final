package payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import payment.entity.PaymentMethodConfig;

public interface PaymentMethodConfigRepository extends JpaRepository<PaymentMethodConfig, Long> {
    boolean existsByMethodKey(String methodKey);
}
