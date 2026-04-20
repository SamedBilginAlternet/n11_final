package payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import payment.entity.Currency;

import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    boolean existsByCode(String code);
    List<String> findAllBy();
}
