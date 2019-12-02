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

import static org.junit.jupiter.api.Assertions.*;

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
        carts.forEach(it -> CartDAO.delete(it.getId()));
        users.forEach(it -> UserDAO.delete(it.getId()));
    }

    @Test
    void save() {
        User user = new User("login0", "pass0", "first_name0", "last_name0", "email0", "phone0");
        UserDAO.save(user);
        users.add(user);
        assertNotNull(user.getId());

        Cart cart = new Cart(Status.OPEN, user, CURRENT_TIME);
        CartDAO.save(cart);
        carts.add(cart);
        assertNotNull(cart.getId());
    }

    @Test
    void getAllByUserAndPeriod() {
        User userOk = new User("login2", "pass2", "first_name2", "last_name2", "email2", "phone2");
        User userNotOk = new User("login2", "pass2", "first_name2", "last_name2", "email2", "phone2");
        UserDAO.save(userOk);
        UserDAO.save(userNotOk);
        users.add(userOk);
        users.add(userNotOk);
        assertNotNull(userOk.getId());
        assertNotNull(userNotOk.getId());

        Long periodFrom = CURRENT_TIME - 100;
        Long periodTo = CURRENT_TIME;
        Long timeOk = CURRENT_TIME - 50;
        Long timeNotOk = CURRENT_TIME - 200;

        Cart cartOk = new Cart(Status.OPEN, userOk, timeOk);
        Cart cartNotOk1 = new Cart(Status.OPEN, userOk, timeNotOk);
        Cart cartNotOk2 = new Cart(Status.OPEN, userNotOk, timeOk);

        CartDAO.save(cartOk);
        CartDAO.save(cartNotOk1);
        CartDAO.save(cartNotOk2);
        carts.add(cartOk);
        carts.add(cartNotOk1);
        carts.add(cartNotOk2);
        assertNotNull(cartOk.getId());
        assertNotNull(cartNotOk1.getId());
        assertNotNull(cartNotOk2.getId());

        List<Cart> targetCarts = CartDAO.getAllByUserAndPeriod(userOk, periodFrom, periodTo);
        assertTrue(targetCarts.size() >= 1);

        boolean isInCollectionCartOk = false;
        boolean isInCollectionCartNotOk1 = false;
        boolean isInCollectionCartNotOk2 = false;

        for (Cart each : targetCarts) {
            if (cartOk.getId() == each.getId()) {
                isInCollectionCartOk = true;
            }
            if (cartNotOk1.getId() == each.getId()) {
                isInCollectionCartNotOk1 = true;
            }
            if (cartNotOk2.getId() == each.getId()) {
                isInCollectionCartNotOk2 = true;
            }
        }
        assertTrue(isInCollectionCartOk);
        assertTrue(!isInCollectionCartNotOk1);
        assertTrue(!isInCollectionCartNotOk2);
    }

    @Test
    void getById() {
        User user = new User("login2", "pass2", "first_name2", "last_name2", "email2", "phone2");
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

    @Test
    void getByUserAndOpenStatus() {
        User userOk = new User("login2", "pass2", "first_name2", "last_name2", "email2", "phone2");
        User userNotOk = new User("login2", "pass2", "first_name2", "last_name2", "email2", "phone2");
        UserDAO.save(userOk);
        UserDAO.save(userNotOk);
        users.add(userOk);
        users.add(userNotOk);
        assertNotNull(userOk.getId());
        assertNotNull(userNotOk.getId());

        Cart cartOk = new Cart(Status.OPEN, userOk, CURRENT_TIME);
        Cart cartNotOk1 = new Cart(Status.CLOSED, userOk, CURRENT_TIME);
        Cart cartNotOk2 = new Cart(Status.OPEN, userNotOk, CURRENT_TIME);

        CartDAO.save(cartOk);
        CartDAO.save(cartNotOk1);
        CartDAO.save(cartNotOk2);
        carts.add(cartOk);
        carts.add(cartNotOk1);
        carts.add(cartNotOk2);
        assertNotNull(cartOk.getId());
        assertNotNull(cartNotOk1.getId());
        assertNotNull(cartNotOk2.getId());

        Cart targetCart = CartDAO.getByUserAndOpenStatus(userOk);
        assertNotNull(targetCart);
        assertNotNull(targetCart.getUser());
        assertNotNull(targetCart.getUser().getId());
        assertEquals(targetCart.getUser().getId(), userOk.getId());
        assertEquals(targetCart.getId(), cartOk.getId());
    }

    @Test
    void updateStatus() {
        User user = new User("login0", "pass0", "first_name0", "last_name0", "email0", "phone0");
        UserDAO.save(user);
        users.add(user);
        assertNotNull(user.getId());

        Cart cart = new Cart(Status.OPEN, user, CURRENT_TIME);
        CartDAO.save(cart);
        carts.add(cart);
        assertNotNull(cart.getId());

        cart.setStatus(Status.CLOSED);

        Cart targetCart = CartDAO.updateStatus(cart, Status.CLOSED);
        assertNotNull(targetCart);
        assertEquals(Status.CLOSED, targetCart.getStatus());
    }

    @Test
    void getAndDelete() {
        User user = new User("login0", "pass0", "first_name0", "last_name0", "email0", "phone0");
        UserDAO.save(user);
        users.add(user);
        assertNotNull(user.getId());

        Cart cart = new Cart(Status.OPEN, user, CURRENT_TIME);
        CartDAO.save(cart);
        carts.add(cart);
        assertNotNull(cart.getId());

        Cart targetCart = CartDAO.getById(cart.getId());
        assertNotNull(targetCart);

        CartDAO.delete(cart.getId());

        Cart deletedCart = CartDAO.getById(targetCart.getId());
        assertNull(deletedCart);
    }
}