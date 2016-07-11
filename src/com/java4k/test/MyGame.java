package com.java4k.test;

import com.java4k.core.Game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.InputEvent;
import javafx.scene.paint.Color;

public class MyGame extends Game {
	public void init() {
		System.out.println("init");
	}
	
	public void handle(InputEvent e) {
		
	}
	
	public void render(GraphicsContext g) {
		g.clearRect(0, 0, 800, 600);
		g.setFill(Color.BLUE);
		g.fillRect(50, 50, 20, 20);
	}
}
