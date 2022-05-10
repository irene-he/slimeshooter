/*=================================================================
SlimeShooter
Irene He
December 19, 2019
Java, 12.0.2
=================================================================
Problem Definition - The goal of this game is to capture as many space slimes by
	entering their (x,y) coordinates to find the randomly appearing slimes
	(appearing ONE AT A TIME) as quickly and accurately as possible within a time
	limit or after so many lives. Each incorrect input takes away one of your game lives.
Input – The player name at the beginning, and (x, y) coordinates of the slimes until
	the player runs out of lives.
Output – Indicators for hitting/missing an slime, the player name and score, the
	current score, player accuracy, average time per slime hit, and a view of all
	previous slime positions.
Process – Compares user given coordinates to the current slime coordinate. Calculates
	a score based on used time and current game level.
=================================================================
List of Identifiers
SlimeShooter - Super class for the game <type SlimeShooter>
Content - Content view; switches between menu, gameboard, and scorescreen view <type Content>
Menu - shows the instructions to the game and allows user to enter player name <type Menu>
GameBoard - shows the gameboard and allows user to enter x,y coordiantes to shoot slimes <type GameBoard>
ScoreScreen - shows the hiscores list <type ScoreScreen>
FinalBoard - shows the gameboard with all of the aliesn <type FinalBoard>
contentPanel - used to update the SlimeShooter JFrame dimensions <type JPanel>
hiscore - keeps track of the hiscore <type int>
slimeShooter - instance of SlimeShooter class <type SlimeShooter>
gameBoard - instance of GameBoard class <type GameBoard>
scoreScreen - instance of ScoreScreen class <type ScoreScreen>
finalBoard - instance of FinalBoard class <type FinalBoard>
playerName - the given player name <type String>
board - 2d array to keep track of slimes <type boolean[][]>
curX - the current slime x coordinate <type int>
curY - the current slime y coordinate <type int>
lives - the current amount of lives <type int>
score - the current game score <type int>
level - the current game level <type int>
hit - number of slimes hit <type int>
miss - number of slimes missed <type int>
givenTime - the level time <type int>
curTime - the current timer <type int>
usedTime - total amount of time spent capturing slimes <type int>
x - user entered x coordinate <type String>
y - user entered y coordinate <type String>
timer - a Timer used for updating the countdown timer and checking for slime misses <type Timer>
names - array of all saved names <type String[]>
scores - array of all saved scores <type int[]>
size - size of saved arrays <type int>
board - copy of saved slime board <type boolean[][]>
=================================================================
*/

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class SlimeShooter extends JFrame {
	// Content
	static Content contentPanel;

	// Game Properties
	static int hiscore;

	/*
	 * SlimeShooter constructor:
	 * This procedural method is the constructor for SlimeShooter, it checks for an
	 * existing hiscore
	 * and sets up the JFrame window.
	 * 
	 * List of Local Variables
	 * None
	 *
	 * @param void
	 * 
	 * @return void
	 */
	public SlimeShooter() {
		super("Slime Shooter"); // Sets window title to 'Slime Shooter'
		setLayout(new BorderLayout()); // Uses the BorderLayout as the layout manager for this JFrame

		setSize(500, 250); // Sets the JFrame dimensions
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Sets the default close operation for the JFrame window
		hiscore = 0; // Default hiscore

		try {
			FileReader freader = new FileReader("scores.txt"); // Opens 'scores.txt'
			BufferedReader br = new BufferedReader(freader); // Use the file as a BufferedReader
			String s;

			if ((s = br.readLine()) != null) { // Read the first line in the file
				hiscore = Integer.parseInt(s); // Assign it as the hiscore
			} // end if statement
			br.close(); // Closes the BufferedReader
		} catch (IOException e) { // Catches any IO exceptions
			hiscore = 0; // Set hiscore to 0 if we can't read any files
		} // end try catch for file reader

		contentPanel = new Content(this); // Instantiate Content class and assign it to contentPanel
		add(contentPanel, BorderLayout.CENTER); // Add the contentPanel to the JFrame window
		setVisible(true); // Shows the JFrame to the user
	} // end SlimeShooter constructor

	/*
	 * setFrameDimensions method:
	 * This procedural method sets the size of the JFrame window
	 * 
	 * List of Local Variables
	 * None
	 *
	 * @param x - length of the window <type int>
	 * y - height of the window <type int>
	 * 
	 * @return void
	 */
	public void setFrameDimensions(int x, int y) {
		setSize(x, y); // Sets the JFrame dimensions
	} // end setFrameDimensions method

	/*
	 * main method:
	 * This procedural method is called automatically and is used to organize the
	 * calling of other methods defined in the class
	 * 
	 * List of Local Variables
	 * None
	 *
	 * @param args <type String>
	 * 
	 * @return void
	 */
	public static void main(String args[]) {
		new SlimeShooter(); // Instantiates a new SlimeShooter class
	} // end main method
} // end SlimeShooter class

class Content extends JPanel {
	SlimeShooter slimeShooter;
	Menu menu;
	GameBoard gameBoard;
	ScoreScreen scoreScreen;
	FinalBoard finalBoard;

