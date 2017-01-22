package at.ac.tuwien.inso.controller.forms;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.ScriptAssert;

import at.ac.tuwien.inso.entity.UisUser;
import at.ac.tuwien.inso.entity.UserAccount;

@ScriptAssert(lang = "javascript", script = "_this.password === _this.repeatPassword", message = "{repeat.password.error}")
public class AccountActivationForm {

    @NotEmpty
    private String password;

    @NotEmpty
    private String repeatPassword;

    protected AccountActivationForm() {

    }

    public AccountActivationForm(String password, String repeatPassword) {
        this.password = password;
        this.repeatPassword = repeatPassword;
    }

    public UserAccount toUserAccount(UisUser user) {
        return new UserAccount(user.getIdentificationNumber(), password);
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

        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        return repeatPassword != null ? repeatPassword.equals(that.repeatPassword) : that.repeatPassword == null;

    }

    @Override
    public int hashCode() {
        int result = password != null ? password.hashCode() : 0;
        result = 31 * result + (repeatPassword != null ? repeatPassword.hashCode() : 0);
        return result;
    }
}
