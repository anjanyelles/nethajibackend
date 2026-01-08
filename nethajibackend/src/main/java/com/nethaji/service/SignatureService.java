package com.nethaji.service;





public interface SignatureService {

    String hashPassword(String password, String salt) throws RuntimeException;

}
