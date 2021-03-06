package SpringTrainingCode.service.storage;

import SpringTrainingCode.models.Client;
import java.util.List;

public interface ClientRepository {
    Client add(Client client);

    Client get(long id);

    Client getBy(String name);

    List<Client> getAll();

    Client update(Client o);

    boolean remove(long id);
}
