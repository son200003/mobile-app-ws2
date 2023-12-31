package com.example.mobileappws.service.impl;

import com.example.mobileappws.UserRepository;
import com.example.mobileappws.exceptions.UserServiceException;
import com.example.mobileappws.io.entity.UserEntity;
import com.example.mobileappws.service.UserService;
import com.example.mobileappws.shared.Utils;
import com.example.mobileappws.shared.dto.UserDto;
import com.example.mobileappws.ui.model.response.ErrorMessages;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if(userEntity == null) throw new UsernameNotFoundException(email);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnValue);
        return returnValue;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserDto returnValue = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null) throw new UsernameNotFoundException("User with ID:" + userId + "not found");
        BeanUtils.copyProperties(userEntity, returnValue);
        return returnValue;
    }

    @Override
    public UserDto updateUser(String userId, UserDto user) {
        UserDto returnValue = new UserDto();

        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null)
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());

        UserEntity updatedUserDetails = userRepository.save(userEntity);
//        returnValue = new ModelMapper().map(updatedUserDetails, UserDto.class);
        BeanUtils.copyProperties(updatedUserDetails, returnValue);
        return returnValue;
    }

    @Override
    public void deleteUser(String userId) {
          UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null)
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        userRepository.delete(userEntity);
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> returnValue = new ArrayList<>();

        if(page>0) page = page -1;

        Pageable pageableRequest  =  PageRequest.of( page , limit);

        Page<UserEntity> usersPage = userRepository.findAll(pageableRequest);
        List<UserEntity> users = usersPage.getContent();

        for(UserEntity userEntity : users){
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity, userDto);
            returnValue.add(userDto);
        }
        return returnValue;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       UserEntity userEntity = userRepository.findByEmail(email);

       if(userEntity == null) throw new UsernameNotFoundException(email);
       return  new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }


}