	/*
	 * Content constructor:
	 * This procedural method is a constructor for Content. It switches between
	 * different
	 * views for the game. It begins with the Menu view
	 * 
	 * List of Local Variables
	 * None
	 *
	 * @param ah - SlimeShooter instance used to set the JFrame size <type
	 * SlimeShooter>
	 * 
	 * @return void
	 */
	public Content(SlimeShooter ss) {
		slimeShooter = ss; // Assigns SlimeShooter instance to ss
		menu = new Menu(); // Instantiates Menu class and assigns it to menu

		add(menu, BorderLayout.CENTER); // Adds the menu JPanel to the content JPanel
	} // end Content constructor

	/*
	 * startGame method:
	 * This procedural method switches to the gameboard view
	 * 
	 * List of Local Variables
	 * None
	 *
	 * @param playerName - the player name <type String>
	 * hiscore - the current hiscore <type int>
	 * 
	 * @return void
	 */
	public void startGame(String playerName, int hiscore) {
		removeAll(); // removes all components in the Content JPanel
		slimeShooter.setFrameDimensions(500, 600); // Uses the SlimeShooter instance to set the SlimeShooter JFrame
													// dimensions

		gameBoard = new GameBoard(playerName, hiscore); // Instantiates GameBoard class and assigns it to gameBoard
		add(gameBoard, BorderLayout.CENTER); // Adds the gameBoard panel to the Content JPanel
		revalidate(); // Recalculates the layout
		repaint(); // Repaints the Content JPanel
	} // end startGame method

	/*
	 * showScores method:
	 * This procedural method switches to the hiscores list view. Shows the location
	 * of all slimes
	 * and lists out the hiscores from the read file
	 * 
	 * List of Local Variables
	 * finalScreenPanel - JPanel used for displaying the location of all slimes
	 * <type JPanel>
	 * hiscoresLabel - JPanel used for displaying the hiscore list <type JPanel>
	 *
	 * @param playerName - the player name <type String>
	 * score - the current user score <type int>
	 * accuracy - the accuracy of the user <type int>
	 * avgTime - the average time taken to hit an slime <type int>
	 * board - the location of all previous slimes <type int[][]>
	 * 
	 * @return void
	 */
	public void showScores(String playerName, int score, int accuracy, int avgTime, boolean[][] board) {
		// Print final score, accuracy, and average hit time to the console
		System.out.println("************************");
		System.out.println("Player: " + playerName);
		System.out.println("Final Score: " + score);
		System.out.println("Accuracy: " + accuracy + "%");
		System.out.println("Average Hit Time: " + avgTime + " seconds");
		System.out.println("************************");

		removeAll(); // Removes all components in the Content JPanel
		slimeShooter.setFrameDimensions(1000, 625); // Uses the SlimeShooter instance to set the SlimeShooter JFrame
													// dimensions

		JPanel finalScreenPanel = new JPanel(new BorderLayout()); // Creates a new JPanel with BorderLoyout layout
		finalScreenPanel.setPreferredSize(new Dimension(1000, 575)); // Sets the finalScreenPanel dimensions

		JLabel hiscoresLabel = new JLabel("Hiscores"); // Creates a new JLabel
		hiscoresLabel.setHorizontalAlignment(JLabel.CENTER); // Centers the label text
		hiscoresLabel.setBorder(new EmptyBorder(5, 0, 5, 0)); // Adds padding around the label

		scoreScreen = new ScoreScreen(playerName, score); // Instantiates ScoreScreen class and assigns it to
															// ScoreScreen
		finalBoard = new FinalBoard(board); // Instantiates FinalBoard class and assigns it to finalBoard

		finalScreenPanel.add(hiscoresLabel, BorderLayout.NORTH); // Adds the hiscore title label to finalScreenPanel
		finalScreenPanel.add(finalBoard, BorderLayout.WEST); // Adds the finalBoard panel to finalScreenPanel
		finalScreenPanel.add(scoreScreen, BorderLayout.EAST); // Adds the scoreScreen panel to finalScreenPanel
		add(finalScreenPanel, BorderLayout.CENTER); // Adds the finalScreenPanel panel to Content JPanel
		revalidate(); // Recalculates the layout
		repaint(); // Repaints the Content JPanel
	} // end showScores method
} // end Content class

class Menu extends JPanel {
	String playerName = "";

	/*
	 * Menu constructor:
	 * This procedural method is the constructor for Menu. Displays the game
	 * instructions.
	 * 
	 * List of Local Variables
	 * titleLabel - Welcome title label <type JLabel>
	 * instTitleLabel - Instruction title label <type JLabel>
	 * instOneLabel - Instruction one label <type JLabel>
	 * instTwoLabel - Instruction two label <type JLabel>
	 * instThreeLabel - Instruction three label <type JLabel>
	 * instFourLabel - Instruction Four label <type JLabel>
	 * instFiveLabel - Instruction Five label <type JLabel>
	 * playerNamePanel - Panel for player name input <type JPanel>
	 * playerNameLabel - Label for player name input <type JLabel>
	 * playerNameError - Label to display invalid name input <type JLabel>
	 * playerNameTA - The text area for entering player name <type JTextArea>
	 * btnPanel - Panel for exit and start buttons <type JPanel>
	 * startBtn - start game button <type JButton>
	 * exitBtn - exit the game button <type JButton>
	 * 
	 * @param void
	 * 
	 * @return void
	 */
	public Menu() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Sets the JPanel layout to BoxLayout
		setBorder(new EmptyBorder(10, 10, 10, 10)); // Adds padding around the JPanel

