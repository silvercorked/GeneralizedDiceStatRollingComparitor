import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import compareDiceSets.Statistics;
import compareDiceSets.Tuple;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;

import rollingSystem.Utilities;
import rollingSystem.Dice.DiceSet;

public class Runner {

	public static int maxOfSingleDie;
	public static BigDecimal runs;
	public static int maxThreads;
	public static String d4;
	public static String d6;
	public static String d8;
	public static String d10;
	public static String d12;
	public static String d20;

	public static void main(String ... args) {
		Instant start = Instant.now();
		maxOfSingleDie = 6;
		runs = new BigDecimal("500");
		maxThreads = 4096;
		d4 = "d4:1,2,3,4";
		d6 = "d6:1,2,3,4,5,6";
		d8 = "d8:1,2,3,4,5,6,7,8";
		d10 = "d10:1,2,3,4,5,6,7,8,9,10";
		d12 = "d12:1,2,3,4,5,6,7,8,9,10,11,12";
		d20 = "d20:1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20";
		BigDecimal diceSetsHandledSoFar = BigDecimal.ZERO;
		List<String> resultStrings = new ArrayList<String>();
		resultStrings.add("sep=;\n");
		resultStrings.add("dice; conditions; min; max; mean; geometric mean; median; mode; sample variance; sample standard deviation; population variance; population standard deviation; trails");
		List<Integer> presetList = new ArrayList<Integer>();
		presetList.add(0); presetList.add(0); presetList.add(0);
		presetList.add(0); presetList.add(0); presetList.add(0);
		Tuple<List<String>, List<Integer>> returnedValues = new Tuple<List<String>, List<Integer>>(new ArrayList<String>(), presetList);
		do {
			returnedValues = iterateNTimes(10000, maxOfSingleDie, resultStrings, returnedValues.getSecond());
			resultStrings = returnedValues.getFirst();
			diceSetsHandledSoFar = new BigDecimal(returnedValues.getFirst().size());
			System.out.println("Items handled so far: " + diceSetsHandledSoFar.toString());
			System.gc();
			
		} while (!returnedValues.getSecond().isEmpty());
		Path file = Paths.get("C:/Users/Alex/Desktop/programOutputFiles/dndRollingStats" + maxOfSingleDie + "w" + runs.toString() + ".csv");
		try {
			Files.write(file,  resultStrings, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Instant end = Instant.now();
		long elapsed = Duration.between(start, end).toSeconds();
		System.out.println("all done! Elapsed Time: " + elapsed);
	}

	public static Tuple<List<String>, List<Integer>> iterateNTimes(int N, int maxSingleDie, List<String> results, List<Integer> forLoopPreset) {
		int currentIterations = 0;
		final int MAX = N;
		List<List<Integer>> samples = Collections.synchronizedList(new ArrayList<List<Integer>>());;
		List<String> diceSetDescriptionStrings = new ArrayList<String>();
		List<Integer> forLoopPostset = new ArrayList<Integer>();
		List<Thread> threads = new ArrayList<Thread>();
		boolean resetJForLoop = forLoopPreset.get(1) <= maxSingleDie;
		boolean resetKForLoop = forLoopPreset.get(2) <= maxSingleDie;
		boolean resetLForLoop = forLoopPreset.get(3) <= maxSingleDie;
		boolean resetMForLoop = forLoopPreset.get(4) <= maxSingleDie;
		boolean resetNForLoop = forLoopPreset.get(5) <= maxSingleDie;
		System.out.println("i: " + forLoopPreset.get(0) + " j: " + forLoopPreset.get(1) + " k: " + forLoopPreset.get(2)
			+ " l: " + forLoopPreset.get(3) + " m: " + forLoopPreset.get(4) + " n: " + forLoopPreset.get(5));
		startLoop: {
			for (int i = forLoopPreset.get(0); i <= maxSingleDie; i++) { //d4
				//System.out.println("incrementing d4 to " + i + " samplesSoFar: " + samples.size() + " iterations: " + currentIterations);
				for (int j = resetJForLoop ? forLoopPreset.get(1) : 0; j <= maxSingleDie; j++) { //d6
					for (int k = resetKForLoop ? forLoopPreset.get(2) : 0; k <= maxSingleDie; k++) { //d8
						for (int l = resetLForLoop ? forLoopPreset.get(3) : 0; l <= maxSingleDie; l++) { //d10
							//System.out.println("incrementing d10 to " + l + " d4: " + i);
							for (int m = resetMForLoop ? forLoopPreset.get(4) : 0; m <= maxSingleDie; m++) { //d12
								for (int n = resetNForLoop ? forLoopPreset.get(5) : 0; n <= maxSingleDie; n++) { //d20
									// all dice sets
									//System.out.println("creating samples for a single diceSet. Incrementing d20 to " + n);
									if (n == 0 && m == 0 && l == 0 && k == 0 && j == 0 && i == 0)
										continue;
									if (currentIterations > MAX) {
										forLoopPostset.add(i); forLoopPostset.add(j); forLoopPostset.add(k);
										forLoopPostset.add(l); forLoopPostset.add(m); forLoopPostset.add(n);
										break startLoop;
									}
									StringBuilder diceSetString = new StringBuilder(
										(i != 0 ? "" + i + d4 + "|" : "")
										+ (j != 0 ? "" + j + d6 + "|" : "")
										+ (k != 0 ? "" + k + d8 + "|" : "")
										+ (l != 0 ? "" + l + d10 + "|" : "")
										+ (m != 0 ? "" + m + d12 + "|" : "")
										+ (n != 0 ? "" + n + d20 + "|" : "")
									);
									DiceSet currentDiceSet = new DiceSet(diceSetString.substring(0, diceSetString.length() - 1));
									// create samples
									samples.add(new ArrayList<Integer>());
									int sampleIndex = samples.size() - 1;
									currentIterations++;
									threads.add(
										new Thread(
											new ProcessSingleDiceSet(
												sampleIndex,
												runs,
												sampleIndex == 0,
												samples.get(sampleIndex),
												currentDiceSet,
												0,
												false,
												s -> (s == 0),
												(diceSet, dropsRerolls, greatestLeast, predicate) -> {
													return Utilities.roll(diceSet);
												},
												samples
											)
										)
									);
									diceSetDescriptionStrings.add(currentDiceSet.toStringAsDice());
									for (int halfDice = 1; halfDice < (i + j + k + l + m + n); halfDice++) {
										for (int bool = 0; bool < 6; bool++) {
											samples.add(new ArrayList<Integer>());
											sampleIndex = samples.size() - 1;
											threads.add(
												switch (bool) {
													case 0, 1 -> {
														diceSetDescriptionStrings.add(currentDiceSet.toStringAsDice() + " Drop" + halfDice + (bool == 0 ? "Greatest" : "Least"));
														currentIterations++;
														yield new Thread(
															new ProcessSingleDiceSet(
																sampleIndex,
																runs,
																sampleIndex == 0,
																samples.get(sampleIndex),
																currentDiceSet,
																halfDice,
																bool == 0,
																s -> (s == 0),
																(diceSet, dropsRerolls, greatestLeast, predicate) -> {
																	return Utilities.rollDropN(diceSet, dropsRerolls, greatestLeast);
																},
																samples
															)
														);
													}
													case 2 -> {
														diceSetDescriptionStrings.add(currentDiceSet.toStringAsDice() + " Drop" + "IfRollIs" + halfDice);
														currentIterations++;
														yield new Thread(
															new ProcessSingleDiceSet(
																sampleIndex,
																runs,
																sampleIndex == 0,
																samples.get(sampleIndex),
																currentDiceSet,
																0,
																false,
																s -> (s == 1),
																(diceSet, dropsRerolls, greatestLeast, predicate) -> {
																	return Utilities.rollDropIf(diceSet, predicate);
																},
																samples
															)
														);
													}
													case 3, 4 -> {
														diceSetDescriptionStrings.add(currentDiceSet.toStringAsDice() + " Reroll" + halfDice + (bool == 3 ? "Greatest" : "Least"));
														currentIterations++;
														yield new Thread(
															new ProcessSingleDiceSet(
																sampleIndex,
																runs,
																sampleIndex == 0,
																samples.get(sampleIndex),
																currentDiceSet,
																halfDice,
																bool == 0,
																s -> (s == 0),
																(diceSet, dropsRerolls, greatestLeast, predicate) -> {
																	return Utilities.rollRerollN(diceSet, dropsRerolls, greatestLeast);
																},
																samples
															)
														);
													}
													case 5 -> {
														diceSetDescriptionStrings.add(currentDiceSet.toStringAsDice() + " Reroll" + "IfRollIs"  + halfDice);
														currentIterations++;
														yield new Thread(
															new ProcessSingleDiceSet(
																sampleIndex,
																runs,
																sampleIndex == 0,
																samples.get(sampleIndex),
																currentDiceSet,
																0,
																false,
																s -> (s == 1),
																(diceSet, dropsRerolls, greatestLeast, predicate) -> {
																	return Utilities.rollRerollIf(diceSet, predicate);
																},
																samples
															)
														);
													}
													default -> throw new RuntimeException("bool was somehow " + bool + " during switch expression! No Thread Created!");
												}
											);
										}
									}
									if (resetJForLoop)
										if (j + 1 > maxSingleDie)
											resetJForLoop = !resetJForLoop;
										if (resetKForLoop)
											if (k + 1 > maxSingleDie)
												resetKForLoop = !resetKForLoop;
											if (resetLForLoop)
												if (l + 1 > maxSingleDie)
													resetLForLoop = !resetLForLoop;
												if (resetMForLoop)
													if (m + 1 > maxSingleDie)
														resetMForLoop = !resetMForLoop;
													if (resetNForLoop)
														if (n + 1 > maxSingleDie)
															resetNForLoop = !resetNForLoop;
								}
							}
						}
					}
				}
			}
		}
		int threadChunks = threads.size() / maxThreads;
		int threadRemainders = threads.size() % maxThreads;
		System.out.println("Starting threads: total = " + samples.size());
		for (int i = 0; i <= threadChunks; i++) {
			int amountOfThreadsInChunk = (i == threadChunks ? threadRemainders : maxThreads);
			for (int j = 0; j < amountOfThreadsInChunk; j++) {
				threads.get((i * maxThreads) + j).start();
			}
			boolean done = false;
			do {
				done = true;
				for (int j = 0; j < amountOfThreadsInChunk; j++) {
					try {
						threads.get((i * maxThreads) + j).join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} while (!done);
			//System.out.println("" + i + " chunks of " + threadChunks + " chunks left -> ~" + ((((double) i * maxThreads) / ((double) ((threadChunks * maxThreads) + threadRemainders))) * 100) + "%");
		}
		List<List<String>> statsMegaPackage = new ArrayList<List<String>>();
		synchronized(samples) {
			for (List<Integer> sample : samples)
				statsMegaPackage.add(Statistics.statsPack(sample));
		}
		for (int i = 0; i < statsMegaPackage.size(); i++) {
			String[] description = diceSetDescriptionStrings.get(i).split(" ");
			String currentLine = "" + String.join("; ", description) + (description.length == 1 ? "; Sum" : "");
			for (String stat : statsMegaPackage.get(i)) {
				currentLine += "; " + stat;
				//System.out.println("diceSet " + i + " -> " + descriptions.get(i) + ") " + stat);
			}
			currentLine += "; " + samples.get(i).size();
			if (samples.get(i).size() != Integer.parseInt(runs.toString())) {
				System.err.println("Wasn't rolled " + runs.toString() + " times!" + " i: " + i);
			}
			results.add(currentLine);
		}
		return new Tuple<List<String>, List<Integer>>(results, forLoopPostset);
	}

	/*
		ArrayList<DiceSet> sets = new ArrayList<DiceSet>();
		List<List<Integer>> samples = new ArrayList<List<Integer>>();
		samples = Collections.synchronizedList(samples);
		ArrayList<Thread> threads = new ArrayList<Thread>();
		ArrayList<String> descriptions = new ArrayList<String>();
		for (int i = 0; i <= maxOfSingleDie; i++) { //d4
			System.out.println("incrementing d4 to " + i + " sizeSoFar: " + samples.size());
			for (int j = 0; j <= maxOfSingleDie; j++) { //d6
				for (int k = 0; k <= maxOfSingleDie; k++) { //d8
					for (int l = 0; l <= maxOfSingleDie; l++) { //d10
						System.out.println("incrementing d10 to " + l + " d4: " + i);
						for (int m = 0; m <= maxOfSingleDie; m++) { //d12
							for (int n = 0; n <= maxOfSingleDie; n++) { //d20
								// all dice sets
								//System.out.println("creating samples for a single diceSet. Incrementing d20 to " + n);
								if (n == 0 && m == 0 && l == 0 && k == 0 && j == 0 && i == 0)
									continue;
								StringBuilder diceSetString = new StringBuilder(
									(i != 0 ? "" + i + d4 + "|" : "")
									+ (j != 0 ? "" + j + d6 + "|" : "")
									+ (k != 0 ? "" + k + d8 + "|" : "")
									+ (l != 0 ? "" + l + d10 + "|" : "")
									+ (m != 0 ? "" + m + d12 + "|" : "")
									+ (n != 0 ? "" + n + d20 + "|" : "")
								);
								sets.add(new DiceSet(diceSetString.substring(0, diceSetString.length() - 1)));
								// create samples
								samples.add(new ArrayList<Integer>());
								int sampleIndex = samples.size() - 1;
								int setIndex = sets.size() - 1;
								threads.add(
									new Thread(
										new ProcessSingleDiceSet(
											sampleIndex,
											runs,
											sampleIndex % 300 == 0,
											samples.get(sampleIndex),
											sets.get(setIndex),
											0,
											false,
											s -> (s == 0),
											(diceSet, dropsRerolls, greatestLeast, predicate) -> {
												return Utilities.roll(diceSet);
											},
											samples
										)
									)
								);
								descriptions.add(sets.get(setIndex).toStringAsDice());
								for (int halfDice = 1; halfDice <= (maxOfSingleDie / 2) && halfDice < ((i + j + k + l + m + n) - 1); halfDice++) {
									for (int bool = 0; bool < 6; bool++) {
										samples.add(new ArrayList<Integer>());
										sampleIndex = samples.size() - 1;
										threads.add(
											switch (bool) {
												case 0, 1 -> {
													descriptions.add(sets.get(setIndex).toStringAsDice() + " Drop" + halfDice + (bool == 0 ? "Greatest" : "Least"));
													yield new Thread(
														new ProcessSingleDiceSet(
															sampleIndex,
															runs,
															sampleIndex % 300 == 0,
															samples.get(sampleIndex),
															sets.get(setIndex),
															halfDice,
															bool == 0,
															s -> (s == 0),
															(diceSet, dropsRerolls, greatestLeast, predicate) -> {
																return Utilities.rollDropN(diceSet, dropsRerolls, greatestLeast);
															},
															samples
														)
													);
												}
												case 2 -> {
													descriptions.add(sets.get(setIndex).toStringAsDice() + " Drop" + "IfRollIs1");
													yield new Thread(
														new ProcessSingleDiceSet(
															sampleIndex,
															runs,
															sampleIndex % 300 == 0,
															samples.get(sampleIndex),
															sets.get(setIndex),
															0,
															false,
															s -> (s == 1),
															(diceSet, dropsRerolls, greatestLeast, predicate) -> {
																return Utilities.rollDropIf(diceSet, predicate);
															},
															samples
														)
													);
												}
												case 3, 4 -> {
													descriptions.add(sets.get(setIndex).toStringAsDice() + " Reroll" + halfDice + (bool == 3 ? "Greatest" : "Least"));
													yield new Thread(
														new ProcessSingleDiceSet(
															sampleIndex,
															runs,
															sampleIndex % 300 == 0,
															samples.get(sampleIndex),
															sets.get(setIndex),
															halfDice,
															bool == 0,
															s -> (s == 0),
															(diceSet, dropsRerolls, greatestLeast, predicate) -> {
																return Utilities.rollRerollN(diceSet, dropsRerolls, greatestLeast);
															},
															samples
														)
													);
												}
												case 5 -> {
													descriptions.add(sets.get(setIndex).toStringAsDice() + " Reroll" + "IfRollIs1");
													yield new Thread(
														new ProcessSingleDiceSet(
															sampleIndex,
															runs,
															sampleIndex % 300 == 0,
															samples.get(sampleIndex),
															sets.get(setIndex),
															0,
															false,
															s -> (s == 1),
															(diceSet, dropsRerolls, greatestLeast, predicate) -> {
																return Utilities.rollRerollIf(diceSet, predicate);
															},
															samples
														)
													);
												}
												default -> throw new RuntimeException("bool was somehow " + bool + " during switch expression! No Thread Created!");
											}
										);
									}
								}
							}
						}
					}
				}
			}
		}
		int threadChunks = threads.size() / maxThreads;
		int threadRemainders = threads.size() % maxThreads;
		System.out.println("Starting threads: total = " + samples.size());
		for (int i = 0; i <= threadChunks; i++) {
			int amountOfThreadsInChunk = (i == threadChunks ? threadRemainders : maxThreads);
			for (int j = 0; j < amountOfThreadsInChunk; j++) {
				threads.get((i * maxThreads) + j).start();
			}
			boolean done = false;
			do {
				done = true;
				for (int j = 0; j < amountOfThreadsInChunk; j++) {
					try {
						threads.get((i * maxThreads) + j).join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} while (!done);
			System.out.println("" + i + " chunks of " + threadChunks + " chunks left -> ~" + ((((double) i * maxThreads) / ((double) ((threadChunks * maxThreads) + threadRemainders))) * 100) + "%");
		}
		List<List<String>> statsMegaPackage = new ArrayList<List<String>>();
		synchronized(samples) {
			for (List<Integer> sample : samples)
				statsMegaPackage.add(Statistics.statsPack(sample));
		}
		List<String> lines = new ArrayList<String>();
		lines.add("sep=;\n");
		lines.add("dice; conditions; min; max; mean; geometric mean; median; mode; sample variance; sample standard deviation; population variance; population standard deviation; trails");
		for (int i = 0; i < statsMegaPackage.size(); i++) {
			String[] description = descriptions.get(i).split(" ");
			String currentLine = "" + String.join("; ", description) + (description.length == 1 ? "; Sum" : "");
			for (String stat : statsMegaPackage.get(i)) {
				currentLine += "; " + stat;
				//System.out.println("diceSet " + i + " -> " + descriptions.get(i) + ") " + stat);
			}
			currentLine += "; " + samples.get(i).size();
			if (samples.get(i).size() != Integer.parseInt(runs.toString())) {
				System.err.println("Wasn't rolled " + runs.toString() + " times!" + " i: " + i);
			}
			lines.add(currentLine);
		}
		Path file = Paths.get("C:/Users/Alex/Desktop/programOutputFiles/dndRollingStats.csv");
		try {
			Files.write(file,  lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("samples size: " + samples.size());
	*/
}