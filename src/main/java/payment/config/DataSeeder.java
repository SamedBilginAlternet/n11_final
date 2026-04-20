package payment.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import payment.entity.Currency;
import payment.entity.PaymentMethodConfig;
import payment.repository.CurrencyRepository;
import payment.repository.PaymentMethodConfigRepository;

@Component
public class DataSeeder implements ApplicationRunner {

    private final CurrencyRepository currencyRepository;
    private final PaymentMethodConfigRepository paymentMethodConfigRepository;

    public DataSeeder(CurrencyRepository currencyRepository, PaymentMethodConfigRepository paymentMethodConfigRepository) {
        this.currencyRepository = currencyRepository;
        this.paymentMethodConfigRepository = paymentMethodConfigRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        seedCurrencies();
        seedPaymentMethods();
    }

    private void seedCurrencies() {
        for (String code : new String[]{"TRY", "USD", "EUR"}) {
            if (!currencyRepository.existsByCode(code)) {
                currencyRepository.save(new Currency(code));
                System.out.println("[Seeder] Currency added: " + code);
            }
        }
    }

    private void seedPaymentMethods() {
        String[][] methods = {
            {"creditcard", "payment.methods.CreditCardPayment"},
            {"paypal",     "payment.methods.PayPalPayment"},
            {"banktransfer", "payment.methods.BankTransferPayment"}
        };
        for (String[] m : methods) {
            if (!paymentMethodConfigRepository.existsByMethodKey(m[0])) {
                paymentMethodConfigRepository.save(new PaymentMethodConfig(m[0], m[1]));
                System.out.println("[Seeder] Payment method added: " + m[0]);
            }
        }
    }
}
