package agh.ics.oop.model.maps;

import agh.ics.oop.model.map_elements.*;


/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo, idzik
 */
public interface WorldMap {

    /**
     * Place an animal on the map.
     *
     * @param animal The animal to place on the map.
     * @return nothing if the animal was placed. The animal cannot be placed if the move is not valid.
     */
    void placeAnimal(Animal animal);

    /**
     * Moves an animal (if it is present on the map) according to specified direction.
     * If the move is not possible, this method has no effect.
     */
// albo po liczbie zamiast map directionvvv




    //String getID();
}