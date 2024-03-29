package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.SignupBusinessService;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.UUID;


@RestController
public class UserController {

    @Autowired
    private SignupBusinessService signupBusinessService;

    @Autowired
    private AuthenticationService authenticationService;


     @RequestMapping(method= RequestMethod.POST, path="/user/signup", consumes= MediaType.APPLICATION_JSON_UTF8_VALUE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
     public ResponseEntity<SignupUserResponse> userSignup(final SignupUserRequest signupUserRequest) throws SignUpRestrictedException {
        final UserEntity userEntity = new UserEntity();

        userEntity.setUuid(UUID.randomUUID().toString());
        userEntity.setFirstName(signupUserRequest.getFirstName());
        userEntity.setLastName(signupUserRequest.getLastName());
        userEntity.setUserName(signupUserRequest.getUserName());
        userEntity.setEmail(signupUserRequest.getEmailAddress());
        userEntity.setPassword(signupUserRequest.getPassword());
        userEntity.setContactNumber(signupUserRequest.getContactNumber());
        userEntity.setCountry(signupUserRequest.getCountry());
        userEntity.setAboutMe(signupUserRequest.getAboutMe());
        userEntity.setDob(signupUserRequest.getDob());
        userEntity.setSalt("1234abc");
        userEntity.setRole("nonadmin");

        final UserEntity createdUserEntity = signupBusinessService.signup(userEntity);
        SignupUserResponse userResponse = new SignupUserResponse().id(createdUserEntity.getUuid()).status("USER SUCCESSFULLY REGISTERED");

        return new ResponseEntity<SignupUserResponse>(userResponse,HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.POST , path="/user/signin" ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SigninResponse> login(@RequestHeader("authorization")  final String authorization) throws AuthenticationFailedException {
        byte[] decode = Base64.getDecoder().decode(authorization);
        String decodedText = new String(decode);
        String[] decodedArray = decodedText.split(":");

        UserAuthTokenEntity userAuthToken = authenticationService.authenticate(decodedArray[0] , decodedArray[1]);

        UserEntity user = userAuthToken.getUser();


        SigninResponse SigninUserResponse = new SigninResponse();


        SigninUserResponse.setId(user.getUuid());
        SigninUserResponse.setMessage("SIGNED IN SUCCESSFULLY");


        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token", userAuthToken.getAccessToken());

        return new ResponseEntity<SigninResponse>(SigninUserResponse,headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST , path="/user/signout" ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignoutResponse> logout(@RequestHeader("authorization")  final String accessToken) throws SignOutRestrictedException {

        UserAuthTokenEntity userAccessAuthToken = authenticationService.signout(accessToken);

        UserEntity user = userAccessAuthToken.getUser();


        SignoutResponse SignoutResponse = new SignoutResponse();


        SignoutResponse.setId(user.getUuid());
        SignoutResponse.setMessage("SIGNED OUT SUCCESSFULLY");


        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token", userAccessAuthToken.getAccessToken());

        return new ResponseEntity<SignoutResponse>(SignoutResponse,headers, HttpStatus.OK);
    }
}
