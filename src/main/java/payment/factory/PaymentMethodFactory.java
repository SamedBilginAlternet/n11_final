package payment.factory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import payment.core.PaymentMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Reflection-based factory that dynamically instantiates PaymentMethod implementations.
 * Class names are read from application.properties (payment.methods) — adding a new
 * payment method requires no code change, only a config update (OCP).
 */
@Component
public class PaymentMethodFactory {

    @Value("${payment.methods}")
    private List<String> registeredClassNames;

    public List<PaymentMethod> createAll() {
        List<PaymentMethod> methods = new ArrayList<>();

        for (String className : registeredClassNames) {
            try {
                Class<?> clazz = Class.forName(className.trim());
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
}
