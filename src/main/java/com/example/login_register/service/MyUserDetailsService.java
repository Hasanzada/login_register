package com.example.login_register.service;

import com.example.login_register.model.User;
import com.example.login_register.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class MyUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    @Autowired
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static UserDetails mapper(User user) {
        return new MyUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRoles()
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = userRepository.findByUsername(username)
                .map(MyUserDetailsService::mapper)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User `%s` not found", username)));
        log.info(userDetails);
        return userDetails;
    }
}
