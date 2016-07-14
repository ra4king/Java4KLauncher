package com.java4k.test;

import com.java4k.core.Game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * @author Roi Atalla
 */
public class MyGame extends Game {
	@Override
	public void init() {
		System.out.println("init");
	}
	
	@Override
	public void keyEvent(KeyEvent keyEvent) {
		System.out.println("keyEvent of type " + keyEvent.getEventType().getName() + ": " + keyEvent.getCode() + ", '" + keyEvent.getCharacter() + "'");
	}
	
	@Override
	public void mouseEvent(MouseEvent mouseEvent) {
		System.out.println("mouseEvent of type " + mouseEvent.getEventType().getName() + ": " + mouseEvent.getButton() + " (" + mouseEvent.getX() + ", " + mouseEvent.getY() + ")");
	}
	
	@Override
	public void render(GraphicsContext g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		g.setFill(Color.BLUE);
		g.fillRect(0, 0, 800, 600);
	}
}
