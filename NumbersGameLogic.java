import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Creates a magic number game GUI
 * 
 * @author Connor 10-26-2020
 */
public class NumbersGameLogic {
	GridBagConstraints gbc = new GridBagConstraints();
	GridBagConstraints gp = new GridBagConstraints();
	GridBagConstraints op = new GridBagConstraints();
	private JFrame jfrm;
	private String name;
	private String difficultyString;
	private int difficulty;
	private String[] magicNumber;
	private String levelOneString;
	private int levelOneMax;
	private JTextArea textArea;
	private long startTime;
	private long endTime;
	private List<String> guessList;
	private JTextField guess;
	private int correctNumbers;
	private int correctPositions;
	private int numberTurns;
	private double time;
	private int playAgain;
	private JButton start;
	private Scanner scan;
	private String path = System.getProperty("user.dir");
	private Scanner nameCheck;
	private Scanner update;
	private double totalTime;
	private int totalTurns;
	private int gameCount;
	private FileWriter updateRecords;
	private boolean foundRecord;
	private FileWriter newEntry;
	private int sameNumberCount;
	private File file;
	private File tempFile;
	private JCheckBox option1;
	private JCheckBox option2;
	private JCheckBox option3;
	private Scanner statScan;
	private Scanner statScan2;
	private String[] statSplit;
	private Double[] timeSplit;
	private int statLineCount;
	private String name1;
	private String name2;
	private String name3;
	private Double[] moveSplit;
	private int[] winSplit;
	private boolean correctNumber;

	/**
	 * Constructor
	 */
	public NumbersGameLogic() {
		jfrm = new JFrame("Numbers Game");
		jfrm.setLayout(new GridBagLayout());
		jfrm.setSize(600, 500);
		jfrm.setResizable(false);
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jfrm.setVisible(true);

		JLabel title = new JLabel("Numbers Game", JLabel.CENTER);
		gbc.gridy = 1;
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		title.setBorder(BorderFactory.createLineBorder(Color.black));
		jfrm.add(title, gbc);

		start = new JButton("Start Game");
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		jfrm.add(start, gbc);

		JPanel guessing = new JPanel();
		guessing.setLayout(new GridBagLayout());
		gbc.gridx = 2;
		gbc.gridy = 2;
		jfrm.add(guessing, gbc);

		JLabel guessText = new JLabel("Enter Guess:", JLabel.CENTER);
		gp.gridx = 1;
		gp.gridy = 1;
		gp.gridwidth = 1;
		gp.weightx = .1;
		gp.fill = GridBagConstraints.HORIZONTAL;
		guessing.add(guessText, gp);

		guess = new JTextField();
		gp.gridx = 2;
		gp.gridy = 1;
		gp.weightx = .9;
		gp.fill = GridBagConstraints.HORIZONTAL;
		guessing.add(guess, gp);

		JButton submit = new JButton("Submit");
		gp.gridx = 3;
		gp.gridy = 1;
		gp.gridwidth = 1;
		gp.weightx = 0;
		gp.fill = GridBagConstraints.HORIZONTAL;
		guessing.add(submit, gp);

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.append(
				"Instructions\n----------------\n\nYour objective is to correctly guess the randomly generated number, based off the difficulty you choose.\n"
						+ "\nFor difficulty 1: You will randomly guess a single number. You enter the maximum value that will be\n"
						+ "randomly generated. After each guess, you will be given a reponse that says if your guess was above,\n"
						+ " or below the correct answer until you enter the correct number\n\n"
						+ "For difficulties 2 and greater: The difficulty you choose is the number of digits (0-9) you need to\n "
						+ "guess. You will enter your digit guesses seperated by a comma and space. For example, if your\n"
						+ "difficulty is 3, an appropriate guess could be 1, 3, 7. After each turn, you will be given a two\n"
						+ "number output like 1, 0. The first digit is the number of correct digits in your guess. The second\n"
						+ "digit is the number of digits in your guess that are in the correct position. You continue until both\n"
						+ "digits output after each turn equals your difficulty number. For example, a win on difficulty 5 would\n"
						+ "look like 5, 5.\n\nClick 'Start Game' to begin.\n\n");
		JScrollPane scrollArea = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.weighty = .5;
		gbc.fill = GridBagConstraints.BOTH;
		jfrm.add(scrollArea, gbc);

		JPanel options = new JPanel();
		options.setLayout(new GridBagLayout());
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		jfrm.add(options, gbc);

		JLabel optionsText = new JLabel("Statistics Options", JLabel.CENTER);
		op.gridx = 2;
		op.gridy = 1;
		op.gridwidth = 1;
		op.weightx = 1;
		op.fill = GridBagConstraints.HORIZONTAL;
		options.add(optionsText, op);

		option1 = new JCheckBox("Avg. Time", false);
		op.gridx = 1;
		op.gridy = 2;
		options.add(option1, op);

		option2 = new JCheckBox("Avg. # of Moves", false);
		op.gridx = 2;
		op.gridy = 2;
		options.add(option2, op);

		option3 = new JCheckBox("Top Players", false);
		op.gridx = 3;
		op.gridy = 2;
		options.add(option3, op);

		JButton statistics = new JButton("Show Statistics");
		gbc.gridx = 2;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		jfrm.add(statistics, gbc);

		start.addActionListener(new ButtonListener());
		submit.addActionListener(new ButtonListener());
		statistics.addActionListener(new ButtonListener());
	}// end of constructor

