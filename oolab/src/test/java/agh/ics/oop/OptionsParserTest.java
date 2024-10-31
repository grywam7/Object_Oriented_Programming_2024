package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptionsParserTest {

    @Test
    void parseContaminatedString() {
        //given
        String[] contaminatedStrings = {"w","e","r","t","y","u","i","o","p","l","k","j","h","g","f","d","s","a","z","x","c","v","b","n","m","qw","we","er","ty","","ui","op","as","df","gh","jk","kl","zx","cv","bn","nm","qaz","wsx","edc","rfv","tgb","yhn","ujm","ik","o","lp","moze samo f bedzie dzialac",",",".",";","/","'","[","]","=","-","0","9","8","7","6","5","4","3","2","1","!","@","#","$","%","^","&","*","(",")","_+","_","+","{","}","\"",":",">","?","<","|","'","\n","   "," kf "," f "};
        MoveDirection[] expectedResponse = {MoveDirection.RIGHT, MoveDirection.LEFT, MoveDirection.FORWARD, MoveDirection.BACKWARD};

        //when
        MoveDirection[] parsedDirections = OptionsParser.parse(contaminatedStrings);

        //then
        assertArrayEquals(expectedResponse, parsedDirections);
    }

    @Test
    void parseEmptyStrings() {
        //given
        String[] emptyStrings = {"", "  ", "   ", "\n "};

        //when then
        assertArrayEquals(new MoveDirection[] {}, OptionsParser.parse(emptyStrings));
    }

    @Test
    void parseEmptyArray() {
        //given
        String[] emptyArray = {};

        //when then
        assertArrayEquals(new MoveDirection[] {}, OptionsParser.parse(emptyArray));
    }
}