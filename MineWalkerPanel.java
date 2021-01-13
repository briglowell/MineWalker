import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Color;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.BorderFactory;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
/**
 * The program is a game where the objective is to make it alive from the 
 * start (bottom left corner) to the finish(top right), and achieve 
 * the highest score you can. You start with a grid of buttons that you 
 * can change, a starting amount of lives, and a starting score. 
 * You can only step in a direction directly north, south, east, or west. 
 * 1/4 the grid is hidden with explosive mines.
 * Stepping on mines loses lives and score. In addition the score is 
 * continually dropping, so speed and precision are required to get the 
 * best score.
 * @author briglowell CS121-6
 *
 */
public class MineWalkerPanel {
	private final int startScore = 1000;
	private int bonusScore = 1000;
	private int score = startScore;
	private final int startLives = 5;
	private int lives = 5;
	private int gridSize = 50;
	private static final int originalGridSize = 10;
	private int DELAY = 2000;
	
	//color array list to use for color when nearby mines
	Color colors[] = new Color[]{Color.GREEN,Color.YELLOW,Color.ORANGE,Color.RED};
	
	//color key text areas
	JTextArea textArea = new JTextArea("0 NearBy Mines",2,2);
	JTextArea textArea2 = new JTextArea("1 NearBy Mines",2,2);
	JTextArea textArea3 = new JTextArea("2 NearBy Mines",2,2);
	JTextArea textArea4 = new JTextArea("3 NearBy Mines",2,2);
	JTextArea textArea5 = new JTextArea("Exloded Mines",2,2);
	JTextArea nullPanel = new JTextArea(4,2);
	JTextArea textArea6 = new JTextArea("Start",2,2);
	JTextArea textArea7 = new JTextArea("Finish",2,2);
	
	//gridSize text field
	JTextField numGridField;
	
	//center and main panel global:
	JPanel centerPanel = new JPanel();
	JPanel mainPanel = new JPanel();
	
	// Array list using points to write methods.
	ArrayList<Point> points;
	ArrayList<Point> mines;
	ArrayList<Point> quit;
	Random rand = new Random();
	
	//Control menu buttons:
	JButton newGame = new JButton("NewGame");
	JButton quitGame = new JButton("QuitGame");
	JButton showMines = new JButton("ShowMines");
	JButton showPath = new JButton("ShowPath");
	JButton gridButton[][] = new JButton[gridSize][gridSize];
	//JButton difficultyButton = new JButton("Difficulty");
	
	
	//JLabels:
	JLabel livesLabel = new JLabel("Lives: " +lives);
	JLabel scoreLabel = new JLabel("Score: " +score);
	JFrame mainFrame;
	
	// Timer that fires off every second
	private static final int ONESECOND = 1000;
	Timer theTimer = new Timer(ONESECOND, new TimerActionListener());
	
