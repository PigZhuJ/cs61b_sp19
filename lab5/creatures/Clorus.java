package creatures;

import huglife.Creature;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

import static huglife.HugLifeUtils.randomEntry;

public class Clorus extends Creature {

    /**
     * red color.
     */
    private int r;
    /**
     * green color.
     */
    private int g;
    /**
     * blue color.
     */
    private int b;
    /**
     * creates clorus with energy equal to e
     * and default colors.
     */
    public Clorus(double e) {
        super("clorus");
        r = 34;
        g = 0;
        b = 231;
        energy = e;
    }

    /**
     * creates a clorus with energy equal to 1.
     */
    public Clorus() {
        this(1);
    }

    @Override
    public String name() {
        return super.name();
    }

    @Override
    public Color color() {
        return color(r, g, b);
    }

    @Override
    public void move() {
        energy -= 0.03;
        if (energy < 0) {
            energy = 0;
        }
    }

    @Override
    public void stay() {
        energy -= 0.01;
        if (energy < 0) {
            energy = 0;
        }
    }

    @Override
    public Clorus replicate() {
        energy = 0.5 * energy;
        Clorus babyClorus = new Clorus(energy);
        return babyClorus;
    }

    @Override
    public void attack(Creature c) {
        energy += c.energy();
    }

    @Override
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        Deque<Direction> emptyNeighbors = new ArrayDeque<>();
        Deque<Direction> plipNeighbors = new ArrayDeque<>();
        // Rule 1: If there are no empty squares, the Clorus will STAY
        // (even if there are Plips nearby they could attack since plip
        // squares do not count as empty squares).
        for (Direction key : neighbors.keySet()) {
            if (neighbors.get(key).name().equals("empty")) {
                emptyNeighbors.add(key);
            } else if (neighbors.get(key).name().equals("plip")) {
                plipNeighbors.add(key);
            }
        }
        if (emptyNeighbors.size() == 0) {
            return new Action(Action.ActionType.STAY);
        } else if (plipNeighbors.size() > 0) {
            // Rule 2: Otherwise, if any Plips are seen, the Clorus will ATTACK
            // one of them randomly.
            return new Action(Action.ActionType.ATTACK, randomEntry(plipNeighbors));
        } else if (energy >= 1.0) {
            // Rule 3: Otherwise, if the Clorus has energy greater than or equal to one,
            // it will REPLICATE to a random empty square.
            return new Action(Action.ActionType.REPLICATE, randomEntry(emptyNeighbors));
        } else {
            // Rule 4: Otherwise, the Clorus will MOVE to a random empty square.
            return new Action(Action.ActionType.MOVE, randomEntry(emptyNeighbors));
        }

    }

}
