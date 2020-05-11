package com.company.service;

import com.company.dao.impl.UserRepository;
import com.company.entity.User;
import com.company.entity.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User.UserBuilder;

import java.util.ArrayList;
import java.util.List;


@Service("customUserDetail")
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        UserBuilder userBuilder = null;

        if (user!=null){
            userBuilder = org.springframework.security.core.userdetails.User.withUsername(email);

            userBuilder.disabled(false);
            userBuilder.password(user.getPassword());

            List<UserRoles> userRoles = user.getUserRolesList();
            String[] authoritiesArr = new String[userRoles.size()];

            for (int i =0;i<userRoles.size();i++){
                UserRoles userR = userRoles.get(i);
                authoritiesArr[i] = userR.getRole();
            }
            userBuilder.authorities(authoritiesArr);

            return userBuilder.build();
        }
        else {
            throw new UsernameNotFoundException("User not found");
        }
    }

}
