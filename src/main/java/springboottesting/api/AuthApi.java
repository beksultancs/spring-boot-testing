package springboottesting.api;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboottesting.dto.request.AuthRequest;
import springboottesting.dto.request.RegisterRequest;
import springboottesting.dto.response.AuthResponse;
import springboottesting.exceptions.AlreadyExistsException;
import springboottesting.model.Authority;
import springboottesting.model.User;
import springboottesting.repository.UserRepository;
import springboottesting.util.JwtUtil;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/authentication")
@AllArgsConstructor
public class AuthApi {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    @PostMapping
    AuthResponse authenticate(@RequestBody AuthRequest authRequest) {

        User user = userRepository.findByEmail(authRequest.email())
                .orElseThrow(() -> new EntityNotFoundException(String.format("user(%s) not found", authRequest.email())));

        String encodedPassword = user.getPassword();
        String password = authRequest.password();

        if (!passwordEncoder.matches(password, encodedPassword)) {
            throw new BadCredentialsException(
                    "Invalid Password!"
            );
        }

        return new AuthResponse(
                user.getId(),
                jwtUtil.generate(user.getEmail())
        );
    }

    @PostMapping("/register")
    AuthResponse register(@RequestBody RegisterRequest registerRequest) {

        boolean exists = userRepository.exists(
                Example.of(new User(null, null, registerRequest.email(), null, null, null))
        );

        if (exists) {
            throw new AlreadyExistsException(
                    "entity already exists!"
            );
        }

        String encodedPassword = passwordEncoder.encode(registerRequest.password());

        User saved = userRepository.save(
                new User(null, registerRequest.name(), registerRequest.email(), encodedPassword, Authority.USER, new ArrayList<>())
        );

        String jwt = jwtUtil.generate(saved.getEmail());

        return new AuthResponse(
                saved.getId(),
                jwt
        );
    }

}
