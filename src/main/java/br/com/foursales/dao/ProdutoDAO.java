package br.com.foursales.dao;

import br.com.foursales.model.ProdutoEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProdutoDAO extends JpaRepository<ProdutoEntity, Integer> {

    @Query(value = "SELECT p.* FROM Produto p WHERE p.id = :id FOR UPDATE", nativeQuery = true)
    ProdutoEntity findByIdWithLock(@Param("id") Integer id);

}
