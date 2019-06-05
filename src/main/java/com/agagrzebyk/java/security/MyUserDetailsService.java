package com.agagrzebyk.java.security;

import com.agagrzebyk.java.model.User;
import com.agagrzebyk.java.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = this.userRepository.findByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException("No user found with username: " + email);
        }


        MyUserDetails myUserDetails = new MyUserDetails(user);

        return myUserDetails;
    }
}
