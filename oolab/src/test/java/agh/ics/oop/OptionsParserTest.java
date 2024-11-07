package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class OptionsParserTest {

    @Test
    void parseContaminatedString() {
        //given
        String[] contaminatedStrings = {"w","e","r","t","y","u","i","o","p","l","k","j","h","g","f","d","s","a","z","x",
                "c","v","b","n","m","qw","we","er","ty","","ui","op","as","df","gh","jk","kl","zx","cv","bn","nm","qaz",
                "wsx","edc","rfv","tgb","yhn","ujm","ik","o","lp","moze samo f bedzie dzialac",",",".",";","/","'","[",
                "]","=","-","0","9","8","7","6","5","4","3","2","1","!","@","#","$","%","^","&","*","(",")","_+","_",
                "+", "{","}","\"",":",">","?","<","|","'","\n","   "," kf "," f "};
        List<MoveDirection> expectedResponse = Arrays.asList(
                MoveDirection.RIGHT,
                MoveDirection.LEFT,
                MoveDirection.FORWARD,
                MoveDirection.BACKWARD
        );

        //when
        List<MoveDirection> parsedDirections = OptionsParser.parse(contaminatedStrings);

        //then
        assertEquals(expectedResponse, parsedDirections);
    }

    @Test
    void parseEmptyStrings() {
        //given
        String[] emptyStrings = {"", "  ", "   ", "\n "};

        //when then
        assertEquals(Collections.emptyList(), OptionsParser.parse(emptyStrings));
    }

    @Test
    void parseEmptyArray() {
        //given
        String[] emptyArray = {};

        //when then
        assertEquals(Collections.emptyList(), OptionsParser.parse(emptyArray));
    }
}