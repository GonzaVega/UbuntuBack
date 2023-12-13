package semillero.ubuntu.service.contract;

import org.springframework.security.core.userdetails.UserDetailsService;
import semillero.ubuntu.entities.User;

public interface UserService extends UserDetailsService {

    User saveUser(User user);
    User updateUser(User user, Long userId);
    User disableUser(Long userId);
   // User updateUser(User user, String userId);
    //User disableUser(String userId);
    User findUserByEmail(String email);

}
