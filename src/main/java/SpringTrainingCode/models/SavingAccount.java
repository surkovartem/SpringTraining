package SpringTrainingCode.models;

//Сберегательный счет
public class SavingAccount extends AbstractAccount{

    public SavingAccount(double initialBalance) {
        if (initialBalance >= 0) {
            setBalance(initialBalance);
        }
    }

    @Override
    public void withdraw(double amount) {
        setBalance(getBalance() - amount);
    }
}
