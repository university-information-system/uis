package at.ac.tuwien.inso.controller.forms;

import at.ac.tuwien.inso.entity.*;
import org.hibernate.validator.constraints.*;

@ScriptAssert(lang = "javascript", script = "_this.password === _this.repeatPassword", message = "repeatPassword")
public class AccountActivationForm {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private String repeatPassword;

    protected AccountActivationForm() {

    }

    public AccountActivationForm(String username, String password, String repeatPassword) {
        this.username = username;
        this.password = password;
        this.repeatPassword = repeatPassword;
    }

    public UserAccount toUserAccount() {
        return new UserAccount(username, password);
    }

    public String getUsername() {
        return username;
    }

    public AccountActivationForm setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AccountActivationForm setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public AccountActivationForm setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
        return this;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountActivationForm that = (AccountActivationForm) o;

        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        return repeatPassword != null ? repeatPassword.equals(that.repeatPassword) : that.repeatPassword == null;

    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (repeatPassword != null ? repeatPassword.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AccountActivationForm{" +
                "username='" + username + '\'' +
                '}';
    }
}
