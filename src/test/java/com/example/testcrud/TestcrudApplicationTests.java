package com.example.testcrud;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TestcrudApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {

        assertThat(context).isNotNull();

    }

    @Test
    void testUserServiceBeanExists() {

        assertThat(context.getBean("userService")).isNotNull();

    }

    @Test
    void testUserRepositoryBeanExists() {

        assertThat(context.getBean("userRepository")).isNotNull();

    }

    @Test
    void testJwtUtilBeanExists() {

        assertThat(context.getBean("jwtUtil")).isNotNull();

    }
}
