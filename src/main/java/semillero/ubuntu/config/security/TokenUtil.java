package semillero.ubuntu.config.security;
import io.jsonwebtoken.Jwts;
import semillero.ubuntu.entities.User;
import java.util.Date;
import org.springframework.stereotype.Component;


@Component
public class TokenUtil {

    private static final String SECRET_KEY = "yasemeolvido";

    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 864000000);  // válido por 10 días

        return Jwts.builder()
                .setSubject(Long.toString(user.getUserId()))
                .claim("email", user.getEmail())
                .claim("firstName", user.getName())
                .claim("lastName", user.getLastName())
                //.claim("photoUrl", user.getPhotoUrl())
                .claim("role", user.getRole())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                //.signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
}

