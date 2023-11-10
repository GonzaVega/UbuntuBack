package semillero.ubuntu.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {

    @GetMapping("/")
    public String home(){
        return "Hello, home";
    }

    @GetMapping("/secured")
    public String secured(){
        return "Hello, secured";
    }

}
