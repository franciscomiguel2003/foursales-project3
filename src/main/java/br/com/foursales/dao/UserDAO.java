package br.com.foursales.dao;

import br.com.foursales.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserDAO extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}