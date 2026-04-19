package payment;

import payment.factory.PaymentMethodFactory;
import payment.service.PaymentProcessor;
import payment.ui.ConsolePaymentUI;
import payment.ui.SwingPaymentForm;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // PaymentMethod instances are created dynamically via reflection — no hardcoded constructors.
        PaymentProcessor processor = new PaymentProcessor(PaymentMethodFactory.createAll());

        boolean useSwing = args.length == 0 || !args[0].equalsIgnoreCase("--console");

        if (useSwing) {
            new SwingPaymentForm(processor).start();
        } else {
            new ConsolePaymentUI(processor).start();
        }
    }
}
