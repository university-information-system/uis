package at.ac.tuwien.inso.exception;

public class ActionNotAllowedException extends RuntimeException {

    public ActionNotAllowedException() {
        super();
    }

    public ActionNotAllowedException(String s) {
        super(s);
    }
}
