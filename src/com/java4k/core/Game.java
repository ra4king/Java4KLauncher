package com.java4k.core;

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
	
	public void mouseEvent(MouseEvent mouseEvent) {}
	
	public void keyEvent(KeyEvent keyEvent) {}
	
	public abstract void init();
	
	public abstract void render(GraphicsContext g);
	
	public void destroy() {}
}
