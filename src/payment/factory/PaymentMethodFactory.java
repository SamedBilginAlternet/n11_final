package payment.factory;

import payment.core.PaymentMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Reflection-based factory that dynamically instantiates PaymentMethod implementations.
 * New payment methods can be registered by adding their fully qualified class name —
 * no code change elsewhere is required (OCP).
 */
public class PaymentMethodFactory {

    private static final List<String> REGISTERED_CLASS_NAMES = List.of(
            "payment.methods.CreditCardPayment",
            "payment.methods.PayPalPayment",
            "payment.methods.BankTransferPayment"
    );

    /**
     * Loads and instantiates all registered PaymentMethod classes using reflection.
     *
     * @return list of ready-to-use PaymentMethod instances
     */
    public static List<PaymentMethod> createAll() {
        List<PaymentMethod> methods = new ArrayList<>();

        for (String className : REGISTERED_CLASS_NAMES) {
            try {
                Class<?> clazz = Class.forName(className);
                Object instance = clazz.getDeclaredConstructor().newInstance();

                if (instance instanceof PaymentMethod paymentMethod) {
                    methods.add(paymentMethod);
                    System.out.println("[Factory] Loaded: " + clazz.getSimpleName());
                } else {
                    System.err.println("[Factory] " + className + " does not implement PaymentMethod — skipped.");
                }
            } catch (ClassNotFoundException e) {
                System.err.println("[Factory] Class not found: " + className);
            } catch (Exception e) {
                System.err.println("[Factory] Could not instantiate " + className + ": " + e.getMessage());
            }
        }

        return methods;
    }

    /**
     * Dynamically creates a single PaymentMethod instance by its fully qualified class name.
     *
     * @param className fully qualified class name (e.g. "payment.methods.CreditCardPayment")
     * @return PaymentMethod instance, or null if creation fails
     */
    public static PaymentMethod create(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Object instance = clazz.getDeclaredConstructor().newInstance();
            if (instance instanceof PaymentMethod paymentMethod) {
                return paymentMethod;
            }
        } catch (Exception e) {
            System.err.println("[Factory] Could not create " + className + ": " + e.getMessage());
        }
        return null;
    }
}
