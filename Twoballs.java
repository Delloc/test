package lab4;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

/**
 *
 * @author Joachim Parrow 2010 rev 2011, 2012, 2013, 2015, 2016
 *
 *         Simulator for two balls
 */
public class Twoballs {
	final static int UPDATE_FREQUENCY = 100; // Global constant: fps, ie times per second to simulate

	public static void main(String[] args) {
		JFrame frame = new JFrame("No collisions!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Table table = new Table();
		frame.add(table);
		frame.pack();
		frame.setVisible(true);
	}
}

/**
 * 
 ***********************************************************************************
 ******
 * Coord
 *
 * A coordinate is a pair (x,y) of doubles. Also used to represent vectors. Here
 * are various utility methods to compute with vectors.
 *
 *
 */
class Coord {
	double x, y;

	Coord(double xCoord, double yCoord) {
		x = xCoord;
		y = yCoord;
	}

	Coord(MouseEvent event) { // Create a Coord from a mouse event
		x = event.getX();
		y = event.getY();
	}

	static final Coord ZERO = new Coord(0, 0);

	double magnitude() {
		return Math.sqrt(x * x + y * y);
	}

	Coord norm() { // norm: a normalised vector at the same direction
		return new Coord(x / magnitude(), y / magnitude());
	}

	void increase(Coord c) {
		x += c.x;
		y += c.y;
	}

	void decrease(Coord c) {
		x -= c.x;
		y -= c.y;
	}

	static double scal(Coord a, Coord b) { // scalar product
		return a.x * b.x + a.y * b.y;
	}

	static Coord sub(Coord a, Coord b) {
		return new Coord(a.x - b.x, a.y - b.y);
	}

	static Coord mul(double k, Coord c) { // multiplication by a constant
		return new Coord(k * c.x, k * c.y);
	}

	static double distance(Coord a, Coord b) {
		return Coord.sub(a, b).magnitude();
	}

	static void paintLine(Graphics2D graph2D, Coord a, Coord b) { // paint line between points
		graph2D.setColor(Color.black);
		graph2D.drawLine((int) a.x, (int) a.y, (int) b.x, (int) b.y);
	}
}

/**
 * 
 ***********************************************************************************
 *****
 * Table
 *
 * The table has some constants and instance variables relating to the graphics
 * and the balls. When simulating the balls it starts a timer which fires
 * UPDATE_FREQUENCY times per second. Each time the timer is activated one step
 * of the simulation is performed. The table reacts to events to accomplish
 * repaints and to stop or start the timer.
 *
 */
class Table extends JPanel implements MouseListener, MouseMotionListener, ActionListener {
	public final static int TABLE_WIDTH = 800;
	public final static int TABLE_HEIGHT = 500;
	public final static int WALL_THICKNESS = 20;
	private final static  int NUMBER_OF_BALLS = 100;
	private final Color COLOR = Color.darkGray;
	private final Color WALL_COLOR = Color.black;
	public static Ball[] balls = new Ball[NUMBER_OF_BALLS];
	private final Timer simulationTimer;

	Table() {

		setPreferredSize(new Dimension(TABLE_WIDTH + 2 * WALL_THICKNESS, TABLE_HEIGHT + 2 * WALL_THICKNESS));
		createInitialBalls();

		addMouseListener(this);
		addMouseMotionListener(this);
		simulationTimer = new Timer((int) (1000.0 / Twoballs.UPDATE_FREQUENCY), this);
	}

	private void createInitialBalls() {
		/*
		final Coord firstInitialPosition = new Coord(100, 100);
		final Coord secondInitialPosition = new Coord(200, 200);
		ball1 = new Ball(firstInitialPosition);
		ball2 = new Ball(secondInitialPosition);
		*/
		
		for (int i = 0; i < NUMBER_OF_BALLS; i++) {
			Random rand = new Random();
			int initx = rand.nextInt(TABLE_WIDTH/2 - 30);
			int inity = rand.nextInt(TABLE_HEIGHT - 30);
			initx += 30;
			inity += 30;
			
			
			final Coord InitialPosition = new Coord(initx, inity);
			balls[i] = new Ball(InitialPosition);
		}
		
		
	}

	public void actionPerformed(ActionEvent e) { // Timer event
		
		/*
		ball1.move();
		ball2.move();
		repaint();
		if (!ball1.isMoving() && !ball2.isMoving()) {
			simulationTimer.stop();
		}
		 */
		
		
		for (int i = 0; i < NUMBER_OF_BALLS; i++) {
			balls[i].move();
		}
		
		repaint();
		
		
		int balls_stopped = 0;
		boolean all_balls_stopped = false;
		
		for (int i = 0; i < NUMBER_OF_BALLS; i++) {
			if (!balls[i].isMoving()) {
				balls_stopped++;
			}
		}
		
		if (balls_stopped == NUMBER_OF_BALLS) {
			all_balls_stopped = true;
		}
		
		
		
		if (all_balls_stopped) {
			simulationTimer.stop();
		}
		
	}

