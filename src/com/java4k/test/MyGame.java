package com.java4k.test;

import com.java4k.core.Game;

import javafx.event.EventType;
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
	public void mouseEvent(EventType<MouseEvent> type, MouseEvent mouseEvent) {
		System.out.println(type + ": " + mouseEvent);
	}
	
	@Override
	public void keyEvent(EventType<KeyEvent> type, KeyEvent keyEvent) {
		System.out.println(type + ": " + keyEvent);
	}
	
	@Override
	public void render(GraphicsContext g) {
		g.setFill(Color.BLACK);
		g.clearRect(0, 0, getWidth(), getHeight());
		g.setFill(Color.BLUE);
		g.fillRect(50, 50, 20, 20);
	}
}
