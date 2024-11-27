package agh.ics.oop.model;


import agh.ics.oop.model.util.MapVisualizer;

import java.util.Collections;
import java.util.Map;

public class RectangularMap extends AbstractWorldMap {
    private final Vector2d rightTopMapCorner;
    private final Vector2d leftBottomMapCorner = new Vector2d(0,0);

    public RectangularMap(int width, int height){
        rightTopMapCorner = new Vector2d(width-1, height-1);
    }

    @Override
    public boolean canMoveTo(Vector2d vectorToCheck) {
        return vectorToCheck.precedes(rightTopMapCorner) && vectorToCheck.follows(leftBottomMapCorner) && super.canMoveTo(vectorToCheck);
    }

    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(leftBottomMapCorner, rightTopMapCorner);
    }
}

//co powienna zaweirac klasa abstrakcyjna
// co z metodami ktore maja jakas czesc wwspolna ale nie jednakowa?
// czy wspolrzedne rogow grassfield miozna i wazrto zapamietac? (czy mozna rogi z poprzedniego ywyolania zapamietac)
// czy mozemy zmodyfikowac interfejs ktory dostalismy. raczej nie bo cos innego mozemy popsuc
// nie robimy geteow do kolekcji
// punk 11 instrukcji @2up (mamy zmodyfikować jak kazą)
// w losowosci seed?
// testowac czy jest n traw w danym zakresie


