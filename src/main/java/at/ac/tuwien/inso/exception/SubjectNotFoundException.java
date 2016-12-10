package at.ac.tuwien.inso.exception;

public class SubjectNotFoundException extends BusinessObjectNotFoundException {

    public SubjectNotFoundException() {
        super();
    }

    public SubjectNotFoundException(final String message) {
        super(message);
    }
}
