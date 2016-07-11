package com.java4k.core;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

/**
 * @author Roi Atalla
 */
public abstract class Game extends JComponent implements Runnable, MouseListener, KeyListener, MouseMotionListener {
	public void run() {
		addMouseListener(this);
		addKeyListener(this);
		addMouseMotionListener(this);
		
		init();
		
		BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D)image.getGraphics();
		
		while(true) {
			render(g);
			tick();
			getGraphics().drawImage(image, 0, 0, null);
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) {}
	
	@Override
	public void keyReleased(KeyEvent e) {}
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	
	@Override
	public void mousePressed(MouseEvent e) {}
	
	@Override
	public void mouseReleased(MouseEvent e) {}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	
	@Override
	public void mouseExited(MouseEvent e) {}
	
	@Override
	public void mouseMoved(MouseEvent e) {}
	
	@Override
	public void mouseDragged(MouseEvent e) {}
	
	public abstract void init();
	
	public abstract void tick();
	
	public abstract void render(Graphics2D g);
}
