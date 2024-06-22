package br.com.bibliotec.ui.helper;

import br.com.bibliotec.anotation.Bind;
import br.com.bibliotec.controller.helper.GenericController;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.interfaces.HasId;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.lang.reflect.Field;

public class Binder<T extends HasId<I>, I, C extends GenericController<T,I,?>> implements HasValue<HasValue.ValueChangeEvent<T>, T> {

    protected T currentBean;
    protected BeanValidationBinder<T> internal;
    protected Class<T> beanType;
    
    private final C controller;
    private final Object container;

    public Binder(Class<T> beanType, Object container, C controller) {
        this.container = container;
        this.beanType = beanType;
        this.controller = controller;
        
        internal = new BeanValidationBinder<>(beanType);
    }
    
    public void createBinder() throws IllegalAccessException {
        for (Field field : container.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getAnnotation(Bind.class) != null) {
                Bind bind = field.getAnnotation(Bind.class);
                HasValue<?, ?> element = (HasValue<?, ?>) field.get(container);
                internal.forField(element).bind(bind.value());
            }
        }
    }
    
    public void loadBean(I id) throws BibliotecException {
        T entity = controller.load(id);
        this.currentBean = entity;
        internal.readBean(entity);
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
