package xyz.smsverification.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class AccountBalanceChange {
    private float oldBalance;
    private float newBalance;
}