	public void mousePressed(MouseEvent event) {
		
		/*
		Coord mousePosition = new Coord(event);
		ball1.setAimPosition(mousePosition);
		ball2.setAimPosition(mousePosition);
		repaint(); // To show aiming line
		*/
		
		
		Coord mousePosition = new Coord(event);
		
		for (int i = 0; i < NUMBER_OF_BALLS; i++) {
			balls[i].setAimPosition(mousePosition);
		}
		
		repaint(); // To show aiming line
		
		
	}

	public void mouseReleased(MouseEvent e) {
		/*
		ball1.shoot();
		ball2.shoot();
		if (!simulationTimer.isRunning()) {
			simulationTimer.start();
		}
		*/
		
		
		for (int i = 0; i < NUMBER_OF_BALLS; i++) {
			balls[i].shoot();
		}

		if (!simulationTimer.isRunning()) {
			simulationTimer.start();
		}
		
		
	}

	public void mouseDragged(MouseEvent event) {
		/*
		Coord mousePosition = new Coord(event);
		ball1.updateAimPosition(mousePosition);
		ball2.updateAimPosition(mousePosition);
		repaint();
		*/
		
		
		Coord mousePosition = new Coord(event);
		
		for (int i = 0; i < NUMBER_OF_BALLS; i++) {
			balls[i].updateAimPosition(mousePosition);
		}
		
		repaint();
		
	}

// Obligatory empty listener methods
	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g2D = (Graphics2D) graphics;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // This makes the graphics smoother
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.setColor(WALL_COLOR);
		g2D.fillRect(0, 0, TABLE_WIDTH + 2 * WALL_THICKNESS, TABLE_HEIGHT + 2 * WALL_THICKNESS);
		g2D.setColor(COLOR);
		g2D.fillRect(WALL_THICKNESS, WALL_THICKNESS, TABLE_WIDTH, TABLE_HEIGHT);
		
		/*
		ball1.paint(g2D);
		ball2.paint(g2D);
		*/
		
		for (int i = 0; i < NUMBER_OF_BALLS; i++) {
			balls[i].paint(g2D);
		}
		
	}
} // end class Table

/**
 * 
 ***********************************************************************************
 *****
 * Ball:
 *
 * The ball has instance variables relating to its graphics and game state:
 * position, velocity, and the position from which a shot is aimed (if any).
 * 
 */
class Ball {
	private final Color COLOR = Color.white;
	private final int BORDER_THICKNESS = 1;
	private final double RADIUS = 4;
	private final double DIAMETER = 2 * RADIUS;
	private final double FRICTION = 0.0; // its friction constant (normed for 100 updates/second)
	private final double FRICTION_PER_UPDATE = // friction applied each simulation step
			1.0 - Math.pow(1.0 - FRICTION, // don't ask - I no longer remember how I got to this
					100.0 / Twoballs.UPDATE_FREQUENCY);
	private Coord position;
	private Coord velocity;
	private Coord aimPosition; // if aiming for a shot, ow null

	Ball(Coord initialPosition) {
		position = initialPosition;
		
		
		Random rand = new Random(); 
		int velx = rand.nextInt(10);
		int vely = rand.nextInt(10);
		velx -= 5;
		vely -= 5;
		
		velocity = new Coord(velx,vely); // WARNING! Are initial velocities
		//velocity = Coord.ZERO;
	} // clones or aliases? Is this important?

	private boolean isAiming() {
		return aimPosition != null;
	}

	boolean isMoving() { // if moving too slow I am deemed to have stopped
		return velocity.magnitude() > FRICTION_PER_UPDATE;
	}

	void setAimPosition(Coord grabPosition) {
		if (Coord.distance(position, grabPosition) <= RADIUS) {
			aimPosition = grabPosition;
		}
	}

	void updateAimPosition(Coord newPosition) {
		if (isAiming()) {
			aimPosition = newPosition;
		}
	}

	void shoot() {
		if (isAiming()) {
			Coord aimingVector = Coord.sub(position, aimPosition);
			velocity = Coord.mul(Math.sqrt(10.0 * aimingVector.magnitude() / Twoballs.UPDATE_FREQUENCY),
					aimingVector.norm()); // don't ask - determined by experimentation
			aimPosition = null;
		}
	}

	void move() {
		if (isMoving()) {
			position.increase(velocity);
			velocity.decrease(Coord.mul(FRICTION_PER_UPDATE, velocity.norm()));
		}
		
        if (hitWallX()){
            velocity.x = velocity.x * -1;
        }
        if (hitWallY()){
            velocity.y = velocity.y * -1;
        }
        
        //System.out.println(Table.ball1.position.x);
        hitBall();

	}

// paint: to draw the ball, first draw a black ball
// and then a smaller ball of proper color inside
// this gives a nice thick border
	void paint(Graphics2D g2D) {
		g2D.setColor(Color.black);
		g2D.fillOval((int) (position.x - RADIUS + 0.5), (int) (position.y - RADIUS + 0.5), (int) DIAMETER,
				(int) DIAMETER);
		g2D.setColor(COLOR);
		g2D.fillOval((int) (position.x - RADIUS + 0.5 + BORDER_THICKNESS),
				(int) (position.y - RADIUS + 0.5 + BORDER_THICKNESS), (int) (DIAMETER - 2 * BORDER_THICKNESS),
				(int) (DIAMETER - 2 * BORDER_THICKNESS));
		if (isAiming()) {
			paintAimingLine(g2D);
		}
	}

