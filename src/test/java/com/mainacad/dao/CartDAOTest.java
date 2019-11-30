package com.mainacad.dao;

import com.mainacad.model.Cart;
import com.mainacad.model.Status;
import com.mainacad.model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CartDAOTest {

    private static final Long CURRENT_TIME = new Date().getTime();
    private static List<Cart> carts;
    private static List<User> users;

    @BeforeAll
    static void setUp() {
        carts = new ArrayList<>();
        users = new ArrayList<>();
    }

    @AfterAll
    static void tearDown() {
//        carts.forEach(it -> CartDAO.delete(it.getId()));
//        users.forEach(it -> UserDAO.delete(it.getId()));
    }

    @Test
    void getById() {
        User user = new User("testLogin", "testPass", "testName", "testLastName", "testEmail", "123456789");
        UserDAO.save(user);
        users.add(user);
        assertNotNull(user.getId());

        Cart cart = new Cart(Status.OPEN, user, CURRENT_TIME);
        CartDAO.save(cart);
        carts.add(cart);
        assertNotNull(cart.getId());

        Cart savedCart = CartDAO.getById(cart.getId());
        assertNotNull(savedCart);
        assertNotNull(savedCart.getUser());
        assertNotNull(savedCart.getUser().getId());
    }
}