package rollingSystem;

import rollingSystem.Dice.DiceSet;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import rollingSystem.Dice.Dice;
import rollingSystem.Dice.Die;

public class Utilities {

    public static int roll(DiceSet diceSet) {
        int sum = 0;
        for (Dice dice : diceSet.getDice()) {
            for (Die die : dice.getDice()) {
                sum += die.rollDie();
            }
        }
        return sum;
    }
    public static int rollDropN(DiceSet diceSet, int drop, boolean greatest) {
        List<Integer> rollResults = new ArrayList<Integer>();
        for (Dice dice : diceSet.getDice()) {
            for (Die die : dice.getDice()) {
                rollResults.add(die.rollDie());
            }
        }
        if (drop > rollResults.size()) {
            throw new RuntimeException("Cannot drop more dice than were rolled!");
        }
        rollResults.sort((Integer o1, Integer o2) -> {
                return !greatest
                    ? o1 > o2 ? -1 : o1 < o2 ? 1 : 0 // drop least
                    : o1 > o2 ? 1 : o1 < o2 ? -1 : 0; // drop greatest
            }
        );
        for (; drop > 0; drop--) {
            rollResults.remove(rollResults.size() - 1);
        }
        return sumList(rollResults);
    }
    public static int rollDropIf(DiceSet diceSet, Predicate<? super Integer> fn) {
        List<Integer> rollResults = new ArrayList<Integer>();
        for (Dice dice : diceSet.getDice()) {
            for (Die die : dice.getDice()) {
                rollResults.add(die.rollDie());
            }
        }
        rollResults.removeIf(fn);
        return sumList(rollResults);
    }
    public static int rollRerollN(DiceSet diceSet, int reroll, boolean greatest) {
        List<ResultAndDie> rollResults = new ArrayList<ResultAndDie>();
        for (Dice dice : diceSet.getDice()) {
            for (Die die : dice.getDice()) {
                rollResults.add(new ResultAndDie(die.rollDie(), die));
            }
        }
        if (reroll > rollResults.size()) {
            throw new RuntimeException("Cannot drop more dice than were rolled! " + "reroll: " + reroll + " greatest: " + (greatest ? 1 : 0));
        }
        rollResults.sort((ResultAndDie o1, ResultAndDie o2) -> {
                return greatest
                    ? o1.result > o2.result ? -1 : o1.result < o2.result ? 1 : 0 // reroll greatest
                    : o1.result > o2.result ? 1 : o1.result < o2.result ? -1 : 0; // reroll least
            }
        );
        int sum = 0;
        for (int i = 0; i < reroll; i++) {
            int newRoll = rollResults.get(i).die.rollDie();
            rollResults.set(i, new ResultAndDie(sum, rollResults.get(i).die));
            sum += newRoll;
        }
        for (int i = reroll; i < rollResults.size(); i++) {
            sum += rollResults.get(i).result;
        }
        return sum;
    }
    public static int rollRerollIf(DiceSet diceSet, Predicate<? super Integer> fn) {
        List<ResultAndDie> rollResults = new ArrayList<ResultAndDie>();
        for (Dice dice : diceSet.getDice()) {
            for (Die die : dice.getDice()) {
                rollResults.add(new ResultAndDie(die.rollDie(), die));
            }
        }
        rollResults.forEach((dieObj) -> {
            boolean a = fn.test(dieObj.result);
            if (a) {
                dieObj.result = dieObj.die.rollDie();
            }
        });
        int sum = 0;
        for (int i = 0; i < rollResults.size(); i++) {
            sum += rollResults.get(i).result;
        }
        return sum;
    }

    public static ArrayList<Integer> createPointBuyResultSet(int[] values) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (Integer val : values)
            result.add(val);
        return result;
    }

    private static int sumList(List<Integer> list) {
        int sum = 0;
        for (int elem : list) {
            sum += elem;
        }
        return sum;
    }
}

class ResultAndDie {
    public int result;
    public Die die;
    public ResultAndDie(int result, Die die) {
        this.result = result;
        this.die = die;
    }
}