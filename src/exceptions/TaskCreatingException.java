package exceptions;

public class TaskCreatingException extends RuntimeException {
    String massage;

    public TaskCreatingException(String massage) {
        this.massage = massage;
    }
}