		JLabel titleLabel = new JLabel("Welcome to Space Slime Shooter!"); // Creates a new JLabel
		// Adds a margin to the bottom of the Title Panel
		titleLabel.setBorder(new EmptyBorder(5, 0, 5, 0)); // Sets padding around JLabel

		// Create new JLabels
		JLabel instTitleLabel = new JLabel("HOW TO PLAY: ");
		JLabel instOneLabel = new JLabel("1. You are given 3 lives");
		JLabel instTwoLabel = new JLabel("2. You have a set amount of time to capture a slime");
		JLabel instThreeLabel = new JLabel("3. Capture 3 slimes to advance to the next level");
		JLabel instFourLabel = new JLabel("4. The higher the level, the shorter the capture timer");
		JLabel instFiveLabel = new JLabel("5. Enter a point on the cartesian plane");

		// Create new JPanel and JLabels
		JPanel playerNamePanel = new JPanel(new GridLayout(2, 2));
		JLabel playerNameLabel = new JLabel("Player Name:");
		JLabel emptyLabel = new JLabel();
		JLabel playerNameError = new JLabel("Invalid Name");
		playerNameError.setForeground(Color.RED); // Set the error label text to red
		playerNameError.setVisible(false); // Hide the error label
		JTextArea playerNameTA = new JTextArea(); // Create a JTextArea

		playerNameTA.getDocument().addDocumentListener(new DocumentListener() { // DocumentListener for JTextArea
			/*
			 * removeUpdate method:
			 * Procedural method used when text is deleted from text area
			 * 
			 * List of Local Variables
			 * None
			 *
			 * @param e - event when text is deleted <type DocumentEvent>
			 * 
			 * @return void
			 */
			@Override
			public void removeUpdate(DocumentEvent e) {
				playerName = playerNameTA.getText();
			} // end removeUpdate method

			/*
			 * insertUpdate method:
			 * This procedural method is used when text is added to the text area
			 * 
			 * List of Local Variables
			 * None
			 *
			 * @param e - event when text is added <type DocumentEvent>
			 * 
			 * @return void
			 */
			@Override
			public void insertUpdate(DocumentEvent e) {
				playerName = playerNameTA.getText();
			} // end insertUpdate method

			@Override
			public void changedUpdate(DocumentEvent arg0) {
			}
		}); // end Document Listener

		playerNamePanel.setBorder(new EmptyBorder(5, 0, 5, 0)); // Adds padding around the input area

		JPanel btnPanel = new JPanel(new GridLayout(1, 2)); // Creates a new JPanel with GridLayout
		JButton startBtn = new JButton("Start"); // Creates a new JButton
		JButton exitBtn = new JButton("Exit"); // Creates a new JButton

		startBtn.addActionListener(new ActionListener() { // ActionListener for start button click
			/*
			 * actionPerformed method:
			 * This procedural method is used when the start button is clicked
			 * 
			 * List of Local Variables
			 * None
			 *
			 * @param e - action even when the button is clicked <type ActionEvent>
			 * 
			 * @return void
			 */
			public void actionPerformed(ActionEvent e) {
				if (playerName.equals("")) { // Checks if player name is empty
					playerNameError.setVisible(true); // If the name is empty show the invalid name label
				} else {
					SlimeShooter.contentPanel.startGame(playerName, SlimeShooter.hiscore); // Switch to the gameboard view
				}
			} // end actionPerformed method
		}); // end ActionListener

		exitBtn.addActionListener(new ActionListener() { // ActionListener for exit button click
			/*
			 * actionPerformed method:
			 * This procedural method is used when the start button is clicked
			 * 
			 * List of Local Variables
			 * None
			 *
			 * @param e - action even when the button is clicked <type ActionEvent>
			 * 
			 * @return void
			 */
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			} // end actionPerformed method
		}); // end ActionListener

		// Centers the Labels
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		instTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		instOneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		instTwoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		instThreeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		instFourLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		instFiveLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Adds the components to player name panel
		playerNamePanel.add(playerNameLabel);
		playerNamePanel.add(playerNameTA);
		playerNamePanel.add(emptyLabel);
		playerNamePanel.add(playerNameError);

		btnPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button panel
		btnPanel.add(startBtn); // Add start button to button panel
		btnPanel.add(exitBtn); // add exit button to button panel

		// Add labels and buttons to Menu JPanel
		add(titleLabel);
		add(instTitleLabel);
		add(instOneLabel);
		add(instTwoLabel);
		add(instThreeLabel);
		add(instFourLabel);
		add(instFiveLabel);
		add(playerNamePanel);
		add(btnPanel);
	} // end Menu constructor
} // end Menu class

class GameBoard extends JPanel {
	boolean[][] board = new boolean[11][11];
	int curX, curY;
	String playerName;
	int lives;
	int score;
	int hiscore;
	int level;
	int hit;
	int miss;
	int givenTime;
	int curTime;
	int usedTime;
	String x;
	String y;
	Timer timer;
	GameBoard gameBoard;

