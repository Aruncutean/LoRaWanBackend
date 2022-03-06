package com.LoRaWan.LoRaWan.Controller;


import com.LoRaWan.LoRaWan.Date.User;
import com.LoRaWan.LoRaWan.Dto.*;

import com.LoRaWan.LoRaWan.Exception.ExceptionEmailExists;
import com.LoRaWan.LoRaWan.Exception.ExceptionNotFoundEmail;
import com.LoRaWan.LoRaWan.Exception.ExceptionPasswordIncorrect;
import com.LoRaWan.LoRaWan.Service.UserService;
import com.LoRaWan.LoRaWan.Util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @PostMapping("/addUser")
    public MessageDTO addUser(@RequestBody UserDto user) {
        String message = "Account is create";
        try {
            userService.addUser(user);
        } catch (ExceptionEmailExists e) {
            message = "Email exists";
        }
        return new MessageDTO(message);
    }

    @GetMapping("/userInfo/{email}")
    public UserInfo setUserRol(@PathVariable(value = "email") String email) {
        return userService.getUserInfo(email);
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationDTO authenticationDTO) throws Exception {
        String message = "Ok!!";
        System.out.println(authenticationDTO.getEmail() + " " + authenticationDTO.getPassword());
        UserDetails userDetails = null;
        String jwt = "";
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationDTO.getEmail(), authenticationDTO.getPassword()));
        } catch (BadCredentialsException e) {

        }
        try {
            userDetails = userService.loadUserByEmail(authenticationDTO.getEmail(), authenticationDTO.getPassword());
            jwt = jwtTokenUtil.generateToken(userDetails);
        } catch (ExceptionNotFoundEmail e) {
            message = "Email not exist!!!";

        } catch (ExceptionPasswordIncorrect e) {
            message = "Password is incorrect!!!";
        }
        return ResponseEntity.ok(new AuthenticationResponse(jwt, message));
    }

    @PutMapping("/userInfo/{email}")
    public void makeUserAdmin() {

    }


}
