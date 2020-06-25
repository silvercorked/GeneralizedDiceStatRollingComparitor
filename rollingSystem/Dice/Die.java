package rollingSystem.Dice;

import java.util.Random;

public class Die {
	
	private int sides;
	private int[] faceValues;
	private final Random rand = new Random();
	
	public Die() {
		this.sides = 6;
		int[] temp = {1,2,3,4,5,6};
		this.faceValues = temp;
	}
	public Die(int[] faceValues) {
        this.setFaceValues(faceValues);
        this.setSides(faceValues.length);
	}
	public Die(String parseString) {
		int dieFaces = Integer.parseInt(parseString.substring(parseString.indexOf('d') + 1, parseString.indexOf(':')), 10);
		int faceValues[] = new int[dieFaces];
		for (int i = parseString.indexOf(':') + 1, arrayIndex = 0,  start = i; arrayIndex < faceValues.length; arrayIndex++,  start = i) {
			while (Character.isDigit(parseString.charAt(i))) {
				if (++i >= parseString.length())
					break;
			}
			faceValues[arrayIndex] = Integer.parseInt(parseString.substring(start, i++), 10);
		}
		this.setSides(dieFaces);
		this.setFaceValues(faceValues);
    }
    
	public int rollDie() {
		double percent = this.rand.nextDouble();
		int partitions = this.getSides();
		double partitionSize = 1.0 / partitions;
		int index = 0;
		for (double increment = partitionSize; increment < percent && index < partitions; increment += partitionSize, index++) {}
		return this.getFaceValues()[index];
    }
    
    private void setFaceValues(int[] faceValues) {
		this.faceValues = faceValues;
		if (this.sides != this.faceValues.length)
			this.setSides(this.faceValues.length);
	}
	private void setSides(int sides) {
		this.sides = sides;
	}
	
	public int[] getFaceValues() {
		return this.faceValues;
    }
    public int getSides() {
		return this.sides;
	}
}
