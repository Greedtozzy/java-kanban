package exceptions;

public class TaskCreatingException extends Exception {
    String massage;

    public TaskCreatingException(String massage) {
        this.massage = massage;
    }
}
