package br.com.foursales.dao;

import br.com.foursales.model.ProdutoEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProdutoDAO extends CrudRepository<ProdutoEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT p.* FROM Produto p WHERE p.id = :id", nativeQuery = true)
    ProdutoEntity findByIdWithLock(Long id);
}
