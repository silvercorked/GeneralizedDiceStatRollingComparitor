package rollingSystem.Dice;

public class Dice {

    private int amountOfDice;
    private Die[] dice;

    public Dice() {
        this.setAmountOfDice(1);
        this.dice = new Die[1];
        this.dice[1] = new Die();
    }
    public Dice(int amountOfDice, int[] faceValues) {
        this.setDice(amountOfDice, faceValues);
    }

    private void setAmountOfDice(int amountOfDice) {
        this.amountOfDice = amountOfDice;
    }
    private void setDice(int amountOfDice, int[] faceValues) {
        this.setAmountOfDice(amountOfDice);
        this.dice = new Die[this.amountOfDice];
        for (int i = 0; i < this.amountOfDice; i++) {
            this.dice[i] = new Die(faceValues);
        }
    }
    public int getAmountOfDice() {
        return this.amountOfDice;
    }
    public Die[] getDice() {
        return this.dice;
    }
}