package br.com.bibliotec.interfaces;

public interface FormDefinition {
    void open();
    void setNewBean();
    <T> void setBinder(T entity);
}
