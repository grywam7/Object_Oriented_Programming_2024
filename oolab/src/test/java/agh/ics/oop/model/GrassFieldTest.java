package agh.ics.oop.model;

import agh.ics.oop.model.exceptions.IncorrectPositionException;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GrassFieldTest {

    @Test
    public void testAmountOfGrassGenerated() {
        //given
        int grassCount = 10;

        assertDoesNotThrow(() -> {
            //when
            GrassField grassField = new GrassField(grassCount);

            //then
            assertEquals(grassCount, grassField.getGrasses().size());
        });
    }

    @Test
    public void testGrassUniqueness() {
        //given
        int grassCount = 10;

        assertDoesNotThrow(() -> {
            //when
            GrassField grassField = new GrassField(grassCount);
            Set<Vector2d> grassPositions = new HashSet<>(grassField.getGrasses().keySet());

            //then
            assertEquals(grassCount, grassPositions.size());
        });
    }

    @Test
    public void testPlacingTwoAnimalsInTheSamePosition() {
        assertDoesNotThrow(() -> {
            //given
            GrassField grassField = new GrassField(0);
            Animal animal1 = new Animal();
            Animal animal2 = new Animal();

            //when then
            assertDoesNotThrow(() -> {grassField.place(animal1);});
            assertThrows(IncorrectPositionException.class, () -> {
                grassField.place(animal2);
            });
        });
    }

    @Test
    public void testIFAnimalsArePlacedInCorrectPosition() {
        assertDoesNotThrow(() -> {
            //given
            GrassField grassField = new GrassField(0);
            Animal animal1 = new Animal();

            //when
            grassField.place(animal1);

            //then
            assertEquals(animal1, grassField.objectAt(new Vector2d(2, 2)));
        });
    }

    @Test
    public void testAnimalMovementForward() {
        assertDoesNotThrow(() -> {
            //given
            GrassField grassField = new GrassField(0);
            Animal animal = new Animal();

            //when
            grassField.place(animal);
            grassField.move(animal, MoveDirection.FORWARD);

            //then
            assertEquals(new Vector2d(2, 3), animal.getPosition());
            grassField.move(animal, MoveDirection.RIGHT);
            grassField.move(animal, MoveDirection.FORWARD);
            assertEquals(new Vector2d(3, 3), animal.getPosition());
        });
    }

    @Test
    public void testAnimalMovementRightAndForward() {
        assertDoesNotThrow(() -> {
            //given
            GrassField grassField = new GrassField(0);
            Animal animal = new Animal();

            //when
            grassField.place(animal);
            grassField.move(animal, MoveDirection.RIGHT);
            grassField.move(animal, MoveDirection.FORWARD);
            assertEquals(new Vector2d(3, 2), animal.getPosition());
        });
    }

    @Test
    public void testToStringWithOneAnimal() {
        assertDoesNotThrow(() -> {
            //given
            GrassField grassField = new GrassField(0);
            Animal animal = new Animal();

            //when
            grassField.place(animal);

            //when
            assertTrue(grassField.toString().contains("N"));
        });
    }


    @Test
    public void testToStringWithOneGrass() {
        assertDoesNotThrow(() -> {
            //given
            GrassField grassField = new GrassField(1);

            //when then
            assertTrue(grassField.toString().contains("*"));
        });
    }

    @Test
    public void testToStringWithAnimalAndGrass() {
        assertDoesNotThrow(() -> {
            //given
            GrassField grassField = new GrassField(1);
            Animal animal = new Animal(new Vector2d(10, 10));

            //when
            grassField.place(animal);
            String mapString = grassField.toString();

            //then
            assertTrue(mapString.contains("N"));
            assertTrue(mapString.contains("*"));
        });
    }
}