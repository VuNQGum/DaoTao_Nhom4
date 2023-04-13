package com.hust.daotao.controller;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.metadata.HikariDataSourcePoolMetadata;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private DataSource dataSource;
    @GetMapping("/helloworld")
    public String helloWorld() {
        Integer idleConnection = new HikariDataSourcePoolMetadata((HikariDataSource) dataSource).getIdle();
        return "Hello World" + idleConnection;
    }
}
