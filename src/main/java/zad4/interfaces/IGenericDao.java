package zad4.interfaces;

import java.util.List;
import java.util.Optional;

public interface IGenericDao<T,K> {
    T save(T t);
    void delete (T t);
    void update (T t);
    Optional<T> findById(K id);
    List<T> findAll();
}