	/*
	 * GameBoard constructor:
	 * This procedural method is the constructor for the gameboard. Shows the timer,
	 * level,
	 * lives, current score, hiscore, (x,y) coordinate inputs, and the cartesian
	 * plane with
	 * an slime
	 * 
	 * List of Local Variables
	 * infoPanel - Panel for timer, level, lives, and current score <type JPanel>
	 * timeLabel - label for the timer <type JLabel>
	 * levelLabel - label for the level <type JLabel>
	 * livesLabel - label for the lives <type JLabel>
	 * scoreLabel - label for the current score <type JLabel>
	 * inputPanel - Panel for the (x,y) inputs <type JPanel>
	 * hiscorePanel - Panel for the hiscore <type JPanel>
	 * hiscoreLabel - label to display the hiscore <type JLabel>
	 * xIntPanel - Panel for the x intercept <type JPanel>
	 * xIntLabel - Label for x intercept <type JPanel>
	 * xIntTA - text area for x intercept input <type JTextArea>
	 * yIntPanel - Panel for the y intercept <type JPanel>
	 * yIntLabel - Label for y intercept <type JPanel>
	 * yIntTA - text area for y intercept input <type JTextArea>
	 * shootBtn - button to shoot slimes <type JButton>
	 * quitPanel - Panel for the quit button <type JPanel>
	 * quitBtn - button for quitting the game <type JButton>
	 *
	 * @param givenName - the player name <type String>
	 * givenHiscore - the current hiscore <type int>
	 * 
	 * @return void
	 */
	public GameBoard(String givenName, int givenHiscore) {
		setLayout(new BorderLayout()); // Set gameboard layout to BorderLayout
		setPreferredSize(new Dimension(500, 600)); // Set the gameboard JPanel dimensions

		// initialize every element in board array to false
		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 11; j++) {
				board[i][j] = false;
			}
		} // end for loop

		// Initialize variables
		playerName = givenName;
		lives = 3;
		score = 0;
		hiscore = givenHiscore;
		level = 1;
		hit = 0;
		miss = 0;
		givenTime = 20;
		curTime = 20;
		usedTime = 0;
		timer = new Timer(); // Instantiate a Timer and assign it to timer

		JPanel infoPanel = new JPanel(new GridLayout(1, 4)); // Create a JPanel
		infoPanel.setPreferredSize(new Dimension(500, 25)); // Set the dimensions of the JPanel
		// Create JLabels
		JLabel timeLabel = new JLabel("Time: " + curTime);
		JLabel levelLabel = new JLabel("Level: " + level);
		JLabel livesLabel = new JLabel("Lives: " + lives);
		JLabel scoreLabel = new JLabel("Score: " + score);

		// Set the text colours of the JLabels and the text alignment
		timeLabel.setForeground(Color.BLACK);
		timeLabel.setHorizontalAlignment(JLabel.CENTER);
		levelLabel.setForeground(Color.BLUE);
		levelLabel.setHorizontalAlignment(JLabel.CENTER);
		livesLabel.setForeground(Color.RED);
		livesLabel.setHorizontalAlignment(JLabel.CENTER);
		scoreLabel.setForeground(Color.GREEN);

		JPanel inputPanel = new JPanel(); // Create a JPanel
		inputPanel.setPreferredSize(new Dimension(500, 130)); // Set the dimensions of the JPanel

		JPanel hiscorePanel = new JPanel(); // Create a JPanel
		hiscorePanel.setPreferredSize(new Dimension(500, 25)); // Set the dimensions of the JPanel
		JLabel hiscoreLabel = new JLabel("Hiscore: " + hiscore); // Create a JLabel
		hiscoreLabel.setForeground(Color.GREEN); // Set the colour of the JLabel

		// Create JPanels, JLabels, and JTextAreas
		JPanel xIntPanel = new JPanel(new GridLayout(1, 2));
		JLabel xIntLabel = new JLabel("X-intercept:");
		JTextArea xIntTA = new JTextArea();
		JPanel yIntPanel = new JPanel(new GridLayout(1, 2));
		JLabel yIntLabel = new JLabel("Y-intercept:");
		JTextArea yIntTA = new JTextArea();

		xIntTA.getDocument().addDocumentListener(new DocumentListener() { // DocumentListener for xIntTA
			/*
			 * removeUpdate method:
			 * Procedural method used when text is deleted from text area
			 * 
			 * List of Local Variables
			 * None
			 *
			 * @param e - event when text is deleted <type DocumentEvent>
			 * 
			 * @return void
			 */
			@Override
			public void removeUpdate(DocumentEvent e) {
				x = xIntTA.getText();
			} // end removeUpdate method

			/*
			 * insertUpdate method:
			 * Procedural method used when text is added to the text area
			 * 
			 * List of Local Variables
			 * None
			 *
			 * @param e - event when text is added <type DocumentEvent>
			 * 
			 * @return void
			 */
			@Override
			public void insertUpdate(DocumentEvent e) {
				x = xIntTA.getText();
			} // end insertUpdate method

			@Override
			public void changedUpdate(DocumentEvent arg0) {
			}
		}); // end DocumentListener

		yIntTA.getDocument().addDocumentListener(new DocumentListener() { // DocumentListener for yIntTA
			/*
			 * removeUpdate method:
			 * Procedural method used when text is deleted from text area
			 * 
			 * List of Local Variables
			 * None
			 *
			 * @param e - event when text is deleted <type DocumentEvent>
			 * 
			 * @return void
			 */
			@Override
			public void removeUpdate(DocumentEvent e) {
				y = xIntTA.getText();
			} // end removeUpdate method

			/*
			 * insertUpdate method:
			 * Procedural method used when text is added to the text area
			 * 
			 * List of Local Variables
			 * None
			 *
			 * @param e - event when text is added <type DocumentEvent>
			 * 
			 * @return void
			 */
			@Override
			public void insertUpdate(DocumentEvent e) {
				y = xIntTA.getText();
			} // end insertUpdate method

			@Override
			public void changedUpdate(DocumentEvent arg0) {
			}
		}); // end DocumentListener

		JButton shootBtn = new JButton("Shoot!"); // Creates a JButton
		shootBtn.addActionListener(new ActionListener() { // ActionListener for shoot button click
			/*
			 * actionPerformed method:
			 * This procedural method handles the logic behind shooting an slime.
			 * Calculates the lives, levels, current score, accuracy and averageTime
			 * 
			 * List of Local Variables
			 * x - the user given x position <type int>
			 * y - the user given y position <type int>
			 * accuracy - the user's accuracy when hitting an slime <type int>
			 * avgTime - the user's average time to hit an slime <type int>
			 *
			 * @param e - action even when the button is clicked <type ActionEvent>
			 * 
			 * @return void
			 */
			public void actionPerformed(ActionEvent e) {
				if (isValidParams(xIntTA.getText(), yIntTA.getText())) { // Checks if the input is valid (able to parse
																			// to int)
					int x = Integer.parseInt(xIntTA.getText()); // parse text area string to int
					int y = Integer.parseInt(yIntTA.getText()); // parse text area string to int

					if ((x >= 0 && x <= 10) && (y >= 0 && y <= 10)) { // Checks if the given point is in range 0-10
						if (x == curX && y == curY) { // checks if the given point hits an slime
							hit++; // increase hit count
							System.out.println("SLIME HIT!"); // notify user of a hit
							score += addPoints(givenTime, curTime, level); // increment the score
							usedTime += (givenTime - curTime); // increment the time spent to locate the slime
							curTime = givenTime; // set the current timer to the level time

							timeLabel.setText("Time: " + curTime); // update the timer label
							scoreLabel.setText("Score: " + score); // update the score label
							gameBoard.repaint(); // repaints the gameboard JPanel
						} else { // User fails to hit slime
							lives--; // Decrease the lives counter
							miss++; // Increase miss counter
							System.out.println("Awww MISSED!"); // Notify user of miss

							if (lives < 0) { // Checks if lives counter is less than 0
								int accuracy = Math.round(hit * 100 / (hit + miss)); // Calculates the user accuracy
								int avgTime = (hit == 0) ? 0 : Math.round(usedTime / hit); // Calculates the average
																							// time spent to hit an
																							// slime
								SlimeShooter.contentPanel.showScores(playerName, score, accuracy, avgTime, board); // Show
																													// the
																													// hiscore
																													// list
								return; // Exit the ActionListener
							} // end if statement
							curTime = givenTime; // Set the current time toe the level time

							livesLabel.setText("Lives: " + lives); // update the lives label
							gameBoard.repaint(); // repaint the gameboard JPanel
						} // end if else statement
					} else { // User did not give a point in range 0 -10
						System.out.println("Input out of range! Enter an integer between 0 and 10!"); // Notify the user
																										// of their
																										// invalid input
					} // end if else statement
				} else { // User did not enter a valid input
					System.out.println("Invalid input! Enter an integer for x and y!"); // Notify the user of their
																						// invalid input
				} // end if else statement
				if (hit != 0 && hit % 3 == 0) { // Checks to see if the level should be increased (every 3 slimes hit)
					level++; // increase the level counter
					if (givenTime != 5) { // lowest time is 5 seconds, but level still increases
						givenTime -= 5; // Decrease the level start time
						curTime = givenTime; // set the current timer time to the level time
					} // end if statement

					levelLabel.setText("Level: " + level); // update the level label
					timeLabel.setText("Time: " + curTime); // update the timer label
				} // end if statement
			} // end action performed method
		}); // end ActionListener

		JPanel quitPanel = new JPanel(); // Create JPanel
		quitPanel.setPreferredSize(new Dimension(500, 30)); // Set the JPanel dimensions
		JButton quitBtn = new JButton("Quit"); // Create JButton
		quitBtn.addActionListener(new ActionListener() { // JButton ActionListener
			/*
			 * actionPerformed method:
			 * This procedural method is called when the quit button is clickeed
			 * 
			 * List of Local Variables
			 * None
			 *
			 * @param e - action even when the button is clicked <type ActionEvent>
			 * 
			 * @return void
			 */
			public void actionPerformed(ActionEvent e) {
				System.exit(0); // Exits the game
			} // end actionPerformed method
		}); // end ActionListener

		// Add labels to info panel
		infoPanel.add(timeLabel);
		infoPanel.add(levelLabel);
		infoPanel.add(livesLabel);
		infoPanel.add(scoreLabel);

		// add labels to hiscore Panel and input panels for x, y coordinates
		hiscorePanel.add(hiscoreLabel);
		xIntPanel.add(xIntLabel);
		xIntPanel.add(xIntTA);
		yIntPanel.add(yIntLabel);
		yIntPanel.add(yIntTA);

		// add quit button to quit panel
		quitPanel.add(quitBtn);

		// add above panels to the input panel
		inputPanel.add(hiscorePanel);
		inputPanel.add(xIntPanel);
		inputPanel.add(yIntPanel);
		inputPanel.add(shootBtn);
		inputPanel.add(quitPanel);

		add(infoPanel, BorderLayout.NORTH); // add infoPanel to GameBoard JPanel
		add(inputPanel, BorderLayout.SOUTH); // add inputPanel to GameBoard JPanel

		gameBoard = this; // assign instance to gameBoard
		timer.schedule(new TimerTask() { // instantiate a TimerTask
			/*
			 * run method:
			 * This procedural method is called every time 1 second has passed.
			 * Decreases the curTime counter.
			 * It deducts the lives and increments the miss counter
			 * 
			 * List of Local Variables
			 * None
			 *
			 * @param e - action even when the button is clicked <type ActionEvent>
			 * 
			 * @return void
			 */
			@Override
			public void run() {
				curTime--; // Decreases the current time counter
				timeLabel.setText("Time: " + curTime); // updates the timer label
				if (curTime == 0 && lives == 0) { // Checks if the current time and lives has hit 0
					miss++; // increases the miss counter
					cancel(); // cancels the scheduled timer task
					int accuracy = Math.round(hit * 100 / (hit + miss)); // Calculates the user accuracy when hitting an
																			// slime
					int avgTime = (hit == 0) ? 0 : Math.round(usedTime / hit); // Calcualtes the user's average slime
																				// hit time
					SlimeShooter.contentPanel.showScores(playerName, score, accuracy, avgTime, board); // Shows the
																										// hiscore list
				} else if (curTime == 0) { // Checks if the current time has hit 0
					lives--; // decreases the lives counter
					miss++; // increases the miss counter
					livesLabel.setText("Lives: " + lives); // updates the lives label
					curTime = givenTime; // sets the current time to level time
					gameBoard.repaint(); // repaints the GameBoard JPanel
				} // end if else if statement
			} // end run method
		}, 0, 1000); // end TimeTask scheduler
	} // end GameBoard constructor

	/*
	 * paintComponent method:
	 * This procedural method draws the cartesian plane and the slime dot
	 * 
	 * List of Local Variables
	 * i - the slime x coordinate <type int>
	 * j - the slime y coordinate <type int>
	 * x - the slime dot width <type int>
	 * y - the slime dot height <type int>
	 *
	 * @param g - the JPanel Graphics property <type Graphics>
	 * 
	 * @return void
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g); // Prints the component

		int i, j;

		Graphics2D g2D = (Graphics2D) g; // casts the Graphics variable to Graphics2D
		g.setColor(Color.BLACK); // Sets the paint colour to black

		// Draw the cartesian plane
		for (int x = 40; x <= 400; x += 40) {
			g.drawString(Integer.toString(x / 40), x + 35, 455);
			for (int y = 400; y >= 40; y -= 40) {
				g.drawRect(x, y, 40, 40);
				g.drawString(Integer.toString(11 - y / 40), 25, y + 3);
			} // end for loop
		} // end for loop

		i = (int) Math.round(Math.random() * 10); // Randomly generate i from 0 - 10
		j = (int) Math.round(Math.random() * 10); // Randomly generate j from 0 - 10

		if (board[i][10 - j]) { // Checks if slime already exists at this point
			while (board[i][10 - j]) { // While an slime exists
				i = (int) Math.round(Math.random() * 10); // choose another random i
				j = (int) Math.round(Math.random() * 10); // choose another random j
			} // end while statement
		} // end if statement

		board[i][10 - j] = true; // Set new slime location
		curX = i; // Set new slime x coordinate
		curY = 10 - j; // set new slime y coordinate

		g2D.setColor(Color.GREEN); // set the paint colour to green
		int x = (i + 1) * 40; // calculate the slime width
		int y = (j + 1) * 40; // calculate the slime height
		g2D.fillOval(x - 20, y - 20, 40, 40); // draw the green circle
	} // end paintComponent method

	/*
	 * isValidParams method:
	 * This functional method validates if the given user input can be casted to an
	 * integer
	 * 
	 * List of Local Variables
	 * None
	 *
	 * @param x - user input for x coordinate <type String>
	 * y - user input for the y coordinate <type String>
	 * 
	 * @return boolean
	 */
	private boolean isValidParams(String x, String y) {
		try {
			Integer.parseInt(x); // tries to cast to int
			Integer.parseInt(y); // tries to cast to int
			return true;

		} catch (NumberFormatException e) { // cannot cast
			return false;
		} // end try catch
	} // end isValidParams method

	/*
	 * addPoints method:
	 * This procedural method calculates the amount of points an slime is worth
	 * based on the capture time and level
	 * 
	 * List of Local Variables
	 * base - base slime worth <type int>
	 * multiplier - the score multiplier <type int>
	 * bonus - bonus given on capture time <type int>
	 *
	 * @param givenTime - the start level time <type int>
	 * curTime - the time user spent <type int>
	 * level - the current level <type int>
	 * 
	 * @return int
	 */
	private int addPoints(int givenTime, int curTime, int level) {
		int base = 100;
		int multiplier = level;
		int bonus;

		if (givenTime - curTime < 5) { // Check if user captured within 5 seconds
			bonus = 50; // set bonus to 50
		} else if (givenTime - curTime < 10) { // Check if user captured within 10 seconds
			bonus = 25; // set bonus to 25
		} else if (givenTime - curTime < 15) { // Check if user captured within 15 seconds
			bonus = 10; // set bonus to 10
		} else { // No bonus
			bonus = 0; // set bonus to 0
		} // end if else statement

		return multiplier * base + bonus; // score formula
	} // end addPoints method
} // end GameBoard class

