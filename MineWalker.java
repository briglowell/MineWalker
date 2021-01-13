/**
 * Runs MineWalkerPanel
 * The program is a game where the objective is to make it alive from the 
 * start (bottom left corner) to the finish(top right), and achieve 
 * the highest score you can. You start with a grid of buttons that you 
 * can change, a starting amount of lives, and a starting score. 
 * You can only step in a direction directly north, south, east, or west. 
 * 1/4 the grid is hidden with explosive mines.
 * Stepping on mines loses lives and score. In addition the score is 
 * continually dropping, so speed and precision are required to get the 
 * best score.
 * @author briglowell cs121-6
 *
 */
public class MineWalker {
	
	private static MineWalkerPanel myApp;
	
	public static void main(String[] args) {
		myApp = new MineWalkerPanel();
	}
	
	
}
