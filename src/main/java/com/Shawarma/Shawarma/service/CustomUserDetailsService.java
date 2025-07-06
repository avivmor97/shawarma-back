package com.Shawarma.Shawarma.service;

import com.Shawarma.Shawarma.model.AppUser;
import com.Shawarma.Shawarma.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    public void save(String username, String encodedPassword, String phoneNumber) {
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setPhoneNumber(phoneNumber);
        userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );
    }

    public AppUser getAppUserByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public boolean existsByUsername(String username) {
        return userRepo.existsByUsername(username);
    }
}
