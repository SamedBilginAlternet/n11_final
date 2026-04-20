package payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import payment.entity.PaymentTransaction;

public interface PaymentRepository extends JpaRepository<PaymentTransaction, Long> {
}
