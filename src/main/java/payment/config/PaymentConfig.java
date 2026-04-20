package payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import payment.factory.PaymentMethodFactory;
import payment.repository.CurrencyRepository;
import payment.repository.PaymentRepository;
import payment.service.PaymentProcessor;
import payment.validation.AmountHandler;
import payment.validation.CurrencyHandler;
import payment.validation.FraudHandler;
import payment.validation.PaymentHandler;

@Configuration
public class PaymentConfig {

    @Bean
    public PaymentHandler validationChain(CurrencyHandler currencyHandler) {
        PaymentHandler chain = new AmountHandler();
        chain.setNext(currencyHandler)
             .setNext(new FraudHandler());
        return chain;
    }

    @Bean
    public PaymentProcessor paymentProcessor(PaymentMethodFactory factory, PaymentHandler validationChain, PaymentRepository paymentRepository, CurrencyRepository currencyRepository) {
        return new PaymentProcessor(factory, validationChain, paymentRepository, currencyRepository);
    }
}
