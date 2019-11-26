package com.mainacad.dao;

import com.mainacad.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    @Test
    void save() {
        User user = new User("login", "password", "fisrtname",
                "lastname", "email", "+4353456363464");
        UserDAO.save(user);
        assertNotNull(user.getId());
    }
}