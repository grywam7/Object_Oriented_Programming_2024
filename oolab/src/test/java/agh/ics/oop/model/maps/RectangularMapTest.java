package agh.ics.oop.model.maps;

import static org.junit.jupiter.api.Assertions.*;

import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.map_elements.MoveDirection;
import agh.ics.oop.model.map_elements.Vector2d;
import agh.ics.oop.model.map_elements.Animal;
import org.junit.jupiter.api.Test;


class RectangularMapTest {

    @Test
    public void testAnimalPlacementOnMap() {
        //given
        RectangularMap map = new RectangularMap(5, 5);
        Animal animal = new Animal();

        //when then
        assertDoesNotThrow(() -> {map.place(animal);});
    }

    @Test
    public void testAnimalPlacementOutsideOfMap() {
        //given
        RectangularMap map = new RectangularMap(5, 5);
        Animal outOfMapAnimal = new Animal(new Vector2d(5, 5));
        String expectedMessage = "Position (5, 5) is not correct.";


        // when & then
        Exception exception = assertThrows(IncorrectPositionException.class, () -> {
            map.place(outOfMapAnimal);
        });
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testMovementOnTheMap() {
        //given
        RectangularMap map = new RectangularMap(5, 5);
        Animal animal = new Animal(new Vector2d(0, 0));
        assertDoesNotThrow(() -> {map.place(animal);});

        //when
        map.move(animal, MoveDirection.BACKWARD);

        //then
        assertEquals(new Vector2d(0, 0), animal.getPosition());
    }

    @Test
    public void testMovementOutsideTheMap() {
        //given
        RectangularMap map = new RectangularMap(5, 5);
        Animal animal = new Animal(new Vector2d(0, 0));
        assertDoesNotThrow(() -> {map.place(animal);});

        //when
        map.move(animal, MoveDirection.LEFT);
        map.move(animal, MoveDirection.FORWARD);

        //then
        assertEquals(new Vector2d(0, 0), animal.getPosition());
    }

    @Test
    public void testMovingIntoOccupiedPosition() {
        //given
        RectangularMap map = new RectangularMap(5, 5);
        Animal animal1 = new Animal();
        Animal animal3 = new Animal(new Vector2d(2, 3));
        assertDoesNotThrow(() -> {map.place(animal1);});
        assertDoesNotThrow(() -> {map.place(animal3);});

        //when
        map.move(animal3, MoveDirection.BACKWARD);

        //then
        assertEquals(new Vector2d(2, 3), animal3.getPosition());
    }

    @Test
    public void testPlacingAnimalsInTheSamePosition() {
        //given
        RectangularMap map = new RectangularMap(5, 5);
        Animal animal1 = new Animal();
        Animal animal2 = new Animal();

        //when then
        assertDoesNotThrow(() -> {map.place(animal1);});
        assertThrows(IncorrectPositionException.class, () -> {
            map.place(animal2);
        });
    }

    @Test
    public void testPlacingAnimals() {
        //given
        RectangularMap map = new RectangularMap(5, 5);
        Animal animal1 = new Animal();
        Animal animal3 = new Animal(new Vector2d(2, 3));
        assertDoesNotThrow(() -> {map.place(animal3);});
        assertDoesNotThrow(() -> {map.place(animal1);});

        //when then
        assertEquals(2, map.getAnimals().size());

        map.move(animal3, MoveDirection.BACKWARD);
        assertEquals(new Vector2d(2, 3), animal3.getPosition());
    }
}