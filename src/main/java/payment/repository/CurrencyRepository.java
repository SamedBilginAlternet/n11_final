package payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import payment.entity.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    boolean existsByCode(String code);
}
