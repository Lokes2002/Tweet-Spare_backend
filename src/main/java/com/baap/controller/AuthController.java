package com.baap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baap.Config.JwtProvider;
import com.baap.exception.UserException;
import com.baap.model.User;
import com.baap.model.Varification;
import com.baap.repository.UserRepository;
import com.baap.response.AuthResponse;
import com.baap.service.CustomUserDetailsServiceImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private CustomUserDetailsServiceImplementation customUserDetails;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {
        logger.info("Attempting to sign up user with email: {}", user.getEmail());
        String email = user.getEmail();
        String password = user.getPassword();
        String fullName = user.getFullName();
        String birthDate = user.getBirthDate();

        User existingUser = userRepository.findByEmail(email);
        if (existingUser != null) {
            logger.warn("Email {} is already in use", email);
            throw new UserException("Email is already used with another account");
        }

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setFullName(fullName);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setBirthDate(birthDate);
        newUser.setVerification(new Varification());

        User savedUser = userRepository.save(newUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse res = new AuthResponse(token,true);
        logger.info("User signed up successfully with email: {}", email);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody User user) {
        logger.info("Attempting to sign in user with email: {}", user.getEmail());
        String email = user.getEmail();
        String password = user.getPassword();

        try {
            Authentication authentication = authenticate(email, password);
            String token = jwtProvider.generateToken(authentication);

            AuthResponse res = new AuthResponse(token, true);
            logger.info("User signed in successfully with email: {}", email);
            return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
        } catch (BadCredentialsException ex) {
            logger.error("Invalid credentials for email: {}", email);
            return new ResponseEntity<>(new AuthResponse(null, false), HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            logger.error("An error occurred during signin for email: {}", email, ex);
            return new ResponseEntity<>(new AuthResponse(null, false), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = customUserDetails.loadUserByUsername(email);

        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username...");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<AuthResponse> handleUserException(UserException ex) {
        logger.error("User exception: {}", ex.getMessage());
        return new ResponseEntity<>(new AuthResponse(null, false), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AuthResponse> handleException(Exception ex) {
        logger.error("An unexpected error occurred: ", ex);
        return new ResponseEntity<>(new AuthResponse(null, false), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
