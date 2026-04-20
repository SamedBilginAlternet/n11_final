package payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import payment.dto.PaymentRequestDto;
import payment.dto.PaymentResponseDto;
import payment.exception.PaymentException;
import payment.model.PaymentRequest;
import payment.model.PaymentResult;
import payment.service.PaymentProcessor;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentProcessor processor;

    public PaymentController(PaymentProcessor processor) {
        this.processor = processor;
    }

    @GetMapping("/methods")
    public List<String> getMethods() {
        return processor.getAvailableMethodKeys();
    }

    @GetMapping("/currencies")
    public List<String> getCurrencies() {
        return processor.getAvailableCurrencies();
    }

    @PostMapping("/process")
    public ResponseEntity<PaymentResponseDto> process(@RequestBody PaymentRequestDto dto) {
        try {
            PaymentRequest request = new PaymentRequest(dto.amount(), dto.currency());
            PaymentResult result = processor.process(dto.method(), request);
            return ResponseEntity.ok(new PaymentResponseDto(result.getStatus().name(), result.getMessage()));
        } catch (PaymentException e) {
            return ResponseEntity.badRequest().body(new PaymentResponseDto("FAILED", e.getMessage()));
        }
    }
}
