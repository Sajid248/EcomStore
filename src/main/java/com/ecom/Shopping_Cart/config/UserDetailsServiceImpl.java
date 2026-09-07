package com.ecom.Shopping_Cart.config;

import com.ecom.Shopping_Cart.model.userDetails;
import com.ecom.Shopping_Cart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        userDetails user = userRepository.findByEmail(username);

        if(user == null)
        {
            throw new UsernameNotFoundException(("User Not found"));
        }
        return new CustomUser(user);
    }
}
