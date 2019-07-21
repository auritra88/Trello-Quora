package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserAuthTokenDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private UserAuthTokenDao userAuthTokenDao;

    @Autowired
    private UserDao userDao;

    public UserEntity getUser(final String id, final String authorization) throws UserNotFoundException, AuthorizationFailedException {

        UserAuthTokenEntity userAuthTokenEntity = userAuthTokenDao.getUserAuthTokenByAccessToken(authorization);
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if(userAuthTokenEntity.getLogoutAt()!=null ){
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get user details");
        } else {
            UserEntity userEntity=userDao.getUserById(Integer.parseInt(id));
            if(userEntity==null) {
                throw new UserNotFoundException("USR-001","User with entered uuid does not exist");
            } else {
                return userEntity;
            }

        }

    }

    public String deleteUser(final String id, final String authorization) throws UserNotFoundException, AuthorizationFailedException{

        UserAuthTokenEntity userAuthTokenEntity = userAuthTokenDao.getUserAuthTokenByAccessToken(authorization);
        String deletedUserUuid=userAuthTokenEntity.getUuid();
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if(userAuthTokenEntity.getLogoutAt()!=null ){
            throw new AuthorizationFailedException("ATHR-002", "User is signed out");
        } else {

            Long l = new Long(userAuthTokenEntity.getId());

            UserEntity userEntity=userDao.getUserById(l.intValue());

            if(userEntity==null) {
                throw new UserNotFoundException("USR-001","User with entered uuid does not exist");
            } else {
                if(userEntity.getRole().equals("admin")){

                    userDao.deleteUserById(Integer.parseInt(id));

                }else{

                    throw new AuthorizationFailedException("ATHR-003","Unauthorized Access, Entered user is not an admin");

                }
            }

        }

        return deletedUserUuid;

    }
}
