# Basit Odeme Ekrani (Java)

Bu proje, bir odeme ekraninda mevcut sisteme yeni odeme yontemi eklemeyi **OOP + abstraction + SOLID** ile basitce gosterir.

## Yaklasim

- `PaymentMethod` bir interface'tir.
- Mevcut yontem: `CreditCardPayment`
- Yeni yontem: `PayPalPayment`
- `PaymentProcessor` sadece orkestrasyon yapar (SRP).
- Yeni yontem eklemek icin mevcut siniflari degistirmeden yeni bir sinif yazmak yeterlidir (OCP).
- Hata yonetimi custom exception siniflari ile yapilir.

## Sinif Diyagrami (Mermaid)

```mermaid
classDiagram
    class PaymentMethod {
      <<interface>>
      +String getMethodKey()
      +PaymentResult pay(PaymentRequest request)
    }

    class CreditCardPayment
    class PayPalPayment
    class PaymentProcessor {
      -Map~String, PaymentMethod~ methodsByKey
      +PaymentResult process(String methodKey, PaymentRequest request)
    }

    class PaymentRequest {
      -double amount
      -String currency
      -String payerInfo
    }

    class PaymentResult {
      -PaymentStatus status
      -String message
    }

    class PaymentException
    class ValidationException
    class ProcessingException

    PaymentMethod <|.. CreditCardPayment
    PaymentMethod <|.. PayPalPayment
    PaymentProcessor --> PaymentMethod
    PaymentProcessor --> PaymentRequest
    PaymentProcessor --> PaymentResult
    PaymentException <|-- ValidationException
    PaymentException <|-- ProcessingException
```

## Akis (Mermaid)

```mermaid
flowchart TD
    A[UI input alir] --> B[PaymentProcessor.process]
    B --> C{Yontem var mi?}
    C -- Hayir --> D[ProcessingException]
    C -- Evet --> E[PaymentMethod.pay]
    E --> F{Girdi dogru mu?}
    F -- Hayir --> G[ValidationException]
    F -- Evet --> H[PaymentResult SUCCESS]
```

## Calistirma

```powershell
javac -d out (Get-ChildItem -Path .\src -Filter *.java -Recurse | ForEach-Object FullName)
java -cp out payment.Main
```

## Ornek test girdileri

### Basarili kredi karti
- yontem: `creditcard`
- tutar: `100`
- para birimi: `TRY`
- odeme bilgisi: `1234567812345678`

### Basarili PayPal
- yontem: `paypal`
- tutar: `250`
- para birimi: `TRY`
- odeme bilgisi: `user@example.com`

