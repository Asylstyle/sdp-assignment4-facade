# Class diagram

This diagram shows the implemented public API. `Checks` is a package-private validation helper.
`Reservation` is private inventory bookkeeping. Prices are an immutable map owned by the facade.

```mermaid
classDiagram
    Client ..> OrderFacade : calls
    Client ..> OrderResult : reads
    OrderFacade --> InventoryService
    OrderFacade --> PaymentProcessor
    OrderFacade --> ShippingService
    OrderFacade --> NotificationService
    OrderFacade ..> OrderResult : returns
    class Client {
        +main(String[] args) void
    }
    class OrderFacade {
        -Map pricesInCents
        +OrderFacade()
        +OrderFacade(InventoryService, PaymentProcessor, ShippingService, NotificationService, Map)
        +placeOrder(String productId, int quantity, String customerId) OrderResult
    }
    class InventoryService {
        +addStock(String productId, int quantity) void
        +available(String productId) int
        +reserve(String productId, int quantity) String
        +release(String reservationId) void
        +complete(String reservationId) void
    }
    class PaymentProcessor {
        +declineCustomer(String customerId) void
        +charge(String customerId, int amountInCents) String
        +refund(String paymentId) void
        +netChargedInCents() int
    }
    class ShippingService {
        +ShippingService()
        +ShippingService(boolean available)
        +createShipment(String customerId, String productId, int quantity) String
        +shipments() List~Shipment~
    }
    class NotificationService {
        +sendConfirmation(String customerId, String shipmentId) void
        +confirmations() List~Confirmation~
    }
    class OrderResult {
        <<record>>
        +status() Status
        +message() String
        +shipmentId() String
        +successful() boolean
    }
    class Status {
        <<enumeration>>
        SUCCESS
        UNKNOWN_PRODUCT
        OUT_OF_STOCK
        PAYMENT_DECLINED
        SHIPPING_FAILED
    }
    OrderResult --> Status
    ShippingService *-- Shipment
    NotificationService *-- Confirmation
    class Shipment {
        <<record>>
        +id() String
        +customerId() String
        +productId() String
        +quantity() int
    }
    class Confirmation {
        <<record>>
        +customerId() String
        +shipmentId() String
    }
```

Arrow direction matters: no subsystem depends on `OrderFacade`. Advanced clients can
construct, call, and inspect the subsystems directly; the tests demonstrate that use.
