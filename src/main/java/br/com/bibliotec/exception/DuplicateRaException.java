package br.com.bibliotec.exception;

import br.com.bibliotec.exeption.BibliotecException;

public class DuplicateRaException extends BibliotecException {
    
    public DuplicateRaException(String message, Object... parameter) {
        super(String.format(message, parameter));
    }
}
