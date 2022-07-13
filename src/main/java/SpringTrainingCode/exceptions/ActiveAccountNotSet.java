package SpringTrainingCode.exceptions;

public class ActiveAccountNotSet extends RuntimeException {
    private String clientName;

    public ActiveAccountNotSet(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public String getMessage() {
        return "Активная учетная запись не настроена для " + clientName;
    }
}
