package com.example.multidatasource.service;

import com.example.multidatasource.domain.User;

import java.util.List;

public interface UserService {

    List<User> listMaster();

    List<User> listSlave();
}
