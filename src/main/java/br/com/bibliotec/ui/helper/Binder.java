package br.com.bibliotec.ui.helper;

import br.com.bibliotec.anotation.Bind;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.lang.reflect.Field;

public class Binder<T> implements HasValue<HasValue.ValueChangeEvent<T>, T> {

    protected T currentBean;
    protected BeanValidationBinder<T> internal;
    protected Class<T> beanType;
    private final Object container;

    public Binder(Class<T> beanType, Object container) {
        this.container = container;
        this.beanType = beanType;
        internal = new BeanValidationBinder<>(beanType);
    }
    
    public void createBean() throws IllegalAccessException {
        for (Field field : container.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getAnnotation(Bind.class) != null) {
                Bind bind = field.getAnnotation(Bind.class);
                HasValue<?, ?> element = (HasValue<?, ?>) field.get(container);
                internal.forField(element).bind(bind.value());
            }
        }
    }
    
    public void readBean(T entity) {
        this.currentBean = entity;
        internal.readBean(entity);
    }
    
    public void writeBean(T entity) throws ValidationException {
        internal.writeBean(entity);
    }
    
    public boolean isValid() {
        return internal.isValid();
    }

    @Override
    public void setValue(T value) {
        readBean(value);
    }

    @Override
    public T getValue() {
        return currentBean;
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super ValueChangeEvent<T>> valueChangeListener) {
        return null;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        internal.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public void setRequiredIndicatorVisible(boolean b) {

    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return false;
    }
}