	/**
	 * ActionListener class for all buttons
	 * 
	 * @author Connor 10-26-2020
	 */
	class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			JButton b = (JButton) e.getSource();

			// Start Game Function
			if (b.getText().contentEquals("Start Game")) {

				// User enters name
				name = JOptionPane.showInputDialog(jfrm, "Enter Your Name (First and Last Name)", null);
				boolean validName = false;
				while (validName == false) {
					if (name.contentEquals("")) {
						name = JOptionPane.showInputDialog(jfrm,
								"You didn't enter a name.\nEnter Your Name (First and Last Name)", null);
					} else {
						validName = true;
					}
				}

				// User enters difficulty option and checks to see if it is a valid difficulty
				difficultyString = JOptionPane.showInputDialog(jfrm, "Enter difficulty level as a positive integer",
						null);
				boolean correctAnswer = false;
				while (correctAnswer == false) {
					if (difficultyString != null) {
						try {
							difficulty = Integer.valueOf(difficultyString);
						} catch (NumberFormatException ne) {
							difficultyString = JOptionPane.showInputDialog(jfrm,
									"You didn't enter an integer\nEnter difficulty level as a positive integer", null);
						}
						if (difficulty >= 1) {
							correctAnswer = true;
						} else {
							difficultyString = JOptionPane.showInputDialog(jfrm,
									"You entered a negative integer\nEnter difficulty level as a positive integer",
									null);
						}
					} else {
						correctAnswer = true;
					}
				}

				// Makes initial data file
				try {
					file = new File(path + "\\data.txt");
					if (!file.exists()) {
						file.createNewFile();
					}
					System.out.println("Data file is in: " + path);
				} catch (IOException er) {
					System.out.println("An error occurred.");
					er.printStackTrace();

				}

				magicNumber = new String[difficulty];

				// Creates magic number for difficulties greater than 1
				if (difficulty > 1) {
					for (int i = 0; i < magicNumber.length; i++) {
						Random random = new Random();
						magicNumber[i] = String.valueOf(random.nextInt(10));
					}
				}

				// Creates magic number for difficulty 1
				else {
					levelOneString = JOptionPane.showInputDialog(jfrm, "Enter max positive integer to guess", null);
					boolean levelOneCorrect = false;
					while (levelOneCorrect == false) {
						if (levelOneString != null) {
							try {
								levelOneMax = Integer.valueOf(levelOneString);
							} catch (NumberFormatException ne) {
								levelOneString = JOptionPane.showInputDialog(jfrm,
										"You didn't enter an integer\nEnter max positive integer to guess", null);
							}
							if (levelOneMax >= 1) {
								levelOneCorrect = true;
							} else {
								levelOneString = JOptionPane.showInputDialog(jfrm,
										"You entered a negative integer\nEnter max positive integer to guess", null);
							}
						} else {
							levelOneCorrect = true;
						}
					}
					Random randomOne = new Random();
					magicNumber[0] = String.valueOf(randomOne.nextInt(levelOneMax + 1));
				}

				textArea.append(name + " guess a number.\n\n");

				// Starts keeping track of time played and turns needed to win one a valid magic
				// number has been generated
				startTime = System.currentTimeMillis();
				numberTurns = 0;

			} // end of Start Game function

