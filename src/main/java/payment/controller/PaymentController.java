package payment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Payment", description = "Odeme islemleri")
public class PaymentController {

    private final PaymentProcessor processor;

    public PaymentController(PaymentProcessor processor) {
        this.processor = processor;
    }

    @GetMapping("/methods")
    @Operation(summary = "Odeme yontemlerini listele", description = "DB'den yuklenen aktif odeme yontemlerini doner")
    public List<String> getMethods() {
        return processor.getAvailableMethodKeys();
    }

    @GetMapping("/currencies")
    @Operation(summary = "Para birimlerini listele", description = "DB'den yuklenen desteklenen para birimlerini doner")
    public List<String> getCurrencies() {
        return processor.getAvailableCurrencies();
    }

    @PostMapping("/process")
    @Operation(summary = "Odeme gerceklestir", description = "Belirtilen yontem ve tutarla odeme islemi yapar")
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
