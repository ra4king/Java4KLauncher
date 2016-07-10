package com.java4k.test;

import java.awt.Color;
import java.awt.Graphics2D;

import com.java4k.core.Game;

/**
 * @author Roi Atalla
 */
public class MyGame extends Game {
	public void init() {
		System.out.println("init");
	}
	
	public void render(Graphics2D g) {
		g.setColor(Color.blue);
		g.fillRect(50, 50, 20, 20);
	}
}
