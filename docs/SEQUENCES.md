# Order sequences

```mermaid
sequenceDiagram
    participant C as Client
    participant F as OrderFacade
    participant I as InventoryService
    participant P as PaymentProcessor
    participant S as ShippingService
    participant N as NotificationService
    C->>F: placeOrder(productId, quantity, customerId)
    Note over F: Validate input, find price, multiply safely
    F->>I: reserve(productId, quantity)
    I-->>F: reservationId or null
    alt insufficient stock
        F-->>C: OUT_OF_STOCK
    else stock reserved
        F->>P: charge(customerId, amount)
        P-->>F: paymentId or null
        alt payment declined
            F->>I: release(reservationId)
            F-->>C: PAYMENT_DECLINED
        else payment accepted
            F->>S: createShipment(customerId, productId, quantity)
            alt shipping unavailable
                S-->>F: IllegalStateException
                F->>P: refund(paymentId)
                F->>I: release(reservationId)
                F-->>C: SHIPPING_FAILED
            else shipment recorded
                S-->>F: shipmentId
                F->>I: complete(reservationId)
                F->>N: sendConfirmation(customerId, shipmentId)
                F-->>C: SUCCESS with shipmentId
            end
        end
    end
```

Invalid inputs throw `IllegalArgumentException`; unknown products return `UNKNOWN_PRODUCT`.
Price multiplication overflow throws `ArithmeticException`. All these paths occur before
reservation, so they change no subsystem state. The in-memory services run sequentially;
this is compensation in a local simulation, not a distributed database transaction.
