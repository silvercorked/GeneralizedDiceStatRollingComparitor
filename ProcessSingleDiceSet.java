import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;

import rollingSystem.Dice.DiceSet;

public class ProcessSingleDiceSet implements Runnable {
    
    private int index;
    private BigDecimal MAX_RUNS;
    private boolean canPrint;
    private List<Integer> sample; // doesn't need to be thread-safe cause one thread should handle each arraylist so they can be unsafe, but their container needs to be safe
    private DiceSet diceSet;
    private int dropsOrRerolls;
    private boolean greatestOrLeast;
    private Predicate<? super Integer> predicate;
    private RollingOperation rollOp;
    private List<List<Integer>> samples;
    
    public ProcessSingleDiceSet(int index, BigDecimal maxRuns, boolean canPrint
        , List<Integer> sample, DiceSet diceSet, int dropsOrRerolls, boolean greatestOrLeast
        , Predicate<? super Integer> predicate, RollingOperation rollOp, List<List<Integer>> samples
    ) {
        this.index = index;
        this.MAX_RUNS = maxRuns;
        this.canPrint = canPrint;
        this.sample = sample;
        this.diceSet = diceSet;
        this.dropsOrRerolls = dropsOrRerolls;
        this.greatestOrLeast = greatestOrLeast;
        this.predicate = predicate;
        this.rollOp = rollOp;
        this.samples = samples;
    }

    @Override
    public void run() {
        if (canPrint)
            System.out.println("Sample: " + index + " is starting!");
        for (BigDecimal i = BigDecimal.ZERO; i.compareTo(this.MAX_RUNS) < 0; i = i.add(BigDecimal.ONE)) {
            try {
            this.sample.add(this.rollOp.rollOperation(this.diceSet, this.dropsOrRerolls, this.greatestOrLeast, this.predicate));
            }
            catch (RuntimeException e) {
                e.printStackTrace();
                System.out.println("index = " + this.index + " diceset = " + this.diceSet.toString());
            }
        }
        this.samples.set(this.index, this.sample);
        if (canPrint)
            System.out.println("Sample: " + index + " is Complete!");
    }
}