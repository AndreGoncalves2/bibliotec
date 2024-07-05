package br.com.bibliotec.exception;

public class CodeIncorrectException extends BibliotecException {

    public CodeIncorrectException(String message, Object... parameter) {
        super(String.format(message, parameter));
    }
}