class ScoreScreen extends JPanel {
	String[] names;
	int[] scores;
	int hiscore;
	int size;

	/*
	 * ScoreScreen constructor:
	 * This procedural method is the constructor for score screen
	 * displays the previous hiscores by reading from an input file
	 * 
	 * List of Local Variables
	 * freader - filereader for scores.txt <type FileReader>
	 * br - BufferedReader used to read from file <type BufferedReader>
	 * fwriter - filewriter for scores.txt <type FileWriter>
	 * bo - BufferedWriter used to write to file <type BufferedWriter>
	 * nameTitleLabel - label for name column <type JLabel>
	 * scoreTitelLabel - label for score column <type JLabel>
	 * nameLabel - label for saved name <type JLabel>
	 * scoreLabel - label fore saved score <type JLabel>
	 * tryAgainBtn - button for starting the game again <type JButton>
	 * quitBtn - button for quitting the game <type JButton>
	 *
	 * @param name - the player name <type String>
	 * score - the last game score <type int>
	 * 
	 * @return void
	 */
	public ScoreScreen(String name, int score) {
		setPreferredSize(new Dimension(500, 400)); // set the dimensions of the ScoreScreen JPanel
		setLayout(new GridLayout(12, 2)); // Set the JPanel layout to GridLayout

		// initialize base variables
		names = new String[11];
		scores = new int[11];
		size = 0;
		hiscore = 0;

		// set the names and scores array to empty values
		for (byte i = 0; i < 11; i++) {
			names[i] = "";
			scores[i] = -1;
		} // end for loop

		try {
			FileReader freader = new FileReader("scores.txt"); // Read from file scores.txt
			BufferedReader br = new BufferedReader(freader); // create BufferedReader from file
			String s;

			if ((s = br.readLine()) != null) { // Read first line from file
				hiscore = Integer.parseInt(s); // set hiscore to first line value
			} // end if statement

			while ((s = br.readLine()) != null) { // Continue to read rest of file
				names[size] = s; // assign saved name
				if ((s = br.readLine()) != null) { // Check if next line is empty
					scores[size] = Integer.parseInt(s); // assign saved score

					if (scores[size] > hiscore) { // check if saved score is greater than current hiscore
						hiscore = scores[size]; // update hiscore (want the largest score as hiscore)
					} // end if statement
				} // end if statement
				size++; // Increase array size counter
			} // end while loop

			br.close(); // close the buffered reader
		} catch (NumberFormatException e) { // Catch and parsing errors
			scores[size] = -1; // set to empty value
			size++; // increase size counter
		} catch (IOException e) { // catch and io errors
			System.out.println("Unable to read from 'scores.txt'"); // notify user of io error
		} // end try catch

		names[0] = name; // set the first element of names array to the current player name
		scores[0] = score; // set the first element of scores array to the last game score
		if (score > hiscore) { // check if last game score is greater than hiscore
			hiscore = score; // udpate hiscore
		} // end if statement

		JLabel nameTitleLabel = new JLabel("Name"); // create JLabel
		nameTitleLabel.setHorizontalAlignment(JLabel.CENTER); // center the JLabel text
		JLabel scoreTitleLabel = new JLabel("Score"); // create JLabel
		scoreTitleLabel.setHorizontalAlignment(JLabel.CENTER); // center the JLabel text
		add(nameTitleLabel); // add the JLabel to the ScoreScreen JPanel
		add(scoreTitleLabel); // add the JLabel to the ScoreScreen JPanel

		bubbleSorting(names, scores); // sort the names and scores array

		for (byte i = 10; i >= 1; i--) { // read the array from back to front because of bubble sort
			JLabel nameLabel;
			JLabel scoreLabel;
			if (!names[i].equals("") && scores[i] != -1) { // Check if array element is not empty
				nameLabel = new JLabel(names[i]); // create JLabel for entry name
				scoreLabel = new JLabel("" + scores[i]); // create JLabel for entry score
			} else { // else create filler labels
				nameLabel = new JLabel("-"); // create filler JLabel
				scoreLabel = new JLabel("-"); // create filler JLabel
			} // end if else statement

			nameLabel.setHorizontalAlignment(JLabel.CENTER); // center the name text
			scoreLabel.setHorizontalAlignment(JLabel.CENTER); // center the score text
			add(nameLabel); // add the JLabel to the ScoreScreen JPanel
			add(scoreLabel); // add the JLabel to the ScoreScreen JPanel
		} // end for loop

		JButton tryAgainBtn = new JButton("Try Again!"); // Create try again button
		JButton quitBtn = new JButton("Quit"); // create quit button

		tryAgainBtn.addActionListener(new ActionListener() { // ActionListener for try again button
			/*
			 * actionPerformed method:
			 * This procedural method is used when a button is clicked
			 * 
			 * List of Local Variables
			 * None
			 *
			 * @param e - the event for button click <type ActionEvent>
			 * 
			 * @return void
			 */
			public void actionPerformed(ActionEvent e) {
				SlimeShooter.contentPanel.startGame(name, hiscore); // Replay the game
			} // end actionPeformed method
		}); // end ActionListener

		quitBtn.addActionListener(new ActionListener() { // ActionListener for quite button
			/*
			 * actionPerformed method:
			 * This procedural method is used when a button is clicked
			 * 
			 * List of Local Variables
			 * None
			 *
			 * @param e - the event for button click <type ActionEvent>
			 * 
			 * @return void
			 */
			public void actionPerformed(ActionEvent e) {
				System.exit(0); // quit the game
			} // end actionPerformed method
		}); // end ActionListener

		try {
			FileWriter fwriter = new FileWriter("scores.txt"); // FileWriter for scores.txt
			BufferedWriter bo = new BufferedWriter(fwriter); // Create a BufferedWriter from file

			bo.write(Integer.toString(hiscore)); // write to file
			bo.newLine(); // go to next line

			for (int i = 0; i < 11; i++) { // list array names and scores in ascending ordre
				bo.write(names[i]); // write entry name to file
				bo.newLine(); // go to next line
				bo.write(Integer.toString(scores[i])); // write entry score to file
				bo.newLine(); // go to next line
			} // end for loop
			bo.close(); // close the buffered writer
		} catch (IOException e) { // catch any io errors
			System.out.println("Unable to write to 'scores.txt'"); // notify user of error
		} // end try catch statement

		add(tryAgainBtn); // add try again button to ScoreScreen JPanel
		add(quitBtn); // add quit button to ScoreScreen JPanel
	}

