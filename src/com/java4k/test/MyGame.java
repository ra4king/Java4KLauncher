package com.java4k.test;

import com.java4k.core.Game;

import javafx.scene.canvas.GraphicsContext;
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
	public void render(GraphicsContext g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		g.setFill(Color.BLUE);
		g.fillRect(0, 0, 800, 600);
	}
}
