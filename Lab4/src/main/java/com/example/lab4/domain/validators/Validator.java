package com.example.lab4.domain.validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
} //metoda de validare utilizabila in cadrul stocarii datelor in Repo