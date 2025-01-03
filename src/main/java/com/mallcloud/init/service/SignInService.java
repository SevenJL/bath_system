package com.mallcloud.init.service;

import com.mallcloud.init.model.entity.SignIn;

import java.util.List;

/**
 * @author Admin
 */
public interface SignInService {

    void signIn(int username) throws Exception;

    void completeSignIn(int username) throws Exception;

    List<SignIn> getAllData();
}
