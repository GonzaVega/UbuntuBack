package semillero.ubuntu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import semillero.ubuntu.entities.User;
import semillero.ubuntu.service.contract.UserService;

@RestController
@RequestMapping("/api/v1/")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> saveEUser(@RequestBody User newUser) {
        return new ResponseEntity<>(userService.saveUser(newUser), HttpStatus.CREATED);
    }

    @PutMapping("{userId}")
    public ResponseEntity<User> updateUser(@PathVariable String userId, @RequestBody User updatedUser) {
        User updateUser = userService.updateUser(updatedUser, userId);

        if (updateUser != null) {
            return new ResponseEntity<>(updateUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/disable/{userId}")
    public ResponseEntity<User> disableUser(@PathVariable String userId) {
        User disableUser = userService.disableUser(userId);

        if (disableUser != null) {
            return new ResponseEntity<>(disableUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
