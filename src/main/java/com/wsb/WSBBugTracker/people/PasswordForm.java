package com.wsb.WSBBugTracker.people;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Transient;
import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
public class PasswordForm {

    Long id;

    @NotBlank
    String password;

    @NotBlank
    @Transient
    String repeatedPassword;

    boolean isValid;

    @AssertTrue(message = "Hasła powinny być identyczne")
    public boolean isValid() {
        if (password == null)
            return false;
        if (repeatedPassword == null)
            return false;
        else
            return this.password.equals(this.repeatedPassword);
    }
}
