package semillero.ubuntu.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import semillero.ubuntu.entities.UserEntity;
import semillero.ubuntu.repository.UserRepository;
import semillero.ubuntu.service.contract.UserService;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserEntity saveUser(UserEntity user) {
        logger.info("Save user");
        return userRepository.save(user);
    }

    @Override
    public UserEntity updateUser(UserEntity user, Long userId) {
        logger.info("Update user");
        UserEntity updateUser = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("The User with ID: " + userId + " was not found"));

        if (updateUser != null) {
            updateUser.setName(user.getName());
            updateUser.setLastName(user.getLastName());
            updateUser.setRole(user.getRole());
            updateUser.setPhone(user.getPhone());

            userRepository.save(updateUser);
        } else {
            logger.info("User not found");
        }
        return updateUser;
    }


    @Override
    public UserEntity disableUser(Long userId) {
        logger.info("Disable User");
        UserEntity disableUser = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("The User with email: " + userId + " was not found"));

        if (disableUser.getDisabled() == true) {
            disableUser.setDisabled(false);
            userRepository.save(disableUser);
        }else{
            disableUser.setDisabled(true);
            userRepository.save(disableUser);
        }

        return disableUser;
    }

    @Override
    public UserEntity findUserByEmail(String email) {
        logger.info("Find User by Email");
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("The User with email: " + email + " was not found"));

        return user;
    }

    /**
     * Carga los detalles de un usuario por su nombre de usuario (en este caso, el correo electrónico).
     * @param username El nombre de usuario (correo electrónico) del usuario.
     * @return UserDetails que representa los detalles del usuario.
     * @throws UsernameNotFoundException Si no se encuentra un usuario con el correo electrónico proporcionado.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                "",
                user.getAuthorities()
        );
    }

    //devolver todos los correos de los usuarios administradores
    @Override
    public List<String> getAllAdminEmails(){
        return userRepository.getAllAdminEmails();
    }
}
