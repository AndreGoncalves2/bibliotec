package br.com.bibliotec.controller.helper;


import br.com.bibliotec.exeption.BibliotecException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public class GenericController<T, I, R extends JpaRepository<T, I>> {

    protected  R repository;

    protected enum Mode {
        SAVE,
        LOAD,
        DELETE,
        UPDATE,
        FIND_ALL
    }

    protected void validate(T entity, Mode mode) throws BibliotecException {}

    protected void afterExecute(T entity, Mode mode) throws BibliotecException {}

    public List<T> list() {
        return repository.findAll().reversed();
    }

    public T save(T entity) throws BibliotecException {
        validate(entity, Mode.SAVE);

        var response = repository.save(entity);

        afterExecute(response, Mode.SAVE);

        return response;
    }

    public T load(I id) throws BibliotecException {
        validate(null, Mode.LOAD);

        var response = repository.findById(id).orElseThrow(() -> new BibliotecException("Nenhum registro encontrado para o ID: " + id));

        afterExecute(null, Mode.LOAD);

        return response;
    }

    public T update(T entity) throws BibliotecException {
        validate(entity, Mode.UPDATE);

        var response = repository.save(entity);

        afterExecute(response, Mode.UPDATE);

        return response;
    }

    public void delete(T entity) throws BibliotecException {
        validate(entity, Mode.DELETE);

        try {
            repository.delete(entity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        afterExecute(entity, Mode.DELETE);
    }
}