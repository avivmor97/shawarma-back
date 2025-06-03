package com.Shawarma.Shawarma.service;

import com.Shawarma.Shawarma.model.AppUser; // 👈 זה הדגם שלך במקום User
import com.Shawarma.Shawarma.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User; // 👈 זה של Spring Security

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    public void save(String username, String encodedPassword) {
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new User( // Spring Security User
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );
    }

    public boolean existsByUsername(String username) {
        return userRepo.existsByUsername(username);
    }
}
