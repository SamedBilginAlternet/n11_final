import payment.methods.CreditCardPayment;
import payment.methods.PayPalPayment;
import payment.service.PaymentProcessor;
import payment.ui.ConsolePaymentUI;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        PaymentProcessor processor = new PaymentProcessor(
                Arrays.asList(
                        new CreditCardPayment(),
                        new PayPalPayment()
                )
        );

        ConsolePaymentUI ui = new ConsolePaymentUI(processor);
        ui.start();
    }
}