			// Submit function
			if (b.getText().contentEquals("Submit")) {

				boolean validGuess = false;
				guessList = Arrays.asList(guess.getText().split(", "));
				guess.setText("");

				// Determines if a correct guess has been entered for a difficulty 1 magic
				// number
				if (difficulty == 1) {
					if (guessList.size() > magicNumber.length) {
						textArea.append(
								"You entered more digits than this difficulty uses.\n" + "Enter a new guess.\n\n");
					} else if (guessList.size() < magicNumber.length) {
						textArea.append(
								"You entered less digits than this difficulty uses or didn't put ', ' between each digit.\n"
										+ "Enter a new guess.\n\n");
					}

					else {
						try {
							Integer.valueOf(guessList.get(0));
							validGuess = true;
						} catch (NumberFormatException ne) {
							textArea.append("You didn't enter an integer.\nEnter a new guess.\n\n");
						}
					}
				}

				// Determines if a correct guess has been entered for all other difficulties
				else {
					if (guessList.size() > magicNumber.length) {
						textArea.append(
								"You entered more digits than this difficulty uses.\n" + "Enter a new guess.\n\n");
					} else if (guessList.size() < magicNumber.length) {
						textArea.append(
								"You entered less digits than this difficulty uses or didn't put ', ' between each digit.\n"
										+ "Enter a new guess.\n\n");
					} else {
						validGuess = true;
					}

					// Checks if all numbers in magicNumber array are the same
					sameNumberCount = 0;
					for (int i = 0; i < magicNumber.length; i++) {
						if (magicNumber[0] == magicNumber[i]) {
							sameNumberCount++;
						}
					}
				}

				// Checks if the user guessed the correct magic number
				if (validGuess == true) {
					correctNumber = false;

					// Checks for a correct difficulty 1 number and provides an appropriate response
					if (difficulty == 1) {
						if (guessList.get(0).contentEquals(magicNumber[0])) {
							correctNumber = true;
						} else if (Integer.valueOf(guessList.get(0)) > Integer.valueOf(magicNumber[0])) {
							textArea.append("Your guess was greater than the correct number.\n\n");
						} else {
							textArea.append("Your guess was less than the correct number.\n\n");
						}
					}

					// Checks for a correct answer for all other difficulties and provides an
					// appropriate response
					else {
						correctNumbers = 0;
						correctPositions = 0;
						for (int i = 0; i < guessList.size(); i++) {
							for (int j = 0; j < guessList.size(); j++) {
								if (guessList.get(i).contentEquals(magicNumber[j])) {
									correctNumbers++;
									if (i == j) {
										correctPositions++;
									}
								}
							}
						}

						// Cuts correct number count in half if all numbers in array are the same to
						// prevent double count
						if (sameNumberCount == magicNumber.length) {
							correctNumbers = correctNumbers / 2;
						}
						textArea.append(correctNumbers + ", " + correctPositions + "\n\n");
						numberTurns++;
					}
				}

				// Checks if player has won
				if (((correctNumbers == difficulty && correctPositions == difficulty)
						|| (correctNumbers == (difficulty * difficulty) && correctPositions == difficulty))
						|| correctNumber == true) {

					// Stops tracking time and calculates total time to win
					endTime = System.currentTimeMillis();
					time = (endTime - startTime) / 1000.0;

					// Asks user if they want to play again
					playAgain = JOptionPane
							.showConfirmDialog(jfrm,
									"You won!\nYou finished the game in " + time + " seconds and " + numberTurns
											+ " turns.\nDo you want to play again?",
									"You won!", JOptionPane.YES_NO_OPTION);

					// Condition if the user tries to close the window without clicking yes or no
					while (playAgain == -1) {
						playAgain = JOptionPane
								.showConfirmDialog(jfrm,
										"You won!\n You finished the game in " + time + " seconds and " + numberTurns
												+ " turns.\nDo you want to play again?",
										"You won!", JOptionPane.YES_NO_OPTION);
					}

					// Conditions if user clicks yes (0) or not (1)
					if (playAgain == 0 || playAgain == 1) {

						try {
							scan = new Scanner(new File(path + "\\data.txt"));
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						int lineCount = 0;
						while (scan.hasNextLine()) {
							lineCount++;
							scan.nextLine();
						}
						scan.close();

						// Checking to see if the player's record exists already
						try {
							nameCheck = new Scanner(new File(path + "\\data.txt"));
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						int nameCheckCount = 0;

						// Creates a new file to store updated records
						try {
							tempFile = new File(path + "\\data_update.txt");
							updateRecords = new FileWriter(tempFile);
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}

						foundRecord = false;

						try {
							update = new Scanner(new File(path + "\\data.txt"));
						} catch (FileNotFoundException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}

						// Does an initial scan of the data.txt file to see if the player's record
						// exists
						while (nameCheck.hasNextLine()) {
							String[] record = nameCheck.nextLine().split(" ");
							if (record[0].contentEquals(name)) {
								foundRecord = true;
								for (int i = 0; i < lineCount; i++) {

									// Adding records that aren't the player to the update file
									if (i != nameCheckCount) {
										try {
											updateRecords.write(update.nextLine() + "\n");
										} catch (IOException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
									}

									// Updating and adding current player's record to temporary file if it already
									// exists
									else {
										String[] oldLine = update.nextLine().split(" ");
										gameCount = Integer.valueOf(oldLine[3]) + 1;
										totalTime += (time + Double.valueOf(oldLine[5]));
										totalTurns += (numberTurns + Integer.valueOf(oldLine[4]));
										double avgTime = totalTime / gameCount;
										double avgTurns = totalTurns / gameCount;
										oldLine[1] = String.valueOf(avgTurns);
										oldLine[2] = String.valueOf(avgTime);
										oldLine[3] = String.valueOf(gameCount);
										try {
											updateRecords.write(oldLine[0] + " " + oldLine[1] + " " + oldLine[2] + " "
													+ oldLine[3] + " " + totalTurns + " " + totalTime + "\n");
										} catch (IOException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
									}
								}
							} else {
								nameCheckCount++;
							}
						}
						nameCheck.close();
						update.close();

						try {
							updateRecords.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

					// Adds record if player isn't found
					if (foundRecord == false) {
						try {
							newEntry = new FileWriter(file, true);
							newEntry.write(
									name + " " + numberTurns + " " + time + " 1 " + numberTurns + " " + time + "\n");
							newEntry.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

					// If the player clicks "Yes" the game starts again
					if (playAgain == 0) {
						start.doClick();
					}
				}
			} // end of Submit function

			// Show Statistics function
			if (b.getText().contentEquals("Show Statistics")) {

				statLineCount = 0;

				// Keeps track of how many lines the current update data file is
				try {
					statScan2 = new Scanner(new File(path + "\\data_update.txt"));
					while (statScan2.hasNextLine()) {
						statLineCount++;
						statScan2.nextLine();
					}
				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				statScan2.close();

				// Avg. Time option selected
				if (option1.isSelected() == true) {

					try {
						statScan = new Scanner(new File(path + "\\data_update.txt"));
						timeSplit = new Double[3];

						while (statScan.hasNextLine()) {

							// Fills the first three spots in the leaderboard with the first three entries
							// (if possible) from the update file
							for (int i = 0; i < statLineCount; i++) {
								statSplit = statScan.nextLine().split(" ");
								if (i <= 2) {
									timeSplit[i] = Double.valueOf(statSplit[2]);
									if (i == 0) {
										name1 = statSplit[0];
									} else if (i == 1) {
										name2 = statSplit[0];
									} else {
										name3 = statSplit[0];
									}
								} else {

									// Sorts initial list
									boolean sorted = false;
									while (sorted == false) {
										if (timeSplit[1] < timeSplit[0]) {
											Double temp1 = timeSplit[0];
											String tempName1 = name1;
											timeSplit[0] = timeSplit[1];
											name1 = name2;
											timeSplit[1] = temp1;
											name2 = tempName1;
										}

										if (timeSplit[2] < timeSplit[1]) {
											Double temp1 = timeSplit[1];
											String tempName1 = name2;
											timeSplit[1] = timeSplit[2];
											name2 = name3;
											timeSplit[2] = temp1;
											name3 = tempName1;
										}

										if (timeSplit[2] < timeSplit[0]) {
											Double temp1 = timeSplit[0];
											String tempName1 = name1;
											timeSplit[0] = timeSplit[2];
											name1 = name3;
											timeSplit[2] = temp1;
											name3 = tempName1;
										}

										if (timeSplit[0] <= timeSplit[1] && timeSplit[1] <= timeSplit[2]) {
											sorted = true;
										}
									}

									// Current time is faster than 3rd fastest time
									if (Double.valueOf(statSplit[2]) < timeSplit[2]) {

										// Current time is faster than 2nd fastest time
										if (Double.valueOf(statSplit[2]) < timeSplit[1]) {

											// Current time is faster than fastest time
											if (Double.valueOf(statSplit[2]) < timeSplit[0]) {
												Double temp1 = timeSplit[1];
												Double temp2 = timeSplit[0];
												String tempName1 = name1;
												String tempName2 = name2;
												timeSplit[0] = Double.valueOf(statSplit[2]);
												name1 = statSplit[0];
												timeSplit[1] = temp2;
												name2 = tempName1;
												timeSplit[2] = temp1;
												name3 = tempName2;
											} // End of the fastest time condition

											else {
												Double temp1 = timeSplit[1];
												String tempName1 = name2;
												timeSplit[1] = Double.valueOf(statSplit[2]);
												name2 = statSplit[0];
												timeSplit[2] = temp1;
												name3 = tempName1;
											} // End of 2nd fastest time condition

										} else {
											timeSplit[2] = Double.valueOf(statSplit[2]);
											name3 = statSplit[0];
										} // End of 3rd fastest time condition

									}
								}
							}
						}

						// Sorts final list
						boolean sorted = false;
						while (sorted == false) {
							if (timeSplit[1] < timeSplit[0]) {
								Double temp1 = timeSplit[0];
								String tempName1 = name1;
								timeSplit[0] = timeSplit[1];
								name1 = name2;
								timeSplit[1] = temp1;
								name2 = tempName1;
							}

							if (timeSplit[2] < timeSplit[1]) {
								Double temp1 = timeSplit[1];
								String tempName1 = name2;
								timeSplit[1] = timeSplit[2];
								name2 = name3;
								timeSplit[2] = temp1;
								name3 = tempName1;
							}

							if (timeSplit[2] < timeSplit[0]) {
								Double temp1 = timeSplit[0];
								String tempName1 = name1;
								timeSplit[0] = timeSplit[2];
								name1 = name3;
								timeSplit[2] = temp1;
								name3 = tempName1;
							}

							if (timeSplit[0] <= timeSplit[1] && timeSplit[1] <= timeSplit[2]) {
								sorted = true;
							}
						}

					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					statScan.close();
					textArea.append("Top 3 Players By Avg. Time\n-------------------------------------\n" + name1 + "  "
							+ String.format("%.3f", timeSplit[0]) + "\n" + name2 + "  "
							+ String.format("%.3f", timeSplit[1]) + "\n" + name3 + "  "
							+ String.format("%.3f", timeSplit[2]) + "\n\n");
				}

				// Avg. Number of Moves Option
				if (option2.isSelected() == true) {
					try {
						statScan = new Scanner(new File(path + "\\data_update.txt"));
						moveSplit = new Double[3];

						while (statScan.hasNextLine()) {

							// Fills the first three spots in the leaderboard with the first three entries
							// (if possible) from the update file
							for (int i = 0; i < statLineCount; i++) {
								statSplit = statScan.nextLine().split(" ");
								if (i <= 2) {
									moveSplit[i] = Double.valueOf(statSplit[1]);
									if (i == 0) {
										name1 = statSplit[0];
									} else if (i == 1) {
										name2 = statSplit[0];
									} else {
										name3 = statSplit[0];
									}
								} else {

									// Sorts initial list
									boolean sorted = false;
									while (sorted == false) {
										if (moveSplit[1] < moveSplit[0]) {
											Double temp1 = moveSplit[0];
											String tempName1 = name1;
											moveSplit[0] = moveSplit[1];
											name1 = name2;
											moveSplit[1] = temp1;
											name2 = tempName1;
										}

										if (moveSplit[2] < moveSplit[1]) {
											Double temp1 = moveSplit[1];
											String tempName1 = name2;
											moveSplit[1] = moveSplit[2];
											name2 = name3;
											moveSplit[2] = temp1;
											name3 = tempName1;
										}

										if (moveSplit[2] < moveSplit[0]) {
											Double temp1 = moveSplit[0];
											String tempName1 = name1;
											moveSplit[0] = moveSplit[2];
											name1 = name3;
											moveSplit[2] = temp1;
											name3 = tempName1;
										}

										if (moveSplit[0] <= moveSplit[1] && moveSplit[1] <= moveSplit[2]) {
											sorted = true;
										}
									}

									// Current average moves is faster than 3rd smallest
									if (Double.valueOf(statSplit[1]) < moveSplit[2]) {

										// Current average moves is faster than 2nd smallest
										if (Double.valueOf(statSplit[1]) < moveSplit[1]) {

											// Current average moves is faster than smallest
											if (Double.valueOf(statSplit[1]) < moveSplit[0]) {
												Double temp1 = moveSplit[1];
												Double temp2 = moveSplit[0];
												String tempName1 = name1;
												String tempName2 = name2;
												moveSplit[0] = Double.valueOf(statSplit[1]);
												name1 = statSplit[0];
												moveSplit[1] = temp2;
												name2 = tempName1;
												moveSplit[2] = temp1;
												name3 = tempName2;
											} // End of smallest average moves condition
											
											else {
												Double temp1 = moveSplit[1];
												String tempName1 = name2;
												moveSplit[1] = Double.valueOf(statSplit[1]);
												name2 = statSplit[0];
												moveSplit[2] = temp1;
												name3 = tempName1;
											} // End of 2nd smallest average moves condition
											
										} else {
											moveSplit[2] = Double.valueOf(statSplit[1]);
											name3 = statSplit[0];
										} // End of 3rd smallest average moves condition
										
									}
								}
							}
						}

						// Sorts final list
						boolean sorted = false;
						while (sorted == false) {
							if (moveSplit[1] < moveSplit[0]) {
								Double temp1 = moveSplit[0];
								String tempName1 = name1;
								moveSplit[0] = moveSplit[1];
								name1 = name2;
								moveSplit[1] = temp1;
								name2 = tempName1;
							}

							if (moveSplit[2] < moveSplit[1]) {
								Double temp1 = moveSplit[1];
								String tempName1 = name2;
								moveSplit[1] = moveSplit[2];
								name2 = name3;
								moveSplit[2] = temp1;
								name3 = tempName1;
							}

							if (moveSplit[2] < moveSplit[0]) {
								Double temp1 = moveSplit[0];
								String tempName1 = name1;
								moveSplit[0] = moveSplit[2];
								name1 = name3;
								moveSplit[2] = temp1;
								name3 = tempName1;
							}

							if (moveSplit[0] <= moveSplit[1] && moveSplit[1] <= moveSplit[2]) {
								sorted = true;
							}
						}

					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					statScan.close();
					textArea.append("Top 3 Players By Avg. Moves\n---------------------------------------\n" + name1
							+ "  " + String.format("%.1f", moveSplit[0]) + "\n" + name2 + "  "
							+ String.format("%.1f", moveSplit[1]) + "\n" + name3 + "  "
							+ String.format("%.1f", moveSplit[2]) + "\n\n");
				}

				// Total Wins option selected
				if (option3.isSelected() == true) {
					try {
						statScan = new Scanner(new File(path + "\\data_update.txt"));
						winSplit = new int[3];

						while (statScan.hasNextLine()) {

							// Fills the first three spots in the leaderboard with the first three entries
							// (if possible) from the update file
							for (int i = 0; i < statLineCount; i++) {
								statSplit = statScan.nextLine().split(" ");
								if (i <= 2) {
									winSplit[i] = Integer.valueOf(statSplit[3]);
									if (i == 0) {
										name1 = statSplit[0];
									} else if (i == 1) {
										name2 = statSplit[0];
									} else {
										name3 = statSplit[0];
									}
								}

								else {

									// Sorts initial list
									boolean sorted = false;
									while (sorted == false) {
										if (winSplit[1] > winSplit[0]) {
											int temp1 = winSplit[0];
											String tempName1 = name1;
											winSplit[0] = winSplit[1];
											name1 = name2;
											winSplit[1] = temp1;
											name2 = tempName1;
										}

										if (winSplit[2] > winSplit[1]) {
											int temp1 = winSplit[1];
											String tempName1 = name2;
											winSplit[1] = winSplit[2];
											name2 = name3;
											winSplit[2] = temp1;
											name3 = tempName1;
										}

										if (winSplit[2] > winSplit[0]) {
											int temp1 = winSplit[0];
											String tempName1 = name1;
											winSplit[0] = winSplit[2];
											name1 = name3;
											winSplit[2] = temp1;
											name3 = tempName1;
										}

										if (winSplit[0] >= winSplit[1] && winSplit[1] >= winSplit[2]) {
											sorted = true;
										}
									}

									// Current number of wins is greater than 3rd highest
									if (Integer.valueOf(statSplit[3]) > winSplit[2]) {

										// Current number of wins is greater than 2nd highest
										if (Integer.valueOf(statSplit[3]) > winSplit[1]) {

											// Current number of wins is greater than the highest
											if (Integer.valueOf(statSplit[3]) > winSplit[0]) {
												int temp1 = winSplit[1];
												int temp2 = winSplit[0];
												String tempName1 = name1;
												String tempName2 = name2;
												winSplit[0] = Integer.valueOf(statSplit[3]);
												name1 = statSplit[0];
												winSplit[1] = temp2;
												name2 = tempName1;
												winSplit[2] = temp1;
												name3 = tempName2;
											} // End of the greatest number of wins condition

											else {
												int temp1 = winSplit[1];
												String tempName1 = name2;
												winSplit[1] = Integer.valueOf(statSplit[3]);
												name2 = statSplit[0];
												winSplit[2] = temp1;
												name3 = tempName1;
											} // End of 2nd greatest number of wins condition

										} else {
											winSplit[2] = Integer.valueOf(statSplit[3]);
											name3 = statSplit[0];
										} // End of 3rd greatest number of wins condition

									}
								}
							}
						}

						// Sorts final list
						boolean sorted = false;
						while (sorted == false) {
							if (winSplit[1] > winSplit[0]) {
								int temp1 = winSplit[0];
								String tempName1 = name1;
								winSplit[0] = winSplit[1];
								name1 = name2;
								winSplit[1] = temp1;
								name2 = tempName1;
							}

							if (winSplit[2] > winSplit[1]) {
								int temp1 = winSplit[1];
								String tempName1 = name2;
								winSplit[1] = winSplit[2];
								name2 = name3;
								winSplit[2] = temp1;
								name3 = tempName1;
							}

							if (winSplit[2] > winSplit[0]) {
								int temp1 = winSplit[0];
								String tempName1 = name1;
								winSplit[0] = winSplit[2];
								name1 = name3;
								winSplit[2] = temp1;
								name3 = tempName1;
							}

							if (winSplit[0] >= winSplit[1] && winSplit[1] >= winSplit[2]) {
								sorted = true;
							}
						}

					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					statScan.close();
					textArea.append(
							"Top 3 Players By Wins\n-------------------------------\n" + name1 + "  " + winSplit[0]
									+ "\n" + name2 + "  " + winSplit[1] + "\n" + name3 + "  " + winSplit[2] + "\n\n");
				}
			} // end of Show Statistics function
		}// end of ActionPerformed method
	}// end of ButtonListener class
}// end of class