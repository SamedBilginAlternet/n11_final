package payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import payment.core.PaymentMethod;
import payment.factory.PaymentMethodFactory;
import payment.service.PaymentProcessor;
import payment.validation.AmountHandler;
import payment.validation.CurrencyHandler;
import payment.validation.FraudHandler;
import payment.validation.PaymentHandler;

import java.util.List;

@Configuration
public class PaymentConfig {

    @Bean
    public List<PaymentMethod> paymentMethods() {
        return PaymentMethodFactory.createAll();
    }

    @Bean
    public PaymentHandler validationChain() {
        PaymentHandler chain = new AmountHandler();
        chain.setNext(new CurrencyHandler())
             .setNext(new FraudHandler());
        return chain;
    }

    @Bean
    public PaymentProcessor paymentProcessor(List<PaymentMethod> paymentMethods, PaymentHandler validationChain) {
        return new PaymentProcessor(paymentMethods, validationChain);
    }
}
