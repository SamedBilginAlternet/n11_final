package payment.factory;

import org.springframework.stereotype.Component;
import payment.core.PaymentMethod;
import payment.repository.PaymentMethodConfigRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class PaymentMethodFactory {

    private final PaymentMethodConfigRepository configRepository;

    public PaymentMethodFactory(PaymentMethodConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    public List<PaymentMethod> createAll() {
        List<PaymentMethod> methods = new ArrayList<>();

        for (var config : configRepository.findAll()) {
            String className = config.getClassName().trim();
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
}