	/*
	 * bubbleSorting method:
	 * This functional method sorts the scores array in ascending order.
	 * Uses the scores array to position names array elements
	 * 
	 * List of Local Variables
	 * tempStr - used to store temporary name value <type String>
	 * tempNum - used to store temporary score value <type int>
	 *
	 * @param names - the names array <type String[]>
	 * num - the scores array <type int[]>
	 * 
	 * @return int[]
	 */
	public int[] bubbleSorting(String names[], int num[]) {
		String tempStr;
		int tempNum;
		// Bubble sort
		for (byte i = 0; i < 10; i++) {
			for (byte j = 0; j < 10; j++) {
				if (num[j] > num[j + 1]) {
					tempStr = names[j];
					tempNum = num[j];
					num[j] = num[j + 1];
					num[j + 1] = tempNum;
					names[j] = names[j + 1]; // assigns names array based on scores bubble sort
					names[j + 1] = tempStr;
				} // end if statement
			} // end for statement
		} // end for statement
		return num;
	} // end bubbleSorting method
} // end ScoreScreen class

class FinalBoard extends JPanel {
	boolean[][] board;

	/*
	 * FinalBoard method:
	 * This procedural method is a constructor for FinalBoard. Displays the location
	 * of all slimes
	 * in the previous game.
	 * 
	 * List of Local Variables
	 * finalBoardLabel - the label title for final board <type JLabel>
	 *
	 * @param givenBoard - the board with the stored slimes <type boolean[][]>
	 * 
	 * @return void
	 */
	public FinalBoard(boolean[][] givenBoard) {
		setPreferredSize(new Dimension(500, 575)); // sets the FinalBoard JPanel dimensions
		JLabel finalBoardLabel = new JLabel("  Final Board:"); // creates a JLabel
		board = givenBoard; // assigns givenBoard to board
		add(finalBoardLabel); // adds the JLabel to FinalBoard JPanel
	}

