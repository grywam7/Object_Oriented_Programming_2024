package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapDirectionTest {
    @Test
    void nextNorth() {
        //given
        MapDirection northDirection = MapDirection.NORTH;

        //when then
        assertEquals(MapDirection.EAST, northDirection.next());
    }

    @Test
    void nextEast() {
        //given
        MapDirection eastDirection = MapDirection.EAST;

        //when then
        assertEquals(MapDirection.SOUTH, eastDirection.next());
    }

    @Test
    void nextSouth() {
        //given
        MapDirection southDirection = MapDirection.SOUTH;

        //when then
        assertEquals(MapDirection.WEST, southDirection.next());
    }

    @Test
    void nextWest() {
        //given
        MapDirection westDirection = MapDirection.WEST;

        //when then
        assertEquals(MapDirection.NORTH, westDirection.next());
    }

    @Test
    void previousNorth() {
        //given
        MapDirection northDirection = MapDirection.NORTH;

        //when then
        assertEquals(MapDirection.WEST, northDirection.previous());
    }

    @Test
    void previousEast() {
        //given
        MapDirection eastDirection = MapDirection.EAST;

        //when then
        assertEquals(MapDirection.NORTH, eastDirection.previous());
    }

    @Test
    void previousSouth() {
        //given
        MapDirection southDirection = MapDirection.SOUTH;

        //when then
        assertEquals(MapDirection.EAST, southDirection.previous());
    }

    @Test
    void previousWest() {
        //given
        MapDirection westDirection = MapDirection.WEST;

        //when then
        assertEquals(MapDirection.SOUTH, westDirection.previous());
    }
}