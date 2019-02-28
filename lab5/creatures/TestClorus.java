package creatures;
import creatures.Clorus;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.awt.Color;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.Impassible;
import huglife.Empty;

/** Tests the clorus class
 *  @author zangsy
 */

public class TestClorus {

    @Test
    public void testBasics() {
        Clorus c = new Clorus(2);
        Plip p = new Plip(2);
        assertEquals(2, c.energy(), 0.01);
        assertEquals(new Color(34, 0, 231), c.color());
        c.move();
        assertEquals(1.97, c.energy(), 0.01);
        c.move();
        assertEquals(1.94, c.energy(), 0.01);
        c.stay();
        assertEquals(1.93, c.energy(), 0.01);
        c.stay();
        assertEquals(1.92, c.energy(), 0.01);
        c.attack(p);
        assertEquals(3.92, c.energy(), 0.01);
    }

    @Test
    public void testReplicate() {
        Clorus c = new Clorus(2);
        Clorus babyC = c.replicate();
        assertEquals(1, c.energy(), 0.01);
        assertEquals(1, babyC.energy(), 0.01);
        assertFalse(c == babyC);
    }

    @Test
    public void testChoose() {

        // No empty adjacent spaces; stay.
        Clorus c = new Clorus(1.2);
        HashMap<Direction, Occupant> surrounded = new HashMap<Direction, Occupant>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        Action actual = c.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);


        // No empty adjacent spaces; stay.
        c = new Clorus(1.2);
        HashMap<Direction, Occupant> allPlips = new HashMap<Direction, Occupant>();
        allPlips.put(Direction.TOP, new Plip());
        allPlips.put(Direction.BOTTOM, new Plip());
        allPlips.put(Direction.LEFT, new Plip());
        allPlips.put(Direction.RIGHT, new Plip());

        actual = c.chooseAction(allPlips);
        expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);


        // Plips are seen, attack one of them randomly.
        c = new Clorus(1.2);
        HashMap<Direction, Occupant> hasPlip = new HashMap<Direction, Occupant>();
        hasPlip.put(Direction.TOP, new Plip());
        hasPlip.put(Direction.BOTTOM, new Impassible());
        hasPlip.put(Direction.LEFT, new Empty());
        hasPlip.put(Direction.RIGHT, new Empty());

        actual = c.chooseAction(hasPlip);
        expected = new Action(Action.ActionType.ATTACK, Direction.TOP);

        assertEquals(expected, actual);


        // Energy >= 1; replicate towards an empty space.
        c = new Clorus(1.2);
        HashMap<Direction, Occupant> topEmpty = new HashMap<Direction, Occupant>();
        topEmpty.put(Direction.TOP, new Empty());
        topEmpty.put(Direction.BOTTOM, new Impassible());
        topEmpty.put(Direction.LEFT, new Impassible());
        topEmpty.put(Direction.RIGHT, new Impassible());

        actual = c.chooseAction(topEmpty);
        expected = new Action(Action.ActionType.REPLICATE, Direction.TOP);

        assertEquals(expected, actual);


        // Energy >= 1; replicate towards an empty space.
        c = new Clorus(1.2);
        HashMap<Direction, Occupant> allEmpty = new HashMap<Direction, Occupant>();
        allEmpty.put(Direction.TOP, new Empty());
        allEmpty.put(Direction.BOTTOM, new Empty());
        allEmpty.put(Direction.LEFT, new Empty());
        allEmpty.put(Direction.RIGHT, new Empty());

        actual = c.chooseAction(allEmpty);
        Action unexpected = new Action(Action.ActionType.STAY);

        assertNotEquals(unexpected, actual);

    }


}
