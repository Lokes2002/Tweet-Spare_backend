package com.baap.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.baap.model.User;
import com.baap.repository.UserRepository;

@Service
public class CustomUserDetailsServiceImplementation implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);

        if (user == null || user.isLoginWithGoogle()) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }

        // You can retrieve roles or authorities if needed
        List<GrantedAuthority> authorities = new ArrayList<>();
        // Add authorities if needed

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), authorities);
    }
}
