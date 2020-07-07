package com.example.login_register.service;

import com.example.login_register.model.User;
import com.example.login_register.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public boolean findUserByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
                /*.orElseThrow(() -> new UsernameNotFoundException(
                String.format("User `%s` not found", email)));*/
    }

    public User findUser(String username) {
        return userRepository.findByUsername(username).get();
                /*.orElseThrow(() -> new UsernameNotFoundException(
                String.format("User `%s` not found", email)));*/
    }

    public void registerNewUser(User user) {

        //Optional<User> found = userRepository.findByUsername(user.getUsername());
        /*ModelMapper modelMapper = new ModelMapper();
        User user2 = modelMapper.map(user, User.class);*/
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new String[] {"USER"});

        userRepository.save(user);

    }

    public User save(User user){
        return userRepository.save(user);
    }


}
