package andreibri.u5_w3_d5.controller;

import andreibri.u5_w3_d5.entities.User;
import andreibri.u5_w3_d5.enums.Role;
import andreibri.u5_w3_d5.repository.UserRepository;
import andreibri.u5_w3_d5.security.LoginRequest;
import andreibri.u5_w3_d5.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/register")
    public User register(@RequestBody LoginRequest req) {
        User u = new User();
        u.setUsername(req.getUsername());
        u.setPassword(encoder.encode(req.getPassword())); // OK
        u.setRole(Role.USER);
        return userRepository.save(u);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest req) {

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getUsername(),
                        req.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        return jwtService.generateToken(userDetails);
    }
}