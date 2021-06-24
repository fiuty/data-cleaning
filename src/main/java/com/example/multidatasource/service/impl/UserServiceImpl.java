package com.example.multidatasource.service.impl;

import com.example.multidatasource.common.annotation.DataSource;
import com.example.multidatasource.common.enums.DataSourcesType;
import com.example.multidatasource.domain.User;
import com.example.multidatasource.domain.repository.UserRepository;
import com.example.multidatasource.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @DataSource(name = DataSourcesType.MASTER)
    public List<User> listMaster() {
        return userRepository.findAll();
    }

    @Override
    @DataSource(name = DataSourcesType.SLAVE)
    public List<User> listSlave() {
        return userRepository.findAll();
    }
}
