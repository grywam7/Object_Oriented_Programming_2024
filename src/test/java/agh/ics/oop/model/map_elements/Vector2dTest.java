package agh.ics.oop.model.map_elements;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector2dTest {

    @Test
    void testConstructorAndGetters() {
        Vector2d vec = new Vector2d(3, 5);
        assertEquals(3, vec.getX());
        assertEquals(5, vec.getY());
    }

    @Test
    void testToString() {
        Vector2d vec = new Vector2d(1, 2);
        assertEquals("(1,2)", vec.toString());
    }

    @Test
    void testAdd() {
        Vector2d v1 = new Vector2d(1, 2);
        Vector2d v2 = new Vector2d(3, 4);
        Vector2d result = v1.add(v2);

        assertEquals(new Vector2d(4, 6), result);
    }

    @Test
    void testSubtract() {
        Vector2d v1 = new Vector2d(5, 7);
        Vector2d v2 = new Vector2d(3, 4);
        Vector2d result = v1.subtract(v2);

        assertEquals(new Vector2d(2, 3), result);
    }

    @Test
    void testEquals() {
        Vector2d v1 = new Vector2d(2, 3);
        Vector2d v2 = new Vector2d(2, 3);
        Vector2d v3 = new Vector2d(3, 2);

        assertEquals(v1, v2);
        assertNotEquals(v1, v3);
        assertNotEquals(null, v1);
        assertNotEquals(v1, new Object());
    }

    @Test
    void testHashCode() {
        Vector2d v1 = new Vector2d(2, 3);
        Vector2d v2 = new Vector2d(2, 3);

        // Equal objects should produce the same hash code
        assertEquals(v1.hashCode(), v2.hashCode());
    }
}
