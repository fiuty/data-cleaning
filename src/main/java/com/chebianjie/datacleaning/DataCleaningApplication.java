package com.chebianjie.datacleaning;

import com.chebianjie.datacleaning.repository.BaseRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
public class DataCleaningApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataCleaningApplication.class, args);
    }

}
