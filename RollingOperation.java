import java.util.function.Predicate;

import rollingSystem.Dice.DiceSet;

public interface RollingOperation {
    public int rollOperation(DiceSet diceSet
        , int dropsOrRerolls, boolean greatestOrLeast
        , Predicate<? super Integer> predicate
    );
}