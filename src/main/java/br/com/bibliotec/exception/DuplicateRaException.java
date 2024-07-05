package br.com.bibliotec.exception;

public class DuplicateRaException extends BibliotecException {
    
    public DuplicateRaException(String message, Object... parameter) {
        super(String.format(message, parameter));
    }
}
