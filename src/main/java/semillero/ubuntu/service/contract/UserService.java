package semillero.ubuntu.service.contract;

import org.springframework.security.core.userdetails.UserDetailsService;
import semillero.ubuntu.entities.UserEntity;

public interface UserService extends UserDetailsService {

    UserEntity saveUser(UserEntity user);
    UserEntity updateUser(UserEntity user, Long userId);
    UserEntity disableUser(Long userId);
   // User updateUser(User user, String userId);
    //User disableUser(String userId);
    UserEntity findUserByEmail(String email);

}
