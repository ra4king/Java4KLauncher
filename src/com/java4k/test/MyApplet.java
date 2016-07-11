package com.java4k.test;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;

/**
 * @author Roi Atalla
 */
public class MyApplet extends Applet {
	@Override
	public void init() {
		System.out.println("init");
	}
	
	@Override
	public void start() {
		System.out.println("start");
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(50, 50, 20, 20);
	}
	
	@Override
	public void destroy() {
		System.out.println("destroy");
	}
}
