package com.example.emarketplace.service;

import com.example.emarketplace.dto.LoginDto;
import com.example.emarketplace.dto.RegisterDto;
import com.example.emarketplace.entity.User;
import com.example.emarketplace.repository.UserRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(RegisterDto dto) {
        if (!dto.getUsername().matches("^[a-zA-Z0-9]{8,16}$")) {
            throw new RuntimeException("Invalid username");
        }
        if (!EmailValidator.getInstance().isValid(dto.getEmail())) {
            throw new RuntimeException("Invalid email");
        }
        if (Period.between(dto.getBirthday(), LocalDate.now()).getYears() <= 13) {
            throw new RuntimeException("Must be older than 13");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setBirthday(dto.getBirthday());
        return userRepository.save(user);
    }

    public User login(LoginDto dto) {
        Optional<User> userOpt = userRepository.findByUsernameOrEmail(dto.getLogin());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(dto.getPassword())) {
                return user;
            }
        }
        throw new RuntimeException("Invalid credentials");
    }
}