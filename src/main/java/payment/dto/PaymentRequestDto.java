package payment.dto;

public record PaymentRequestDto(String method, double amount, String currency) {}
