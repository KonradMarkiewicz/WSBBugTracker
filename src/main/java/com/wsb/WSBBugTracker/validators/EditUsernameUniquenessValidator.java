package com.wsb.WSBBugTracker.validators;

import com.wsb.WSBBugTracker.people.Person;
import com.wsb.WSBBugTracker.people.PersonForm;
import com.wsb.WSBBugTracker.people.PersonRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EditUsernameUniquenessValidator
        implements ConstraintValidator<UniqueUsername, PersonForm> {

    private final PersonRepository personRepository;

    public EditUsernameUniquenessValidator(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(PersonForm personform, ConstraintValidatorContext ctx) {

        Person foundPerson = personRepository.findByUsername(personform.getUsername());

        if (foundPerson == null) {
            return true;
        }

        boolean usernameIsUnique = personform.getId() != null && foundPerson.getId().equals(personform.getId());

        if (!usernameIsUnique) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(ctx.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("username")
                    .addConstraintViolation();
        }
        return usernameIsUnique;
    }
}
