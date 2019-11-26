package com.mainacad.dao;

import com.mainacad.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    @Test
    void save() {
        User user = new User("login", "password", "firstname",
                "lastname", "email", "+4353456363464");
        UserDAO.save(user);
        assertNotNull(user.getId());
    }

    @Test
    void update() {
        User user = new User("login1", "password1", "firstname1",
                "lastname1", "email1", "phone1");
        UserDAO.save(user);

        user.setLogin("login_update");
        user.setPassword("password_update");
        user.setFirstName("firstname_update");
        user.setLastName("lastname_update");
        user.setEmail("email_update");
        user.setPhone("phone_update");

        UserDAO.update(user);
    }

    @Test
    void delete() {
        User user = new User("login2", "password2", "firstname2",
                "lastname2", "email2", "phone2");
        UserDAO.save(user);
        Integer id = user.getId();
        UserDAO.delete(id);
    }

    @Test
    void getById() {
        User user = new User("login3", "password3", "firstname3",
                "lastname3", "email3", "phone3");
        UserDAO.save(user);
        Integer id = user.getId();

        User newUser = new User();
        newUser = UserDAO.getById(id);

        assertEquals(id, newUser.getId());
        assertEquals("login3", newUser.getLogin());
        assertEquals("password3", newUser.getPassword());
        assertEquals("firstname3", newUser.getFirstName());
        assertEquals("lastname3", newUser.getLastName());
        assertEquals("email3", newUser.getEmail());
        assertEquals("phone3", newUser.getPhone());
    }

}