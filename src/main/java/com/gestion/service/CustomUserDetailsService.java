package com.gestion.service;

import com.gestion.model.User;
import com.gestion.repository.UserRepository;
import com.gestion.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service()
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    public UserRepository userRepository;

    public CustomUserDetailsService() {
    }

    /**
     *  Load User by username
     * @param login of the user
     * @return User details retrieved
     * @throws UsernameNotFoundException
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(login);
        if(user == null) throw new UsernameNotFoundException("User Not Found with Email: " + login);

        return UserDetailsImpl.build(user);
    }
}
