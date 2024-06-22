package br.com.bibliotec.exception;

import br.com.bibliotec.exeption.BibliotecException;

public class CodeIncorrectException extends BibliotecException {

    public CodeIncorrectException(String message, Object... parameter) {
        super(String.format(message, parameter));
    }
}
