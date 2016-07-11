package com.java4k.core;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

/**
 * @author Roi Atalla
 */
public abstract class Game extends JComponent implements Runnable {
	@Override
	public void run() {
		addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mouseEvent(e);
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				mouseEvent(e);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				mouseEvent(e);
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				mouseEvent(e);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				mouseEvent(e);
			}
		});
		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				mouseEvent(e);
			}
			
			@Override
			public void mouseMoved(MouseEvent e) {
				mouseEvent(e);
			}
		});
		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				keyEvent(e);
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				keyEvent(e);
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				keyEvent(e);
			}
		});
		
		init();
		
		BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D)image.getGraphics();
		
		while(true) {
			render(g);
			getGraphics().drawImage(image, 0, 0, null);
		}
	}
	
	public void keyEvent(KeyEvent keyEvent) {}
	
	public void mouseEvent(MouseEvent mouseEvent) {}
	
	public void init() {}
	
	public void render(Graphics2D g) {}
}
