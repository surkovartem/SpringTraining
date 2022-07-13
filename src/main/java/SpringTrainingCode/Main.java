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

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static java.lang.System.*;

@Slf4j
public class Main {
    private static final String[] CLIENT_NAMES =
            {"Артём Сурков", "Иван Иванов", "Сергей Сергеев"};

    @SneakyThrows
    public static void main(String[] args) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        log.debug("Context: " + context);

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
        out.print("\n");
        log.info("[Initialization using Banking implementation]");

        Client sergey = new Client(CLIENT_NAMES[2], Client.Gender.FEMALE);
        sergey = banking.addClient(sergey);

        AbstractAccount saving = banking.createAccount(sergey, SavingAccount.class);
        saving.deposit(1_000);
        banking.updateAccount(sergey, saving);

        AbstractAccount checking = banking.createAccount(sergey, CheckingAccount.class);
        checking.deposit(3_000);

        banking.updateAccount(sergey, checking);
        banking.getAllAccounts(sergey).stream().forEach(out::println);
        out.print("\n");
    }

    public static void workWithExistingClients(Banking banking) {
        out.print("\n");
        log.info("[Work with existing clients]");

        Client artem = banking.getClient(CLIENT_NAMES[0]);
        try {
            artem.deposit(5_000);
        } catch (ActiveAccountNotSet e) {
            log.error(e.getMessage());
            artem.setDefaultActiveAccountIfNotSet();
            artem.deposit(5_000);
        }
        out.println(artem);

        Client ivan = banking.getClient(CLIENT_NAMES[1]);
        ivan.setDefaultActiveAccountIfNotSet();
        ivan.withdraw(1_500);
        double balance = ivan.getBalance();
        out.println(ivan.getName() + ", текущий баланс: " + balance);

        banking.transferMoney(artem, ivan, 1_000);
        banking.getClients().forEach(out::println);
        out.print("\n");
    }

    public static Banking initialize(ApplicationContext context) {

        Banking banking = (Banking) context.getBean("banking");

        Client client_1 = new Client(CLIENT_NAMES[0], Client.Gender.MALE);
        Client client_2 = new Client(CLIENT_NAMES[1], Client.Gender.MALE);

        //Клиент 1
        AbstractAccount savingAccountC1 = new SavingAccount(1000);
        AbstractAccount checkingAccountC1 = new CheckingAccount(1000);
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
