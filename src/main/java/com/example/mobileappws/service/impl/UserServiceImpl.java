package com.example.mobileappws.service.impl;

import com.example.mobileappws.UserRepository;
import com.example.mobileappws.io.entity.UserEntity;
import com.example.mobileappws.service.UserService;
import com.example.mobileappws.shared.Utils;
import com.example.mobileappws.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
 public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils utils;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto user) {

        if(userRepository.findByEmail(user.getEmail()) != null ) throw new RuntimeException("Record already exitsts");

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user,userEntity );

        String publicUserId = utils.generateUserId(30);
        userEntity.setUserId(publicUserId);
         userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//        userEntity.setEncryptedPassword("test");


      UserEntity  storeUserDetails = userRepository.save(userEntity);


        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(storeUserDetails, returnValue);
        return  returnValue ;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}


