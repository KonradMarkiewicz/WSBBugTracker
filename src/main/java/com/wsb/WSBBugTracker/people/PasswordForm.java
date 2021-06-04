package com.wsb.WSBBugTracker.people;

import com.wsb.WSBBugTracker.validators.ValidPasswords;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Transient;
import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@ValidPasswords
public class PasswordForm {

    Long id;

    @NotBlank
    @Size(min=5,max=100)
    String password;

    @Transient
    String repeatedPassword;

}
