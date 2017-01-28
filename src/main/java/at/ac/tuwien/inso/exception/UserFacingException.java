package at.ac.tuwien.inso.exception;


public class UserFacingException extends RuntimeException {

    public UserFacingException() {
        super();
    }

    public UserFacingException(String s) {
        super(s);
    }
}
