package SpringTrainingCode.models;


public class CheckingAccount extends AbstractAccount{
    private double overdraft = 0;

    public CheckingAccount() {}

    public CheckingAccount(double overdraft) {
        setOverdraft(overdraft);
    }

    public void setOverdraft(double amount) {
        if (overdraft < 0) {
            return;
        }
        overdraft = amount;
    }

    public double getOverdraft() {
        return overdraft;
    }

    @Override
    public void withdraw(double amount){
        setBalance(getBalance() - amount);
    }
}
