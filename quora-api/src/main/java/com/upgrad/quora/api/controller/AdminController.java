package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.service.business.AdminService;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;

    @RequestMapping(method= RequestMethod.DELETE, path="/admin/user/{userId}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDeleteResponse> userDelete(@PathVariable("userId") final String userId, @RequestHeader("authorization") final String authorization) throws UserNotFoundException,AuthorizationFailedException {
       // String delUuid=adminService.getUser()
        String deletedUuid=adminService.deleteUser(userId,authorization);

        UserDeleteResponse userDeleteResponse = new UserDeleteResponse();

        userDeleteResponse.setId(deletedUuid);
        userDeleteResponse.setStatus("USER SUCCESSFULLY DELETED");

        return new ResponseEntity<UserDeleteResponse>(userDeleteResponse, HttpStatus.OK);
    }
}
