package br.com.foursales.dao;

import br.com.foursales.model.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserDAO extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}