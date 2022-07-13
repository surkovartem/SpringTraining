package SpringTrainingCode.service;

import SpringTrainingCode.models.AbstractAccount;
import SpringTrainingCode.models.Client;
import SpringTrainingCode.service.storage.ClientRepository;

import java.util.List;

public interface Banking {
    Client addClient(Client client);

    Client getClient(String name);

    List<Client> getClients();

    void removeClient(Client client);

    AbstractAccount createAccount(Client client, Class type);

    void updateAccount(Client c, AbstractAccount account);

    AbstractAccount getAccount(Client client, Class type);

    List<AbstractAccount> getAllAccounts();

    List<AbstractAccount> getAllAccounts(Client client);

    void transferMoney(Client from, Client to, double amount);

    void setRepository(ClientRepository storage);
}