	/**
	 *  This sets up the entire program and instantiates variables and methods.
	 *  It creates the frames and panels, sets buttons to all of them as well.
	 *  
	 */
	public MineWalkerPanel() {
		//builds frame
		mainFrame = new JFrame("Mine Walker");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setMinimumSize(new Dimension(600,300));
		
		// main panel set to content pane with border layout
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainFrame.setContentPane(mainPanel);
		
		//instantiates textfield for customizing gridSize
		numGridField = new JTextField();
		
		//grid layout center panel and adds grid buttons
		createGrid();
		//creates a random path so there is always a successul route
		createPath();
		//implements the created mines to the center panel
		createMines();
		
		//sets panel to borderlayout center
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		// creates the eastPanel for score and lives labels
		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
		eastPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		eastPanel.add(Box.createVerticalGlue());
		eastPanel.add(livesLabel);
		eastPanel.add(scoreLabel);
		eastPanel.add(Box.createVerticalGlue());
		
		//sets the east panel to the main panel
		mainPanel.add(eastPanel, BorderLayout.EAST);
		
		// creates westPanel text areas for color key 
		JPanel westPanel = new JPanel();
		westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
		westPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		westPanel.setBorder(BorderFactory.createTitledBorder("Mine Key"));
		westPanel.add(textArea);
		textArea.setBackground(Color.GREEN);
		textArea.setEditable(false);
		westPanel.add(textArea2);
		textArea2.setBackground(Color.YELLOW);
		textArea2.setEditable(false);
		westPanel.add(textArea3);
		textArea3.setBackground(Color.ORANGE);
		textArea3.setEditable(false);
		westPanel.add(textArea4);
		textArea4.setBackground(Color.RED);
		textArea4.setEditable(false);
		westPanel.add(textArea5);
		textArea5.setBackground(Color.BLACK);
		textArea5.setForeground(Color.WHITE);
		textArea5.setEditable(false);
		westPanel.add(nullPanel);
		nullPanel.setBackground(Color.WHITE);
		nullPanel.setEditable(false);
		westPanel.add(textArea6);
		textArea6.setBackground(Color.BLUE);
		textArea6.setForeground(Color.WHITE);
		textArea6.setEditable(false);
		westPanel.add(textArea7);
		textArea7.setBackground(Color.MAGENTA);
		textArea7.setForeground(Color.WHITE);
		textArea7.setEditable(false);
		
		//sets the west panel to the main panel
		mainPanel.add(westPanel, BorderLayout.WEST);
		
		//creates southPanel with control buttons
		JPanel southPanel = new JPanel();
		southPanel.setBackground(Color.GRAY);
		southPanel.add(newGame);
		newGame.addActionListener(new controlButtonListener());
		southPanel.add(quitGame);
		quitGame.addActionListener(new controlButtonListener());
		//southPanel.add(difficultyButton);
		//difficultyButton.addActionListener(new controlButtonListener());
		southPanel.add(showMines);
		showMines.addActionListener(new controlButtonListener());
		southPanel.add(numGridField);
		numGridField.setEditable(true);
		numGridField.setPreferredSize(new Dimension(20,20));
		numGridField.addActionListener(new gridSizeTextField());
		southPanel.add(showPath);
		showPath.addActionListener(new controlButtonListener());
		
		//sets the south panel to the main panel
		mainPanel.add(southPanel, BorderLayout.SOUTH);
		
		// shows visible
		mainFrame.pack();
		mainFrame.setVisible(true);
	}
	/**
	 * Method to create the grid of jbuttons for center panel.
	 * 
	 */
	public void createGrid() {
		gridSize = originalGridSize;
		numGridField.setText(Integer.toString(gridSize));
		centerPanel.setLayout(new GridLayout(gridSize, gridSize));
		for (int row = 0; row < gridSize; row++) {
			for (int col = 0; col < gridSize; col++) {
				gridButton[row][col] = new JButton();
				gridButton[row][col].setOpaque(true);
				gridButton[row][col].setEnabled(false);
				gridButton[row][col].addActionListener(new gridButtonListener());
				centerPanel.add(gridButton[row][col]);
			}
		}
		gridButton[0][gridSize-1].setBackground(Color.MAGENTA);
		gridButton[gridSize-1][0].setBackground(Color.BLUE);
		gridButton[gridSize-2][0].setEnabled(true);
		gridButton[gridSize-1][1].setEnabled(true);
		gridButton[gridSize-1][0].setText("x");
		gridButton[gridSize-1][0].setForeground(Color.WHITE);
		theTimer.start();
	}
	/**
	 * Method to remove the grid in the center panel, 
	 * then recreate new grid in center panel
	 */
	public void newGrid() {
		centerPanel.removeAll();
		createGrid();
		createPath();
		createMines();
	}
	
