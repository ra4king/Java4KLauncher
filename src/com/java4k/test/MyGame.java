package com.java4k.test;

import com.java4k.core.Game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

/**
 * @author Roi Atalla
 */
public class MyGame extends Game {
	private boolean a = false;
	private boolean b = false;
	private boolean c = false;
	private boolean d = false;
	private double x, y;
	
	public void keyEvent(KeyEvent e) {
		if(e.getEventType() == KeyEvent.KEY_PRESSED || e.getEventType() == KeyEvent.KEY_RELEASED) {
			boolean isPressed = e.getEventType() == KeyEvent.KEY_PRESSED;
			
			if(e.getCode() == KeyCode.RIGHT)
				a = isPressed;
			if(e.getCode() == KeyCode.LEFT)
				b = isPressed;
			if(e.getCode() == KeyCode.UP)
				c = isPressed;
			if(e.getCode() == KeyCode.DOWN)
				d = isPressed;
		}
	}
	
	public void render(GraphicsContext g) {
		double speed = 2;
		if(a) x += speed;
		if(b) x -= speed;
		if(c) y -= speed;
		if(d) y += speed;
		x = Math.max(Math.min(x, getWidth() - 20), 0);
		y = Math.max(Math.min(y, getHeight() - 20), 0);
		
		g.clearRect(0, 0, getWidth(), getHeight());
		g.setFill(Color.BLUE);
		g.fillRect(x, y, 20, 20);
	}
}
