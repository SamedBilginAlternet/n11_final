package payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import payment.core.PaymentMethod;
import payment.factory.PaymentMethodFactory;
import payment.service.PaymentProcessor;

import java.util.List;

@Configuration
public class PaymentConfig {

    @Bean
    public List<PaymentMethod> paymentMethods() {
        return PaymentMethodFactory.createAll();
    }

    @Bean
    public PaymentProcessor paymentProcessor(List<PaymentMethod> paymentMethods) {
        return new PaymentProcessor(paymentMethods);
    }
}