	/*
	 * paintComponent method:
	 * This procedural method draws the cartesian plane and all the slime dots from
	 * the previous game
	 * 
	 * List of Local Variables
	 * x - the slime dot width <type int>
	 * y - the slime dot height <type int>
	 *
	 * @param g - the JPanel Graphics property <type Graphics>
	 * 
	 * @return void
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g); // prints the component

		Graphics2D g2D = (Graphics2D) g; // casts Graphics to Graphics2D
		g.setColor(Color.BLACK); // sets the paint color to black

		// Draws the cartesian plane
		for (int x = 40; x <= 400; x += 40) {
			g.drawString(Integer.toString(x / 40), x + 35, 455);
			for (int y = 400; y >= 40; y -= 40) {
				g.drawRect(x, y, 40, 40);
				g.drawString(Integer.toString(11 - y / 40), 25, y + 3);
			} // end for loop
		} // end for loop

		// Draws all the slimes from the previous game
		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 11; j++) {
				if (board[i][j]) { // draw circle if slime existed at point
					g2D.setColor(Color.GREEN); // sets the paint color to green
					int x = (i + 1) * 40; // calculates the circle width
					int y = (10 - j + 1) * 40; // calculates the circle height
					g2D.fillOval(x - 20, y - 20, 40, 40); // draws to circle
				} // end if statement
			} // end for loop
		} // end for loop
	} // end paintComponent method
} // end FinalBoard class
