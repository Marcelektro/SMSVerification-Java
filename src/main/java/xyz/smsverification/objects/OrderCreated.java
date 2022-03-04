package xyz.smsverification.objects;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OrderCreated {
    private AccountBalanceChange accountBalanceChange;

    private final String number;
    private final String id;

    public OrderCreated(AccountBalanceChange accountBalanceChange, String number, String id) {
        this.accountBalanceChange = accountBalanceChange;
        this.number = number;
        this.id = id;
    }

    public OrderCreated(String number, String id) {
        this.number = number;
        this.id = id;
    }
}
