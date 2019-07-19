package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.AdminService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommonController {

    @Autowired
    private AdminService adminService;

    @RequestMapping(method= RequestMethod.GET, path="/userprofile/{userId}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> userSignup(@PathVariable("userId") final String userUuid, @RequestHeader("authorization") final String authorization) throws UserNotFoundException,AuthorizationFailedException {

        final UserEntity userEntity = adminService.getUser(userUuid,authorization);
        UserDetailsResponse userDetailsResponse = new UserDetailsResponse();

        userDetailsResponse.setAboutMe(userEntity.getAboutMe());
        userDetailsResponse.setContactNumber(userEntity.getContactNumber());
        userDetailsResponse.setCountry(userEntity.getCountry());
        userDetailsResponse.setDob(userEntity.getDob());
        userDetailsResponse.setEmailAddress(userEntity.getEmail());
        userDetailsResponse.setFirstName(userEntity.getFirstName());
        userDetailsResponse.setLastName(userEntity.getLastName());
        userDetailsResponse.setUserName(userEntity.getUserName());

        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
    }
}
