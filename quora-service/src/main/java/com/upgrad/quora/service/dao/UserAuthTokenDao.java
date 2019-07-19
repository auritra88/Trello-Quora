package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserAuthTokenDao {

    @PersistenceContext
    private EntityManager entityManager;

    public UserAuthTokenEntity getUserAuthTokenByAccessToken(final String accessToken) {
        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken",UserAuthTokenEntity.class).setParameter("accessToken" , accessToken).getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    public void updateUserAuthTokenEntity(final UserAuthTokenEntity updatedUserAuthTokenEntity) {
        entityManager.merge(updatedUserAuthTokenEntity);
    }




}
