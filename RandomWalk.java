import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
/**
 * Creates a random path of points from bottom left corner of grid to
 * upper right corner of grid.
 * @author BrigLowell
 *
 */
public class RandomWalk {
	Random rand;
	boolean done = false;
	ArrayList<Point> path;
	int size;
	int x;
	int y; 
	Point start;
	
/**
 * 
 * @param gridSize
 * @param seed
 *  RandomWalk method like below except specifying specifying random seed 
 * 
 */
	public RandomWalk(int gridSize, long seed) {
		size = gridSize;
		rand = new Random(seed);
		path = new ArrayList<Point>();
		Point start = new Point(0,size-1);
		path.add(start);
		x = start.x;
		y = start.y;
	
	}

	/**
	 * 
	 * @param gridSize
	 *  Initializes variables but does not specify a parameter for the 
	 *  random number generator. 
	 */
	public RandomWalk(int gridSize) {
		size = gridSize;
		rand = new Random();
		path = new ArrayList<Point>();
		Point start = new Point(0,size-1);
		path.add(start);
		x = start.x;
		y = start.y;
		
	}
	
	/**
	 * This method calls our step method until it is done
	 */
	public void createWalk() {
		
		while(!isDone()) {
			step();
		}

	}

/**
 * This method continues to plot out our grid until we reach the top 
 * right corner of the grip map  
 *
 */
	public void step() {
		int step = rand.nextInt(2);
		
		if (step==0) {
			
			Point moveRight = new Point(++x,y);
			
			if(x<=size-1) {
				path.add(moveRight);
			}
			else --x;
		}
		else { 
			Point topStep = new Point(x,--y);
			
			if(y>=0) {
				path.add(topStep);
			}
			else ++y;
		}
		if (x==size-1 && y==0) {
			setDone(true);
		}
		
		
	}
	
	/**
	 * Returns the done statement
	 * @return
	 */
	public boolean isDone() {
		
		return done;
	}
	/**
	 *  Set the parameter done depending on when the path is 
	 *  complete 
	 * @param done
	 */
	public void setDone(boolean done) {
		this.done = done;
		
	}
	/**
	 * Returns the path to the array list
	 * @return path
	 */
	public ArrayList<Point> getPath() {
		return path;
		
	}
	/** 
	 * Prints the points on the grid list using a for each loop
	 * @return pathString
	 */
	public String toString() {
		String pathString = "";
		for(Point point : path) {
			pathString = pathString + "[ " + point.getX() + ", " + point.getY() + " ]";
		}
		return pathString;
	}

}