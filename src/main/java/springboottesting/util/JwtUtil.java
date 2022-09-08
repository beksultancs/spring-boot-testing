package springboottesting.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
@ConfigurationProperties(prefix = "app.jwt")
@Getter
@Setter
public class JwtUtil {

    private String secret;

    private String issuer;

    private long expiresAfterDays;

    // generate jwt
    public String generate(String email) {

        Date expiresAt = Date.from(ZonedDateTime.now().plusDays(expiresAfterDays).toInstant());

        return JWT.create()
                .withIssuer(issuer)
                .withIssuedAt(new Date())
                .withExpiresAt(expiresAt)
                .withSubject(email)
                .sign(Algorithm.HMAC256(secret));
    }

    public String verifyJwtTokenAndGetEmail(String token) {

        Algorithm algorithm = Algorithm.HMAC256(secret);

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();

        DecodedJWT decodedJWT = verifier.verify(token);

        return decodedJWT.getSubject();
    }
}