	/**
	 * Method to create a walk and implement the random walk on the grid
	 */
	public void createPath() {
		RandomWalk newPath = new RandomWalk((int) gridSize);
		newPath.createWalk();
	    points = newPath.getPath();
	}
	/**
	 * Method to create a path and color it cyan. will show path when button 
	 * is used.
	 */
	public void showPath() {
		
		for(Point p: points) {
			gridButton[(int) p.getX()][(int)p.getY()].setBackground(Color.CYAN);
		}
	}
	/**
	 * Method to set the path back to normal after hide path button is used.
	 */
	public void hidePath() {
		for(Point p: points) {
			gridButton[(int) p.getX()][(int)p.getY()].setBackground(null);
		}
	}
	/**
	 * Method to calculate random coordinates for mines 1/4 size of grid
	 * Avoids creating mines on path, and trying to avoid duplicating
	 * mine location
	 */
	public void createMines() {
		mines = new ArrayList<Point>();
		for (int i = 0; i < (gridSize*gridSize)/4; i++) {
			int x = rand.nextInt(gridSize);
			int y = rand.nextInt(gridSize);
			Point mine = new Point(x,y);
			if (!points.contains(mine)) {
				if (!mines.contains(mine)) {
					mines.add(mine);
				}
				else if (mines.contains(mine)){
					x = rand.nextInt(gridSize);
					y = rand.nextInt(gridSize);
					mine = new Point(x,y);
					if(!points.contains(mine) && !mines.contains(mine)) {
						mines.add(mine);
					}
				}
			}
			while (points.contains(mine)){
				x = rand.nextInt(gridSize);
				y = rand.nextInt(gridSize);
				mine = new Point(x,y);
				if(!points.contains(mine) && !mines.contains(mine)) {
					mines.add(mine);
				}
			}
		}
	}
	/**
	 * Method to show created mines with black background when showmines
	 * button clicked. sets background color of gridbuttons to black
	 */
	public void showMines() {
		for (Point p: mines) {
			gridButton[(int) p.getX()][(int)p.getY()].setBackground(Color.BLACK);
		}
	}
	/**
	 * Method to hide mines and return to default after show mines 
	 * is clicked again
	 */
	public void hideMines() {
		for (Point p: mines) {
			gridButton[(int) p.getX()][(int)p.getY()].setBackground(null);
		}
	}
	/**
	 * Method to take input from text field from 5-50 and adjust the gridsize to the user input
	 * If input is not in those parameters, then it will set to original gridsize of 10.
	 * 
	 */
	private void userGridSize(){
		int oldGridSize = gridSize;
		try{
			gridSize = Integer.parseInt(numGridField.getText());
			//if grid size is outside parameters, it just sets gridSize to previous gridSize used.
			if(gridSize < 5){
				gridSize = oldGridSize;
				numGridField.setText(Integer.toString(gridSize));
			}else if(gridSize > 50){
				gridSize = oldGridSize;
				numGridField.setText(Integer.toString(gridSize));
			}
			// code to adjust: starting score, lives, and bonus score higher/lower based on gridsize:
			else{
				if(gridSize >= 5 && gridSize < 10){
					score = startScore/2;
					scoreLabel.setText("Score: " + score);
					lives = (startLives+1)/2;
					livesLabel.setText("Lives " + lives);
					bonusScore = bonusScore/2;
				}else if(gridSize >=15 && gridSize <20){
					score = startScore*4/3;
					scoreLabel.setText("Score: " + score);
					lives = (startLives)*4/3;
					livesLabel.setText("Lives " + lives);
					bonusScore = bonusScore*4/3;
				}else if (gridSize >=20 && gridSize<30){
					score = startScore*5/3;
					scoreLabel.setText("Score: " + score);
					lives = (startLives)*5/3;
					livesLabel.setText("Lives " + lives);
					bonusScore = bonusScore*5/3;
				}else if (gridSize >=30 && gridSize<40){
					score = startScore*2;
					scoreLabel.setText("Score: " + score);
					lives = (startLives)*2;
					livesLabel.setText("Lives " + lives);
					bonusScore = bonusScore*2;
				}else if (gridSize >=40 && gridSize<50){
					score = startScore*7/3;
					scoreLabel.setText("Score: " + score);
					lives = (startLives)*7/3;
					livesLabel.setText("Lives " + lives);
					bonusScore = bonusScore*3;
				}else if (gridSize ==50){
					score = startScore*8/3;
					scoreLabel.setText("Score: " + score);
					lives = (startLives)*8/3;
					livesLabel.setText("Lives " + lives);
					bonusScore = bonusScore*5;
				}
				if(mainPanel != null){
					mainPanel.remove(centerPanel);
				}
				centerPanel = new JPanel();
				centerPanel.setLayout(new GridLayout(gridSize,gridSize));
				mainFrame.setMinimumSize(new Dimension(600,300));
				for (int row = 0; row < gridSize; row++) {
					for (int col = 0; col < gridSize; col++) {
						gridButton[row][col] = new JButton();
						gridButton[row][col].setOpaque(true);
						gridButton[row][col].setEnabled(false);
						gridButton[row][col].addActionListener(new gridButtonListener());
						centerPanel.add(gridButton[row][col]);
					}
				}
				mainPanel.add(centerPanel,BorderLayout.CENTER);
				mainPanel.revalidate();
			}
		}catch (NumberFormatException nfe){
			gridSize = oldGridSize;
			numGridField.setText(Integer.toString(gridSize));
		}
		
	}
	/**
	 * Method to detect nearby mines and return an int equal to number of mines nearby.
	 * Uses Points to achieve this.
	 * @Returns counter (which is number of nearby mines in east,north,south,west direction.)
	 */
	public int minesNearby(int x, int y) {
		int counter = 0;
		Point north = new Point(x-1,y);
		Point east = new Point(x,y+1);
		Point west = new Point(x,y-1);
		Point south = new Point(x+1,y);
		if(mines.contains(north)){
			counter++;
		}
		if(mines.contains(east)){
			counter++;
		}
		if(mines.contains(south)){
			counter++;
		}
		if(mines.contains(west)){
			counter++;
		}
		return counter;	
	}
	/**
	 * Action Listener/Performed for pressing a grid button.
	 * Depending on gridButton pressed, changes color, enables nearby buttons, 
	 * adjusts score, adjusts lives, and ends game if won.
	 * @author briglowell
	 *
	 */
	private class gridButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton clickedJButton = (JButton) e.getSource();
			// if last button is clicked, then stop timer, add up score, and win pop up window with final score.
			if (gridButton[0][gridSize-1]==clickedJButton){
				theTimer.stop();
				score += bonusScore;
				scoreLabel.setText("Score: " + score);
				JOptionPane.showMessageDialog(null, "YOU MADE IT! \nFinal Score: " +score + "\nLives Remaining: " + lives);
				showMines();
				for (int row = 0; row < gridSize; row++) {
					for (int col = 0; col < gridSize; col++) {
						gridButton[row][col] = new JButton();
						gridButton[row][col].setOpaque(true);
						gridButton[row][col].setEnabled(false);
						centerPanel.add(gridButton[row][col]);
					}
				}
			}
			for (int row = 0; row < gridSize; row++) {
				for (int col = 0; col < gridSize; col++) {
					if(gridButton[row][col] == clickedJButton){
						// sets background color based on minesNearby method and colors array.
						gridButton[row][col].setBackground(colors[minesNearby(row,col)]);
						gridButton[row][col].setText("x");
						Point temp = new Point (row,col);
						
						// adjusts score and mines when a mine is clicked
						if(mines.contains(temp)){
							gridButton[row][col].setBackground(Color.BLACK);
							gridButton[row][col].setEnabled(false);
							lives -= 1;	
							score -= 100;
							//mine popup window if lives are not zero.
							if(lives > 0){
								livesLabel.setText("Lives: " +lives);
								scoreLabel.setText("Score: " + score);
								JOptionPane.showMessageDialog(null, "BOOM! \nLives remaining: "+lives);
							// Dead popup window if lives are 0
							}else if(lives == 0){
								JOptionPane.showMessageDialog(null, "YOU DEAD!! \nFinal Score: " + score + "\nNo lives remaining!");
								theTimer.stop();
								showMines();
								for (int i = 0; i < gridSize; i++) {
									for (int j = 0; j < gridSize; j++) {
										gridButton[i][j] = new JButton();
										gridButton[i][j].setOpaque(true);
										gridButton[i][j].setEnabled(false);
										centerPanel.add(gridButton[row][col]);
									}
								}
							}
						}else {
							// when button is clicked, score is adjusted and only neighboring not yet clicked buttons are enabled
							score -=10;
							scoreLabel.setText("Score: " + score);
							if(row + 1< gridSize){
								if(gridButton[row+1][col].getText()!="x"){
								gridButton[row+1][col].setEnabled(true);
								}
								gridButton[row][col].setEnabled(false);
							}
							if(row - 1>= 0){
								if(gridButton[row-1][col].getText()!="x"){
								gridButton[row-1][col].setEnabled(true);
								}
								gridButton[row][col].setEnabled(false);
							}
							if(col + 1< gridSize){
								if(gridButton[row][col+1].getText()!="x"){
								gridButton[row][col+1].setEnabled(true);
								}
								gridButton[row][col].setEnabled(false);
							}
							if(col - 1>= 0){
								if(gridButton[row][col-1].getText()!="x"){
								gridButton[row][col-1].setEnabled(true);
								}
								gridButton[row][col].setEnabled(false);
							}
						}
					}
				}
			}
		}
	}
	/**
	 * Action Listener/Performed for when someone inputs gridsize in textField
	 * Resets game to gridsize input from user.
	 * @author briglowell
	 *
	 */
	private class gridSizeTextField implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			userGridSize();
			createPath();
			createMines();
			showMines.setText("ShowMines");
        	showPath.setText("ShowPath");
        	gridButton[0][gridSize-1].setBackground(Color.MAGENTA);
    		gridButton[gridSize-1][0].setBackground(Color.BLUE);
    		gridButton[gridSize-2][0].setEnabled(true);
    		gridButton[gridSize-1][1].setEnabled(true);
    		gridButton[gridSize-1][0].setText("x");
    		gridButton[gridSize-1][0].setForeground(Color.WHITE);
		}
	}
	/**
	 * action Listener/Performed for pressing one of the bottom Control buttons
	 * Depending on which control button pressed, ends game, starts new game,
	 * adjusts score, shows/hides good path, shows/hides mines.
	 * @author briglowell
	 *
	 */
	private class controlButtonListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e){
			Object buttonClicked = e.getSource();
			//newgame button clicked resets everything.
	        if(buttonClicked == newGame) {
	            showMines.setText("ShowMines");
	            showPath.setText("ShowPath");
	            System.out.println("New game button pressed");
	            newGrid();
	            lives = 5;
	            score = startScore;
	            livesLabel.setText("Lives: "+lives);
	            scoreLabel.setText("Score: " +score);
	         }
	        // quitgame button clicked pops up quit menu, gives score and lives, resets game.
	         else if (buttonClicked == quitGame){
	        	theTimer.stop();
	        	showMines();
	            JOptionPane.showMessageDialog(null, "You Quit! \nFinal Score: " + score + "\nFinal Lives Remaining: " + lives);
	            showMines.setText("ShowMines");
	            showPath.setText("ShowPath");
	            newGrid();
	            lives = 5;
	            score = startScore;
	            livesLabel.setText("Lives: "+lives);
	            scoreLabel.setText("Score: " +score);
	            }
	       /*
	        else if(buttonClicked == difficultyButton) {
	            System.out.println("difficulty button pressed."); 
	        }*/
	        else if(buttonClicked == showMines) {
	            //shows all mines on grid, adjusts score if pressed.
	            if(showMines.getText() == "ShowMines"){
	            	showMines.setText("HideMines");
	            	showMines();
	            	score = score/2;
	            	bonusScore = bonusScore/4;
					scoreLabel.setText("Score: " + score);
	            }
	            else if (showMines.getText() == "HideMines"){
	            	showMines.setText("ShowMines");
	            	hideMines();
	            }
	        }
	        else if(buttonClicked == showPath) {
	    // shows the safe route, adjusts score if pressed
	            if(showPath.getText() == "ShowPath"){
	            	showPath.setText("HidePath");
	            	System.out.println("show path pressed");
	            	showPath();
	            	score = score/2;
	            	bonusScore = bonusScore/4;
					scoreLabel.setText("Score: " + score);
	            	gridButton[0][gridSize-1].setBackground(Color.MAGENTA);
	        		gridButton[gridSize-1][0].setBackground(Color.BLUE);
	            }
	            else if (showPath.getText() == "HidePath"){
	            	showPath.setText("ShowPath");
	            	System.out.println("hide path pressed");
	            	hidePath();
	            	gridButton[0][gridSize-1].setBackground(Color.MAGENTA);
	        		gridButton[gridSize-1][0].setBackground(Color.BLUE);
	            }
	        }
		}
	}	
	/**
     * Performs action when timer event fires.
     * Lowers the score by 1 for every second. 
     * @author Brig Lowell
     */
	private class TimerActionListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent evt){
			score = score -1;
			scoreLabel.setText("Score: " +score);
		}
	}

   /**
    * Create an animation thread that runs periodically, didn't end up using.
    */
    private void startAnimation()
    {
	    TimerActionListener taskPerformer = new TimerActionListener();
	    new Timer(DELAY, taskPerformer).start();
    }
}