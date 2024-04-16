package br.com.bibliotec.listener;

public interface FormDefinition {
    void open();
    void setNewBean();
    <T> void setBinder(T entity);
}
