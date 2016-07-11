package com.java4k.core;

import javafx.event.EventType;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * @author Roi Atalla
 */
public abstract class Game extends Canvas {
	public Game() {
		super(800, 600);
	}
	
	public void mouseEvent(EventType<MouseEvent> type, MouseEvent mouseEvent) {}
	
	public void keyEvent(EventType<KeyEvent> type, KeyEvent keyEvent) {}
	
	public abstract void init();
	
	public abstract void render(GraphicsContext g);
	
	public void destroy() {}
}
