package com.example.emarketplace.service;

import com.example.emarketplace.dto.LoginDto;
import com.example.emarketplace.dto.RegisterDto;
import com.example.emarketplace.entity.User;
import com.example.emarketplace.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Spy
    private User spyUser = new User();

    @InjectMocks
    private UserService userService;

    @Test
    public void testRegisterSuccess() {
        RegisterDto dto = new RegisterDto();
        dto.setUsername("student123");
        dto.setEmail("student@mail.com");
        dto.setPassword("password123");
        dto.setBirthday(LocalDate.of(2000, 5, 10));

        when(userRepository.save(any(User.class))).thenReturn(spyUser);

        User result = userService.register(dto);

        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterThrowException() {
        RegisterDto dto = new RegisterDto();
        dto.setUsername("student123");
        dto.setEmail("student@mail.com");
        dto.setPassword("password123");
        dto.setBirthday(LocalDate.of(2000, 5, 10));

        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.register(dto);
        });

        assertEquals("Database error", exception.getMessage());
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testLoginSuccess() {
        LoginDto dto = new LoginDto();
        dto.setLogin("student@mail.com");
        dto.setPassword("password123");

        User user = new User();
        user.setPassword("password123");

        when(userRepository.findByUsernameOrEmail("student@mail.com")).thenReturn(Optional.of(user));

        User result = userService.login(dto);

        assertEquals("password123", result.getPassword());
        verify(userRepository).findByUsernameOrEmail("student@mail.com");
    }
}