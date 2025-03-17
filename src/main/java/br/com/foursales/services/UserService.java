package br.com.foursales.services;

import br.com.foursales.autentication.services.exceptions.FourSalesBusinessException;
import br.com.foursales.dao.UserDAO;
import br.com.foursales.model.UserEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
@Service
public class UserService implements UserDetailsService {

    private Logger logger = LogManager.getLogger(Thread.currentThread().getClass().getName());
    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserDAO userDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userDAO.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new User(user.getUsername(), user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())));
    }

    public UserEntity saveUser(UserEntity user) {
        //Encripta a senha

        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userDAO.save(user);
        } catch (DataIntegrityViolationException er){
            throw new FourSalesBusinessException("Username já existente, tente outro!");
        } catch (Exception e){
            logger.error("Erro inesperado ao criar usuário: {}", e.getMessage());
            throw new RuntimeException("Erro inesperado ao criar usuário: ", e);
        }

    }
}
