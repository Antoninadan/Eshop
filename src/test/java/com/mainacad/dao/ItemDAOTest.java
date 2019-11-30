package com.mainacad.dao;

import com.mainacad.model.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemDAOTest {
    List<Item> items;

    @BeforeEach
    void setUp() {
        items = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        items.forEach(it -> ItemDAO.delete(it.getId()));
    }

    @Test
    void save() {
        Item item = new Item("name_0", "code_0", 1, 10);
        items.add(item);

        ItemDAO.save(item);
        assertNotNull(item.getId());
    }

    @Test
    void getAll() {
        Item item1 = new Item("name_1", "code_1", 10, 100);
        Item item2 = new Item("name_2", "code_2", 20, 200);
        items.add(item1);
        items.add(item2);
        ItemDAO.save(item1);
        ItemDAO.save(item2);

        List<Item> targetItems = ItemDAO.getAll();
        assertTrue(targetItems.size() >= 2);

        int count = 0;
        for (Item each:targetItems){
            if (item1.getId() == each.getId()) {count++;}
            if (item2.getId() == each.getId()) {count++;}
        }
        assertEquals(2,count);
    }

    @Test
    void getAllAvailable() {
        Item item3 = new Item("name_3", "code_3", 30, 300);
        Item item4 = new Item("name_4", "code_4", 40, 0);
        items.add(item3);
        items.add(item4);
        ItemDAO.save(item3);
        ItemDAO.save(item4);

        List<Item> targetItems = ItemDAO.getAllAvailable();
        assertTrue(targetItems.size() >= 1);

        boolean isInCollectionItem3 = false;
        boolean isInCollectionItem4 = false;
        for (Item each:targetItems){
            if (item3.getId() == each.getId()) {isInCollectionItem3 = true;}
            if (item4.getId() == each.getId()) {isInCollectionItem4 = true;}
        }
        assertTrue(isInCollectionItem3);
        assertTrue(!isInCollectionItem4);
    }

    @Test
    void getById() {
        Item item = new Item("name_5", "code_5", 50, 500);
        ItemDAO.save(item);
        items.add(item);
        assertNotNull(item.getId());

        Item targetItem = ItemDAO.getById(item.getId());
        assertNotNull(targetItem);
        assertAll("Should equal all fields",
                ()->assertEquals(targetItem.getName(), item.getName()),
                ()->assertEquals(targetItem.getCode(), item.getCode()),
                ()->assertEquals(targetItem.getPrice(), item.getPrice()),
                ()->assertEquals(targetItem.getAvailability(), item.getAvailability()));
    }

    @Test
    void update() {
        Item item = new Item("name_6", "code_6", 60, 600);
        ItemDAO.save(item);
        items.add(item);
        assertNotNull(item.getId());

        item.setName("name_new");

        Item targetItem = ItemDAO.update(item);
        assertNotNull(targetItem);
        assertEquals("name_new", targetItem.getName());
    }

    @Test
    void getAndDelete() {
        Item item = new Item("name_7", "code_7", 70, 700);
        ItemDAO.save(item);
        items.add(item);
        assertNotNull(item.getId());

        Item targetItem = ItemDAO.getById(item.getId());
        assertNotNull(targetItem);
        ItemDAO.delete(targetItem.getId());
        targetItem = ItemDAO.getById(item.getId());
        assertNull(targetItem);
    }
}