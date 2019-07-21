package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class UserDao {


    @PersistenceContext
    private EntityManager entityManager;

    public UserEntity createUser(UserEntity userEntity) throws SignUpRestrictedException {



        TypedQuery<UserEntity> query=entityManager.createNamedQuery("getAllUsers",UserEntity.class);
        List<UserEntity> resultList = query.getResultList();

        for (UserEntity user:resultList ) {

            if(user.getEmail().equals(userEntity.getEmail())){
                throw new SignUpRestrictedException("SGR-002","This user has already been registered, try with any other emailId");
            } else if(user.getUserName().equals(userEntity.getUserName())){
                throw new SignUpRestrictedException("SGR-001","Try any other Username, this Username has already been taken");
            }

        }

        entityManager.persist(userEntity);

        return  userEntity;
    }

    public UserEntity getUserByUsername(final String userName) {
        try {
            return entityManager.createNamedQuery("userByUname" , UserEntity.class).setParameter("username" , userName).getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    public UserEntity getUserById(final Integer userId) {
        try {
            return entityManager.createNamedQuery("userById", UserEntity.class).setParameter("id", userId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public void deleteUserById(final Integer userId) {

        UserEntity userEntity;
        try {

            entityManager.createNamedQuery("deleteUserById", UserEntity.class)
                    .setParameter(1, userId)
                    .executeUpdate();

            //Execute the delete query
            entityManager.flush();

        }
        catch(Exception e) {
            e.printStackTrace();
        }


    }

    public UserAuthTokenEntity createAuthToken(final UserAuthTokenEntity userAuthTokenEntity) {
        entityManager.persist(userAuthTokenEntity);
        return userAuthTokenEntity;
    }

    public void updateUser(final UserEntity updatedUserEntity) {
        entityManager.merge(updatedUserEntity);
    }
}
