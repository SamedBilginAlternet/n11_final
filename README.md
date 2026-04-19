# Basit Odeme Ekrani — Spring Boot + Docker

Bu proje, bir odeme ekraninda **OOP + SOLID + Reflection + Chain of Responsibility** prensiplerini gosterir.
Spring Boot REST API olarak calisir, Docker ile containerize edilmistir.

---

## Ozellikler

- `PaymentMethod` arayuzu ile **Strategy Pattern**
- `PaymentMethodFactory` ile **Reflection tabanli dinamik nesne uretimi**
- `PaymentHandler` zinciri ile **Chain of Responsibility** dogrulama katmani
- **Spring Boot REST API** — `/api/payment/methods` ve `/api/payment/process`
- **Web formu** — `localhost:9090` adresinde acilan statik HTML form
- Combo kutusu, reflection ile yuklenen yontemleri otomatik alir (UI sync)
- Custom exception hiyerarsisi ile guvenli hata yonetimi

---

## Mimari

### Reflection Factory

`PaymentMethodFactory`, odeme yontemlerini `new` ile olusturmak yerine **Java Reflection** kullanarak runtime'da yukler.
Yuklenecek siniflar `application.properties`'deki `payment.methods` listesinden okunur — Java kodu degismez (OCP).

### Chain of Responsibility

`PaymentProcessor.process()`, odeme yontemine gecmeden once inject edilen dogrulama zincirini kosutur:

```
AmountHandler → CurrencyHandler → FraudHandler → PaymentMethod.pay()
```

| Handler | Kontrol |
|---|---|
| `AmountHandler` | Tutar > 0 olmali |
| `CurrencyHandler` | Para birimi TRY / USD / EUR olmali |
| `FraudHandler` | Tutar 50.000 ustu islemleri engeller |

Zincir `PaymentConfig`'de `@Bean` olarak kurulur ve `PaymentProcessor`'a inject edilir (DIP).

---

## Paket Yapisi

```
src/main/java/payment/
├── Main.java                        # @SpringBootApplication
├── config/
│   └── PaymentConfig.java           # @Configuration — method bean + chain bean + processor bean
├── controller/
│   └── PaymentController.java       # @RestController — /api/payment/*
├── dto/
│   ├── PaymentRequestDto.java       # API istek modeli (record)
│   └── PaymentResponseDto.java      # API yanit modeli (record)
├── core/
│   └── PaymentMethod.java           # Strateji arayuzu
├── factory/
│   └── PaymentMethodFactory.java    # @Component — reflection ile dinamik yukleme
├── methods/
│   ├── CreditCardPayment.java
│   ├── PayPalPayment.java
│   └── BankTransferPayment.java
├── model/
│   ├── PaymentRequest.java          # Immutable istek DTO (amount, currency)
│   ├── PaymentResult.java
│   └── PaymentStatus.java           # SUCCESS / FAILED
├── service/
│   └── PaymentProcessor.java        # Zinciri kosturur, routing yapar
├── validation/
│   ├── PaymentHandler.java          # Abstract CoR base
│   ├── AmountHandler.java
│   ├── CurrencyHandler.java
│   └── FraudHandler.java
└── exception/
    ├── PaymentException.java
    ├── ValidationException.java
    └── ProcessingException.java

src/main/resources/
├── application.properties           # Yuklu yontem listesi burada
└── static/
    └── index.html                   # Web form UI
```

---

## API

| Method | Endpoint | Aciklama |
|---|---|---|
| `GET` | `/api/payment/methods` | Yuklu odeme yontemlerini listeler |
| `POST` | `/api/payment/process` | Odeme islemi gerceklestirir |

### GET /api/payment/methods

**Response:**
```json
["creditcard", "paypal", "banktransfer"]
```

### POST /api/payment/process

**Request:**
```json
{
  "method": "creditcard",
  "amount": 100.0,
  "currency": "TRY"
}
```

**Response (basarili):**
```json
{
  "status": "SUCCESS",
  "message": "Kredi karti ile odeme basarili."
}
```

**Response (hata):**
```json
{
  "status": "FAILED",
  "message": "Desteklenmeyen para birimi: GBP"
}
```

---

## Sinif Diyagrami (Mermaid)

```mermaid
classDiagram
    class PaymentMethod {
      <<interface>>
      +String getMethodKey()
      +PaymentResult pay(PaymentRequest request)
    }

    class PaymentMethodFactory {
      <<@Component>>
      -List~String~ registeredClassNames
      +List~PaymentMethod~ createAll()
    }

    class PaymentConfig {
      <<@Configuration>>
      +List~PaymentMethod~ paymentMethods()
      +PaymentHandler validationChain()
      +PaymentProcessor paymentProcessor()
    }

    class PaymentController {
      <<@RestController>>
      +List~String~ getMethods()
      +ResponseEntity process(dto)
    }

    class PaymentProcessor {
      -Map~String,PaymentMethod~ methodsByKey
      -PaymentHandler validationChain
      +List~String~ getAvailableMethodKeys()
      +PaymentResult process(methodKey, request)
    }

    class PaymentHandler {
      <<abstract>>
      +void handle(PaymentRequest)
    }

    class CreditCardPayment
    class PayPalPayment
    class BankTransferPayment
    class AmountHandler
    class CurrencyHandler
    class FraudHandler

    PaymentMethod <|.. CreditCardPayment
    PaymentMethod <|.. PayPalPayment
    PaymentMethod <|.. BankTransferPayment
    PaymentMethodFactory ..> PaymentMethod : <<reflection>>
    PaymentConfig --> PaymentMethodFactory
    PaymentConfig --> PaymentProcessor
    PaymentConfig --> PaymentHandler
    PaymentController --> PaymentProcessor
    PaymentProcessor --> PaymentHandler
    PaymentProcessor --> PaymentMethod
    PaymentHandler <|-- AmountHandler
    PaymentHandler <|-- CurrencyHandler
    PaymentHandler <|-- FraudHandler
```

