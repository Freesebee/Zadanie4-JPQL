package zad4.exceptions;


public class DataAccessException extends RuntimeException {

    public DataAccessException() {
    }

    public DataAccessException(Throwable cause) {
        super (cause);
    }

}