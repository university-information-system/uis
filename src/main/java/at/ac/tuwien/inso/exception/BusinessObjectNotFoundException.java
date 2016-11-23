package at.ac.tuwien.inso.exception;

public class BusinessObjectNotFoundException extends RuntimeException {

    public BusinessObjectNotFoundException() {
        super();
    }

    public BusinessObjectNotFoundException(String s) {
        super(s);
    }
}
