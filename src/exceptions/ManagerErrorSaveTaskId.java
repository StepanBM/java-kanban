package exceptions;

public class ManagerErrorSaveTaskId extends RuntimeException {
    public ManagerErrorSaveTaskId(String message) {
        super(message);
    }
}
