package com.gestion.services;

import com.gestion.models.User;
import com.gestion.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service()
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    public UserService userService;

    public UserDetailsServiceImpl() {
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
        User user = userService.findByLogin(login);
        if(user == null) throw new UsernameNotFoundException("User Not Found with login: " + login);

        return UserDetailsImpl.build(user);
    }
}
