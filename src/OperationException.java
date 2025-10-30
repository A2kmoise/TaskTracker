public class OperationException extends Exception {
    public OperationException(String message) {
        super(message);
        System.out.println("Oops!");
    }
}
