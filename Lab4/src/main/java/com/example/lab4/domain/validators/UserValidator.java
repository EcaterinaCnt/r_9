package com.example.lab4.domain.validators;

import com.example.lab4.domain.User;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        validateFirstName(entity.getFirstName());
        validateLastName(entity.getLastName());
    }

    private void validateFirstName(String firstName) throws ValidationException {
        if(firstName == null)
            throw new ValidationException("Prenumele trebuie sa fie diferit de null!");
        else if(firstName.length() >= 100)
            throw new ValidationException("Prenumele e prea lung!");
        else if(firstName.isEmpty())
            throw new ValidationException("Prenumele nu trebuie sa fie gol!");
        else if(! Character.isAlphabetic(firstName.charAt(0)))
            throw new ValidationException("Primul caracter trebuie sa fie o litera!");
    }


    private void validateLastName(String lastName) throws ValidationException {
        if(lastName == null)
            throw new ValidationException("Numele trebuie sa fie diferit de null!");
        else if(lastName.length() >= 100)
            throw new ValidationException("Numele e prea lung!");
        else if(lastName.isEmpty())
            throw new ValidationException("Numele nu trebuie sa fie gol!");
        else if(! Character.isAlphabetic(lastName.charAt(0)))
            throw new ValidationException("Primul caracter trebuie sa fie o litera!");
    }
}
