package xyz.smsverification.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class OrderStatusChange {
    private String oldStatus;
    private String newStatus;
}
