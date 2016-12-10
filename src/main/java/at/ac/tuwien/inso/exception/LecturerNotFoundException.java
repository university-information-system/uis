package at.ac.tuwien.inso.exception;


public class LecturerNotFoundException extends BusinessObjectNotFoundException {

    public LecturerNotFoundException() {
    }

    public LecturerNotFoundException(String s) {
        super(s);
    }
}