	private void paintAimingLine(Graphics2D graph2D) {
		Coord.paintLine(graph2D, aimPosition, Coord.sub(Coord.mul(2, position), aimPosition));
	}
	
    boolean hitWallX() {
        if (position.x <= Table.WALL_THICKNESS + RADIUS) {
            return true;
        }
        else if (position.x >= Table.TABLE_WIDTH) {
            return true;
        }
        else {
            return false;
        }
    }

    boolean hitWallY() {
        if (position.y <= Table.WALL_THICKNESS + RADIUS){
            return true;
        }
        else if (position.y >= Table.TABLE_HEIGHT){
            return true;
        }
        else {
            return false;
        }
    }
    
    void hitBall() {
    	/*
    	double dist = Math.sqrt(Math.pow(Table.ball1.position.y - Table.ball2.position.y, 2) + Math.pow(Table.ball1.position.x - Table.ball2.position.x, 2));
    	
    	if (dist < DIAMETER) {
            return true;
    	}
    	*/
    	
    	/*
    	double[][] dist = new double[Table.balls.length][Table.balls.length];
    	
    	for (int i = 0 ; i < Table.balls.length ; i++) {
    		for (int j = 0; i < Table.balls.length ; i++) {
    			
    			//System.out.println(Table.balls[1].position.y);
    			
    			
    			dist[i][j] = Math.sqrt(Math.pow(Table.balls[i].position.y - Table.balls[j].position.y, 2) + Math.pow(Table.balls[i].position.x - Table.balls[j].position.x, 2));
    			
    			
    			//System.out.println(dist[i][j]);
    			
    			if (dist[i][j] < DIAMETER && dist[i][j] != 0) {
    				System.out.println("Hit! Boll " + i + " och boll " + j);
    				hitEffect(Table.balls[i], Table.balls[j]);


    	    	}	
        	}
    	}  	
    	 */
    	
    	
    	double dist = DIAMETER + 1;
    	
    	for (int i = 0 ; i < Table.balls.length ; i++) {
    		
    		if (this != Table.balls[i]) {
    			dist = Math.sqrt(Math.pow(this.position.y - Table.balls[i].position.y, 2) + Math.pow(this.position.x - Table.balls[i].position.x, 2));
    			if ( dist < DIAMETER ) {
        			hitEffect(this, Table.balls[i]);
        		}
    		}
    	}
    }
    
    void hitEffect(Ball ball1, Ball ball2) {
    	
    	/*
    	double yA = Table.ball1.position.y;
    	double xA = Table.ball1.position.x;
    	double yB = Table.ball2.position.y;
    	double xB = Table.ball2.position.x;
    	
    	double dx = (xA-xB) / Math.sqrt((xA-xB)*(xA-xB) + (yA-yB)*(yA-yB));
    	double dy = (yA-yB) / Math.sqrt((xA-xB)*(xA-xB) + (yA-yB)*(yA-yB));
    	
    	double pAy = Table.ball1.velocity.y;
    	double pAx = Table.ball1.velocity.x;
    	double pBy = Table.ball2.velocity.y;
    	double pBx = Table.ball2.velocity.x;
    	
    	double J = pBx*dx + pBy*dy - (pAx*dx + pAy*dy);
    	
    	Table.ball1.velocity.y = pAy + J * dy;
    	Table.ball1.velocity.x = pAx + J * dx;
    	Table.ball2.velocity.y = pBy - J * dy;
    	Table.ball2.velocity.x = pBx - J * dx;
    	*/
    	
    	double yA = ball1.position.y;
    	double xA = ball1.position.x;
    	double yB = ball2.position.y;
    	double xB = ball2.position.x;
    	
    	double dx = (xA-xB) / Math.sqrt((xA-xB)*(xA-xB) + (yA-yB)*(yA-yB));
    	double dy = (yA-yB) / Math.sqrt((xA-xB)*(xA-xB) + (yA-yB)*(yA-yB));
    	
    	double pAy = ball1.velocity.y;
    	double pAx = ball1.velocity.x;
    	double pBy = ball2.velocity.y;
    	double pBx = ball2.velocity.x;
    	
    	double J = pBx*dx + pBy*dy - (pAx*dx + pAy*dy);
    	
    	ball1.velocity.y = pAy + J * dy;
    	ball1.velocity.x = pAx + J * dx;
    	ball2.velocity.y = pBy - J * dy;
    	ball2.velocity.x = pBx - J * dx;
    }
    

} // end class Ball