---

## Akis (Mermaid)

```mermaid
flowchart TD
    A[index.html] -->|GET /api/payment/methods| B[PaymentController]
    A -->|POST /api/payment/process| B
    B --> C[PaymentProcessor.process]
    C --> D[AmountHandler]
    D --> E[CurrencyHandler]
    E --> F[FraudHandler]
    D -- hata --> G[400 FAILED]
    E -- hata --> G
    F -- hata --> G
    F --> H{Yontem var mi?}
    H -- Hayir --> G
    H -- Evet --> I[PaymentMethod.pay]
    I --> J[200 SUCCESS]
```

---

## Calistirma

### Docker ile (onerilen)

```bash
docker-compose up --build
```

Uygulama `http://localhost:9090` adresinde calisir.

### Maven ile (yerel)

```bash
mvn spring-boot:run
```

Uygulama `http://localhost:8080` adresinde calisir.

---

## Gelistirme Kilavuzu

### Yeni Odeme Yontemi Eklemek

**1. Sinif yaz** — `payment/methods/` altinda `PaymentMethod` implement et:

```java
package payment.methods;

public class ApplePayPayment implements PaymentMethod {
    @Override
    public String getMethodKey() { return "applepay"; }

    @Override
    public PaymentResult pay(PaymentRequest request) throws PaymentException {
        return new PaymentResult(PaymentStatus.SUCCESS, "Apple Pay ile odeme basarili.");
    }
}
```

**2. `application.properties`'e ekle** — baska hicbir dosyaya dokunma:

```properties
payment.methods=...,payment.methods.ApplePayPayment
```

Web formu combo kutusu otomatik guncellenir.

---

### Yeni Dogrulama Kurali Eklemek

**1. Handler yaz** — `payment/validation/` altinda `PaymentHandler` extend et:

```java
package payment.validation;

public class MaxAmountHandler extends PaymentHandler {
    @Override
    public void handle(PaymentRequest request) throws PaymentException {
        if (request.getAmount() > 10_000)
            throw new ValidationException("Tek islemde maksimum 10.000 girilebilir.");
        super.handle(request);
    }
}
```

**2. `PaymentConfig`'de zincire ekle** — sadece bu metodu guncelle:

```java
@Bean
public PaymentHandler validationChain() {
    PaymentHandler chain = new AmountHandler();
    chain.setNext(new CurrencyHandler())
         .setNext(new FraudHandler())
         .setNext(new MaxAmountHandler());  // ← ekle
    return chain;
}
```

`PaymentProcessor` degismez.

---

### Yeni Endpoint Eklemek

**1. Controller'a metod ekle** — `PaymentController.java`:

```java
@GetMapping("/status")
public ResponseEntity<String> status() {
    return ResponseEntity.ok("Sistem aktif");
}
```

**Yeni bir kaynak icin ayri controller yaz** (SRP):

```java
package payment.controller;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @GetMapping("/summary")
    public ResponseEntity<String> summary() {
        return ResponseEntity.ok("Rapor burada");
    }
}
```

---

### Yeni DTO Eklemek

`payment/dto/` altina yeni bir `record` yaz:

```java
package payment.dto;

public record RefundRequestDto(String transactionId, double amount) {}
```

Controller'da import edip kullan — baska degisiklik gerekmez.

---

### Yeni Exception Eklemek

`payment/exception/` altinda `PaymentException` extend et:

```java
package payment.exception;

public class InsufficientFundsException extends PaymentException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
```

Handler veya payment method icinde `throw new InsufficientFundsException(...)` ile kullan.
`PaymentController`'daki `catch (PaymentException e)` blogu otomatik yakalar.

---

## Ornek Test Girdileri

| Yontem | Tutar | Para Birimi | Beklenen |
|---|---|---|---|
| `creditcard` | `100` | `TRY` | SUCCESS |
| `paypal` | `250` | `USD` | SUCCESS |
| `banktransfer` | `500` | `TRY` | SUCCESS |
| `banktransfer` | `500` | `USD` | FAILED — yalnizca TRY |
| `creditcard` | `99999` | `TRY` | FAILED — fraud limiti |
| `paypal` | `100` | `GBP` | FAILED — desteklenmeyen para birimi |
| `creditcard` | `-50` | `TRY` | FAILED — gecersiz tutar |
