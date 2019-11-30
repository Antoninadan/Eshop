package com.mainacad.dao;

import com.mainacad.model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    private static List<User> users;

    @BeforeAll
    static void setPreConditions() {
        users = new ArrayList<>();
    }

    @Test
    void save() {
        User user = new User("testLogin", "testPass", "testName", "testLastName", "testEmail", "123456789");
        UserDAO.save(user);
        users.add(user);
        assertNotNull(user.getId());
    }

    @Test
    void update() {
        User user = new User("testLogin", "testPass", "testName", "testLastName", "testEmail", "123456789");
        User savedUser = UserDAO.save(user);
        users.add(savedUser);
        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals("testPass", savedUser.getPassword());

        user.setPassword("newPass");

        User updatedUser = UserDAO.update(user);
        assertNotNull(updatedUser);
        assertEquals("newPass", updatedUser.getPassword());
    }

    @Test
    void getAndDelete() {
        User user = new User("testLogin", "testPass", "testName", "testLastName", "testEmail", "123456789");
        UserDAO.save(user);

        assertNotNull(user.getId());

        User targetUser = UserDAO.getById(user.getId());
        assertNotNull(targetUser);
        UserDAO.delete(targetUser.getId());
        targetUser = UserDAO.getById(user.getId());
        assertNull(targetUser);
    }


    @AfterAll
    static void deleteTestData() {
        users.forEach(it -> UserDAO.delete(it.getId()));
    }
}