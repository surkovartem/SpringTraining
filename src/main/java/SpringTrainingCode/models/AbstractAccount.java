package SpringTrainingCode.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AbstractAccount {
    long id;
    double balance;

    public void deposit(double amount) {
        if (amount < 0) {
            return;
        }
        balance += amount;
    }

    public void withdraw(double amount) {}
}