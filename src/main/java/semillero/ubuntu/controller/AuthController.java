package semillero.ubuntu.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.user.OAuth2User;
import semillero.ubuntu.config.security.TokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import semillero.ubuntu.entities.User;
import semillero.ubuntu.service.contract.UserService;

import java.util.HashMap;
import java.util.Map;


@RestController
public class AuthController {

    private UserService userService;
    private final TokenUtil tokenUtil;

    @Autowired
    public AuthController(UserService userService, TokenUtil tokenUtil) {
        this.userService = userService;
        this.tokenUtil = tokenUtil;
    }

   @GetMapping("/")
   public String home(){
       return "Hello, home";
   }

   @GetMapping("/secured")
   public String secured(){
       return "Hello, secured";
   }

    //controler para manejar la autenticación con google oauth
    @PostMapping("/google")
    public ResponseEntity<Map<String, Object>> loginSuccess(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        String firstName = principal.getAttribute("given_name");
        String lastName = principal.getAttribute("family_name");
        String photoUrl = principal.getAttribute("picture");

        // Verificar si el usuario ya existe en la base de datos local
        User existingUser = userService.findUserByEmail(email);

        if (existingUser != null) {
            // El usuario ya existe, verificamos su rol
            String userRole = String.valueOf(existingUser.getRole());

            if ("ADMIN".equals(userRole)) {
                // El usuario es admin, creamos el token
                String token = tokenUtil.generateToken(existingUser);

                // Construimos la respuesta positiva con el token y los datos del usuario para una fácil disposición en el front
                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("email", email);
                response.put("firstName", firstName);
                response.put("lastName", lastName);
                response.put("photoUrl", photoUrl);
                response.put("role", userRole);

                return ResponseEntity.ok(response);
            } else {
                // El usuario no es admin, respondemos con no autorizado
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            // Usuario no registrado, respondemos con no autorizado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


}
