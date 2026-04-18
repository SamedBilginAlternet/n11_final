package payment.ui;

import payment.exception.PaymentException;
import payment.model.PaymentRequest;
import payment.model.PaymentResult;
import payment.service.PaymentProcessor;

import java.util.Scanner;

public class ConsolePaymentUI {
    private final PaymentProcessor paymentProcessor;

    public ConsolePaymentUI(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("=== Basit Odeme Ekrani ===");
            System.out.println("Yontemler: creditcard, paypal");

            System.out.print("Odeme yontemi: ");
            String method = scanner.nextLine();

            System.out.print("Tutar: ");
            double amount = Double.parseDouble(scanner.nextLine());

            System.out.print("Para birimi (orn. TRY): ");
            String currency = scanner.nextLine();

            System.out.print("Odeme bilgisi (kart no veya email): ");
            String payerInfo = scanner.nextLine();

            PaymentRequest request = new PaymentRequest(amount, currency, payerInfo);
            PaymentResult result = paymentProcessor.process(method, request);

            System.out.println("Durum: " + result.getStatus());
            System.out.println("Mesaj: " + result.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Hata: Tutar sayisal olmali.");
        } catch (PaymentException e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }
}

