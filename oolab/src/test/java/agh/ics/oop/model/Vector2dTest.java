package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector2dTest {

    @Test
    void equalTheSameVectors() {
        //given
        Vector2d vector = new Vector2d(1,2);

        //when then
        assertTrue(vector.equals(vector));
    }

    @Test
    void equalVectorsWithSameValue() {
        //given
        Vector2d vector = new Vector2d(1,2);
        Vector2d sameValueVector = new Vector2d(1, 2);

        //when then
        assertTrue(vector.equals(sameValueVector));
    }

    @Test
    void equalsDifferentVectors() {
        //given
        Vector2d vector = new Vector2d(1,2);
        Vector2d otherVector = new Vector2d(5, 5);

        //when then
        assertFalse(vector.equals(otherVector));
    }

    @Test
    void equalsVectorAndNull(){
        //given
        Vector2d vector = new Vector2d(1,2);

        //when then
        assertFalse(vector.equals(null));
    }

    @Test
    void toStringForPositiveVector() {
        //given
        Vector2d vector = new Vector2d(1,2);

        //when then
        assertEquals("(1,2)", vector.toString());
    }

    @Test
    void toStringForNegativeVector() {
        //given
        Vector2d vector = new Vector2d(-1, -2);

        //when then
        assertEquals("(-1,-2)", vector.toString());
    }

    @Test
    void precedesTooBigVector() {
        //given
        Vector2d vector = new Vector2d(5,5);
        Vector2d biggerVector = new Vector2d( 50, 50);

        //when then
        assertFalse(biggerVector.precedes(vector));
    }

    @Test
    void precedesEqualVectors() {
        //given
        Vector2d vector = new Vector2d(5,5);
        Vector2d equalVector = new Vector2d( 5, 5);

        //when then
        assertTrue(equalVector.precedes(vector));
    }

    @Test
    void precedesSmallerVector() {
        //given
        Vector2d vector = new Vector2d(5,5);
        Vector2d smallerVector = new Vector2d( 0, 0);

        //when then
        assertTrue(smallerVector.precedes(vector));
    }

    @Test
    void followsTooBigVector() {
        //given
        Vector2d vector = new Vector2d(5,5);
        Vector2d biggerVector = new Vector2d( 50, 50);

        //when then
        assertTrue(biggerVector.follows(vector));
    }

    @Test
    void followsEqualVectors() {
        //given
        Vector2d vector = new Vector2d(5,5);
        Vector2d equalVector = new Vector2d( 5, 5);

        //when then
        assertTrue(equalVector.follows(vector));
    }

    @Test
    void followsSmallerVector() {
        //given
        Vector2d vector = new Vector2d(5,5);
        Vector2d smallerVector = new Vector2d( 0, 0);

        //when then
        assertFalse(smallerVector.follows(vector));
    }

    @Test
    void upperRightPositiveVectors() {
        //given
        Vector2d vector = new Vector2d(10,89);
        Vector2d otherVector = new Vector2d(9,99);

        //when then
        assertEquals(new Vector2d(10,99), vector.upperRight(otherVector));
    }

    @Test
    void upperRightNegativeVectors() {
        //given
        Vector2d vector = new Vector2d(-10,-46);
        Vector2d otherVector = new Vector2d(-83,-33);

        //when then
        assertEquals(new Vector2d(-10,-33), vector.upperRight(otherVector));
    }

    @Test
    void upperRightMixedVectors() {
        //given
        Vector2d vector = new Vector2d(-10,0);
        Vector2d otherVector = new Vector2d(100,0);

        //when then
        assertEquals(new Vector2d(100,0), vector.upperRight(otherVector));
    }

    @Test
    void lowerLeftPositiveVectors() {
        //given
        Vector2d vector = new Vector2d(10,89);
        Vector2d otherVector = new Vector2d(9,99);

        //when then
        assertEquals(new Vector2d(9,89), vector.lowerLeft(otherVector));
    }

    @Test
    void lowerLeftNegativeVectors() {
        //given
        Vector2d vector = new Vector2d(-10,-46);
        Vector2d otherVector = new Vector2d(-83,-33);

        //when then
        assertEquals(new Vector2d(-83,-46), vector.lowerLeft(otherVector));
    }

    @Test
    void lowerLeftMixedVectors() {
        //given
        Vector2d vector = new Vector2d(-10,0);
        Vector2d otherVector = new Vector2d(100,0);

        //when then
        assertEquals(new Vector2d(-10,0), vector.lowerLeft(otherVector));
    }

    @Test
    void addPositiveVectors() {
        //given
        Vector2d vector = new Vector2d(10,89);
        Vector2d otherVector = new Vector2d(9,99);

        //when then
        assertEquals(new Vector2d(19,188), vector.add(otherVector));
    }

    @Test
    void addNegativeVectors() {
        //given
        Vector2d vector = new Vector2d(-10,-46);
        Vector2d otherVector = new Vector2d(-83,-33);

        //when then
        assertEquals(new Vector2d(-93,-79), vector.add(otherVector));
    }

    @Test
    void addMixedVectors() {
        //given
        Vector2d vector = new Vector2d(-10,0);
        Vector2d otherVector = new Vector2d(100,0);

        //when then
        assertEquals(new Vector2d(90,0), vector.add(otherVector));
    }

    @Test
    void subtractPositiveVectors() {
        //given
        Vector2d vector = new Vector2d(10,89);
        Vector2d otherVector = new Vector2d(9,99);

        //when then
        assertEquals(new Vector2d(1,-10), vector.subtract(otherVector));
    }

    @Test
    void subtractNegativeVectors() {
        //given
        Vector2d vector = new Vector2d(-10,-46);
        Vector2d otherVector = new Vector2d(-83,-33);

        //when then
        assertEquals(new Vector2d(73,-13), vector.subtract(otherVector));
    }

    @Test
    void subtractMixedVectors() {
        //given
        Vector2d vector = new Vector2d(-10,0);
        Vector2d otherVector = new Vector2d(100,0);

        //when then
        assertEquals(new Vector2d(-110,0), vector.subtract(otherVector));
    }

    @Test
    void oppositePositiveValues() {
        //given
        Vector2d vector = new Vector2d(10,89);

        //when then
        assertEquals(new Vector2d(-10,-89), vector.opposite());
    }

    @Test
    void oppositeNegativeValues() {
        //given
        Vector2d vector = new Vector2d(-10,-46);

        //when then
        assertEquals(new Vector2d(10,46), vector.opposite());
    }

    @Test
    void oppositeMixedValues() {
        //given
        Vector2d vector = new Vector2d(-10,0);

        //when then
        assertEquals(new Vector2d(10,0), vector.opposite());
    }
}