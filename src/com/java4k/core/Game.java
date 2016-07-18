package com.java4k.core;

import com.java4k.launcher.Java4KLauncher;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * @author Roi Atalla
 */
public abstract class Game extends Canvas {
	public Game() {
		super(Java4KLauncher.WIDTH, Java4KLauncher.HEIGHT);
	}
	
	public void mouseEvent(MouseEvent mouseEvent) {}
	
	public void keyEvent(KeyEvent keyEvent) {}
	
	public void init() {}
	
	public abstract void render(GraphicsContext g);
	
	public void destroy() {}
}
