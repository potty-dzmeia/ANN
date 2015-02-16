/**
 * Motion2.java
 * Animation of 4 "balls" moving in a container and bouncing off the
 * walls.
 * This version does not use double buffering. You can see the flickering
 * in the red of the objects.
 * Motion3.java adds double buffering which removes most of the
 * flickering.
 */


import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class Motion2 extends Applet implements ActionListener
{
	MyCanvas myCanvas;
	Button btnStop, btnGo;
	
	public void init() {
		setLayout(new BorderLayout());
		Panel pnlButtons = new Panel();
		btnGo = new Button("go");
		btnStop = new Button("stop");
		pnlButtons.add(btnGo);
		pnlButtons.add(btnStop);
		add("South", pnlButtons);
		myCanvas = new MyCanvas();
		add("Center", myCanvas);
		btnGo.addActionListener(this);
		btnStop.addActionListener(this);
	}
	// Called by the applet context right after init()
	public void start() {
		myCanvas.go();
	}
	// called when the browser goes to another page. Good idea
	// (usually) to stop your thread here to free CPU cycles.
	public void stop() {
		myCanvas.halt();
	}
	
	public void actionPerformed(ActionEvent evt) {
		if(evt.getActionCommand().equals("go")) {
			myCanvas.go();
		}
		else if("stop".equals(evt.getActionCommand())) {
			myCanvas.halt();
		}
	}
}

/**
 * Note that most of the code is in the Canvas subclass. More modular!
 */
class MyCanvas extends Canvas implements Runnable {

	boolean firstTime = true;	// flag. paint() called after init()
	
	private Thread drawThread = null;
	
	private final int NUMCIRCLES = 4;
	// position of circles
	private Point [] circles = new Point[NUMCIRCLES];
	// change in position per time interval
	private Point [] delta = new Point[NUMCIRCLES];

	public MyCanvas() {
		setBackground(Color.cyan);
		//size = getSize(); -- useless: layout occurs after construction!
		delta[0] = new Point(10, 10);	// arbitray speeds.
		delta[1] = new Point(2, 2);
		delta[2] = new Point(8, 8);
		delta[3] = new Point(5, 5);		

	}
	
	// draws the balls. Note that the default update() is used so that
	// the screen is erased at each cycle because repaint() first
	// requests update() which then calls paint()
	
	public void paint(Graphics g) {
		if(firstTime) {
			for(int i = 0; i < NUMCIRCLES; i++) {
				int x = (int) (Math.random() * getSize().width);
				int y = (int) (Math.random() * getSize().height);	
				drawBall(g, x, y,40, 40);
				circles[i] = new Point(x, y);
			}
			firstTime = false;			
		}
		else {
			for(int i=0; i < NUMCIRCLES; i++) {
				Point np = moveBall(i, circles[i]);
				drawBall(g, np.x, np.y, 40, 40);
				circles[i]= np;
			}
		}
	}
	
	/**
	 * Note that stopping the thread with stop() is avoided. When the
	 * user clicks the stop button, the drawThread is set to null,
	 * ending the while loop and allowing the thread to die a natural
	 * death when the run() method returns.
	 */
	public void run () {
		Thread currentThread = Thread.currentThread();
	
		while(currentThread == drawThread) {
			try {
				Thread.sleep(20);	// slow down repaint() requests.
			}
			catch(InterruptedException e) {}
			repaint();	// asks for default update() which calls paint()
		}
	}
	
	public void drawBall(Graphics g, int xCenter, int yCenter, 
									 int width, int height)
	{
		g.setColor(Color.red);
		g.fillOval(xCenter-width/2, yCenter-height/2, width, height);
	}
	
	// find new ball position. Bounce off walls.
	
	public Point moveBall(int ballID, Point p) {

		int nextX = p.x + delta[ballID].x;
		int nextY = p.y + delta[ballID].y;
		
		if (nextX < 0) {
			delta[ballID].x = -delta[ballID].x;
		}
		if (nextY < 0) {
			delta[ballID].y = -delta[ballID].y;
		}
		if (nextX > getSize().width) {
			delta[ballID].x = -delta[ballID].x;
		}
		if (nextY > getSize().height) {
			delta[ballID].y = -delta[ballID].y;
		}
		
		nextX = p.x + delta[ballID].x;
		nextY = p.y + delta[ballID].y;
		return new Point(nextX, nextY);
	}	
	
	public void go() {
		if(drawThread == null) {// make sure thread is not running.
			// this points to where the run() method is.
			drawThread = new Thread(this);
			drawThread.start();
		}
	}
	
	// By setting drawThread to null, we force an exit from the while
	// loop in the run() method and therefore the run() method to 
	// return. Upon return from run() the thread goes into the dead
	// state. Since we have also set its reference, drawThread, to null
	// there are no references to the Thread object and therefore
	// the Garbage Collector can return the memoryit was using to the
	// heap.
	
	public void halt() {
		if(drawThread != null) {
			drawThread = null;
		}
	}
			
		
}