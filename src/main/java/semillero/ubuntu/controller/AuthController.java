package semillero.ubuntu.controller;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import semillero.ubuntu.security.jwt.TokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import semillero.ubuntu.entities.User;
import semillero.ubuntu.service.contract.UserService;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenUtil tokenUtil;

//    private UserService userService;
//    private final TokenUtil tokenUtil;
//
//    @Autowired
//    public AuthController(UserService userService, TokenUtil tokenUtil) {
//        this.userService = userService;
//        this.tokenUtil = tokenUtil;
//    }

    //controler para manejar la autenticación con google oauth
    @PostMapping("/google")
    public ResponseEntity<?> googleAuth(@RequestParam Map<String, String> payload) throws Exception {
        try{
            //obtenemos el googleTokenId de google
            String googleTokenId = payload.get("tokenId");

            //comprobamos que tenga un valor
            if(googleTokenId == null || googleTokenId.isEmpty()){
                throw new Exception("No se ha proporcionado un token");
            }

            //verificamos que sea un token de google válido
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory()).build();
            GoogleIdToken idToken = verifier.verify(googleTokenId);

            if(idToken == null){
                throw new Exception("Token de google no válido");
            }

            //obtenemos los datos del usuario de google
            GoogleIdToken.Payload googleUserPayload = idToken.getPayload();

            String email = googleUserPayload.getEmail();
            String name = (String) googleUserPayload.get("given_name");
            String lastName = (String) googleUserPayload.get("family_name");
            String picture = (String) googleUserPayload.get("picture");

            // Verificar si el usuario ya existe en la base de datos
            User existingUser = userService.findUserByEmail(email);
            if(existingUser == null){
                throw new Exception("Usuario no registrado");
            }

            // El usuario ya existe, verificamos su rol
            String userRole = String.valueOf(existingUser.getRole());
            if(!"ADMIN".equals(userRole)){
                throw new Exception("Usuario no autorizado");
            }

            // ------Autenticar al usuario en Spring Security--------
            UserDetails userDetails = existingUser;
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);


            // El usuario es admin, creamos el token
            String token = tokenUtil.generateToken(existingUser,picture);


            //construimos la respuesta positiva con el token y los datos del usuario para una fácil disposición en el front
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("email", email);
            response.put("firstName", name);
            response.put("lastName", lastName);
            response.put("photoUrl", picture);
            response.put("role", userRole);
            response.put("authenticationToken", authenticationToken);

            return ResponseEntity.ok(response);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"Token inválido.\"}");
        }
    }
    
}
