package andreibri.u5_w3_d5.services;

import andreibri.u5_w3_d5.entities.User;
import andreibri.u5_w3_d5.exception.NotFoundException;
import andreibri.u5_w3_d5.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}

