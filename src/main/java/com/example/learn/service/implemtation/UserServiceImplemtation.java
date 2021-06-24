package com.example.learn.service.implemtation;

import com.example.learn.domain.User;
import com.example.learn.domain.UserPrincipal;
import com.example.learn.repository.UserRepository;
import com.example.learn.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImplemtation implements UserService, UserDetailsService {

    private Logger logger= LoggerFactory.getLogger(getClass());
    private UserRepository userRepository;

    @Autowired
    public UserServiceImplemtation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= userRepository.findUserByUsername(username);
        if(user==null){
            logger.error("user Not found By Username: "+username);
            throw new UsernameNotFoundException("user Not found By Username: "+username);
        }
        else{
            user.setLastLoginDateDisplay(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            userRepository.save(user);
            UserPrincipal userPrincipal=new UserPrincipal(user);
            logger.info("User found By Username: "+username);
            return userPrincipal;
        }
    }
}
