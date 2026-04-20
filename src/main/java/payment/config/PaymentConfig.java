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

import payment.repository.PaymentRepository;

import java.util.List;

@Configuration
public class PaymentConfig {

    @Bean
    public List<PaymentMethod> paymentMethods(PaymentMethodFactory factory) {
        return factory.createAll();
    }

    @Bean
    public PaymentHandler validationChain(CurrencyHandler currencyHandler) {
        PaymentHandler chain = new AmountHandler();
        chain.setNext(currencyHandler)
             .setNext(new FraudHandler());
        return chain;
    }

    @Bean
    public PaymentProcessor paymentProcessor(List<PaymentMethod> paymentMethods, PaymentHandler validationChain, PaymentRepository paymentRepository) {
        return new PaymentProcessor(paymentMethods, validationChain, paymentRepository);
    }
}
