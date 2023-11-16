package semillero.ubuntu.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import semillero.ubuntu.entities.User;
import semillero.ubuntu.repository.UserRepository;
import semillero.ubuntu.service.contract.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;


    @Override
    public User saveUser(User user) {
        logger.info("Save Employee");
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user, String userId) {
        logger.info("Update Employee");
        User updateUser = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("The User with ID: " + userId + " was not found"));

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
    public User disableUser(String userId) {
        logger.info("Disable User");
        User disableUser = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("The User with email: " + userId + " was not found"));

        if (disableUser.getDisabled() == true) {
            disableUser.setDisabled(false);
            userRepository.save(disableUser);
        }

        return disableUser;
    }

    @Override
    public User findUserByEmail(String email) {
        logger.info("Find User by Email");
        return userRepository.findByEmail(email);
    }
}
