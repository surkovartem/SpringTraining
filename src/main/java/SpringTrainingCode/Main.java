package SpringTrainingCode;

import SpringTrainingCode.exceptions.ActiveAccountNotSet;
import SpringTrainingCode.models.AbstractAccount;
import SpringTrainingCode.models.CheckingAccount;
import SpringTrainingCode.models.Client;
import SpringTrainingCode.models.SavingAccount;
import SpringTrainingCode.service.BankReportService;
import SpringTrainingCode.service.BankReportServiceImpl;
import SpringTrainingCode.service.Banking;
import SpringTrainingCode.service.storage.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import static java.lang.System.*;

@Slf4j
public class Main {
    private static final String[] CLIENT_NAMES =
            {"Артём Сурков", "Илья Бикмеев", "Марина Иванова"};

    public static void main(String[] args) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        Banking banking = initialize(context);
        workWithExistingClients(banking);
        bankingServiceDemo(banking);
    }

    public static void bankReportsDemo(ClientRepository repository) {
        log.info("Using BankReportService");
        BankReportService reportService = new BankReportServiceImpl();
        reportService.setRepository(repository);

        out.println("Клиентов всего: " + reportService.getNumberOfBankClients());
        out.println("Аккаунтов всего: " + reportService.getAccountsNumber());
        out.println("Сумма банковсвого кредита: " + reportService.getBankCreditSum());
    }

    public static void bankingServiceDemo(Banking banking) {
        out.println("\n==================Work banking ServiceDemo==================" );

        Client marina = new Client(CLIENT_NAMES[2], Client.Gender.FEMALE);
        marina = banking.addClient(marina);

        AbstractAccount saving = banking.createAccount(marina, SavingAccount.class);
        banking.updateAccount(marina, saving);
        saving.deposit(1_000);

        AbstractAccount checking = banking.createAccount(marina, CheckingAccount.class);
        checking.deposit(3_000);
        banking.updateAccount(marina, checking);

        banking.getAllAccounts(marina).stream().forEach(out::println);
    }

    public static void workWithExistingClients(Banking banking) {
        out.println("\n==================Work with existing clients==================" );

        Client artem = banking.getClient(CLIENT_NAMES[0]);
        try {
            artem.deposit(5_000);
        } catch (ActiveAccountNotSet e) {
            log.error(e.getMessage());
            artem.setDefaultActiveAccountIfNotSet();
            artem.deposit(5_000);
        }
        out.println(artem);
        log.debug("Artem's balance: " + artem.getBalance());

        Client ilya = banking.getClient(CLIENT_NAMES[1]);
        out.print("\n");
        ilya.setDefaultActiveAccountIfNotSet();
        out.println(ilya);
        ilya.withdraw(1_500);
        log.debug("Ilya's balance: " + ilya.getBalance());

        banking.transferMoney(artem, ilya, 1_000);

        out.print("\n");
        log.info("Денежный перевод: " + artem.getName() + " to " + ilya.getName() + ". в размере " + 1_000);
        log.debug("Artem's balance: " + artem.getBalance());
        log.debug("Ivan's balance: " + ilya.getBalance());
        out.print("\n");
    }

    public static Banking initialize(ApplicationContext context) {

        Banking banking = (Banking) context.getBean("banking");

        Client client_1 = new Client(CLIENT_NAMES[0], Client.Gender.MALE);
        Client client_2 = new Client(CLIENT_NAMES[1], Client.Gender.MALE);

        //Клиент 1
        AbstractAccount savingAccountC1 = new SavingAccount(1_000);
        AbstractAccount checkingAccountC1 = new CheckingAccount(1_000);
        client_1.addAccount(savingAccountC1);
        client_1.addAccount(checkingAccountC1);

        //Клиент 2
        AbstractAccount checkingAccountC2 = new CheckingAccount(1500);
        client_2.addAccount(checkingAccountC2);

        banking.addClient(client_1);
        banking.addClient(client_2);

        return banking;
    }
}