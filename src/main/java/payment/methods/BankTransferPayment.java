package payment.methods;

import payment.core.PaymentMethod;
import payment.exception.PaymentException;
import payment.exception.ValidationException;
import payment.model.PaymentRequest;
import payment.model.PaymentResult;
import payment.model.PaymentStatus;

public class BankTransferPayment implements PaymentMethod {
    @Override
    public String getMethodKey() {
        return "banktransfer";
    }

    @Override
    public PaymentResult pay(PaymentRequest request) throws PaymentException {
        if (!request.getCurrency().equalsIgnoreCase("TRY")) {
            throw new ValidationException("Havale/EFT yalnizca TRY destekler.");
        }
        return new PaymentResult(PaymentStatus.SUCCESS, "Havale/EFT ile odeme basarili.");
    }
}
