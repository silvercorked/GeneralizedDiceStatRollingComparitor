package rollingSystem.Dice;

public class DiceSet {

    private Dice[] dice;
    private String stringJustAmountSides;
    private String stringRepresentation;
	
	public DiceSet(String parseString) {
        this.dice = new Dice[0];
        this.stringJustAmountSides = "";
        this.stringRepresentation = parseString;
        parseDiceString(parseString); 
    }
    public void parseDiceString(String diceString) {
        String[] types = diceString.split("\\|");
        this.dice = new Dice[types.length];
        for (int i = 0; i < types.length; i++) {
            int amountOfDice = Integer.parseInt(types[i].substring(0, types[i].indexOf("d")));
            int sides = Integer.parseInt(types[i].substring(types[i].indexOf("d") + 1, types[i].indexOf(":")));
            String[] faceValuesUnparsed = types[i].substring(types[i].indexOf(":") + 1).split(",");
            int[] faceValues = new int[sides];
            this.stringJustAmountSides += "" + amountOfDice + "d" + sides + "|";
            for (int j = 0; j < faceValues.length; j++) {
                faceValues[j] = Integer.parseInt(faceValuesUnparsed[j]);
            }
            if (sides != faceValues.length) {
                throw new RuntimeException("sides and facevalues mismatch!");
            }
            this.dice[i] = new Dice(amountOfDice, faceValues);
        }
    }
	
	public Dice[] getDice() {
		return this.dice;
    }
    
    @Override
    public String toString() {
        return this.stringRepresentation;
    }

    public String toStringAsDice() {
        return this.stringJustAmountSides.substring(0, this.stringJustAmountSides.length() - 1);
    }
}
