package com.LoRaWan.LoRaWan.Service;

import com.LoRaWan.LoRaWan.Date.Rol;
import com.LoRaWan.LoRaWan.Dto.UserDto;
import com.LoRaWan.LoRaWan.Dto.UserInfo;
import com.LoRaWan.LoRaWan.Exception.ExceptionEmailExists;
import com.LoRaWan.LoRaWan.Exception.ExceptionNotFoundEmail;
import com.LoRaWan.LoRaWan.Exception.ExceptionPasswordIncorrect;
import com.LoRaWan.LoRaWan.Repository.RolRepository;
import com.LoRaWan.LoRaWan.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolRepository rolRepository;


    public void addUser(UserDto user) throws ExceptionEmailExists {
        com.LoRaWan.LoRaWan.Date.User userByEmail = userRepository.getUserByEmail(user.getEmail());
        if (userByEmail == null) {
            com.LoRaWan.LoRaWan.Date.User userSaveInDataBase = new com.LoRaWan.LoRaWan.Date.User();
            userSaveInDataBase.setEmail(user.getEmail());
            userSaveInDataBase.setUserName(user.getUserName());
            userSaveInDataBase.setFirstName(user.getFirstName());
            userSaveInDataBase.setLastName(user.getLastName());
            userSaveInDataBase.setPassword(user.getPassword());
            userRepository.save(userSaveInDataBase);

            Rol rol = new Rol();
            rol.setRol("user");
            rol.setUser(userSaveInDataBase);
            rolRepository.save(rol);
        } else {
            throw new ExceptionEmailExists();
        }
    }


    public UserDetails loadUserByEmail(String email, String password) throws ExceptionPasswordIncorrect, ExceptionNotFoundEmail {
        com.LoRaWan.LoRaWan.Date.User user = userRepository.getUserByEmail(email);

        if (user != null) {
            if (!password.equals(user.getPassword())) {
                throw new ExceptionPasswordIncorrect();
            }
        } else {
            throw new ExceptionNotFoundEmail();
        }
        return new User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.LoRaWan.LoRaWan.Date.User user = userRepository.getUserByEmail(email);
        return new User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }


    public UserInfo getUserInfo(String email) {
        com.LoRaWan.LoRaWan.Date.UserInfo userInfo = userRepository.userInfo(email);

        UserInfo userInfo1 = new UserInfo();
        userInfo1.setUserName(userInfo.getName());
        userInfo1.setEmail(userInfo.getEmail());
        userInfo1.setFirstName(userInfo.getFn());
        userInfo1.setLastName(userInfo.getLn());
        userInfo1.setRol(userInfo.getRol());
        return userInfo1;

    }


}
