package semillero.ubuntu.service.contract;

import semillero.ubuntu.entities.User;

public interface UserService {

    User saveUser(User user);
    User updateUser(User user);
    User disableUser(String userId);
}