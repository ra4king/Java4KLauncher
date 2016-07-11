package com.java4k.core;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JComponent;

/**
 * @author Roi Atalla
 */
public abstract class Game extends JComponent implements Runnable {
	private static class Pair<A, B> {
		A a;
		B b;
		
		Pair(A a, B b) {
			this.a = a;
			this.b = b;
		}
	}
	
	private ConcurrentLinkedQueue<Pair<Integer, MouseEvent>> mouseEventQueue = new ConcurrentLinkedQueue<>();
	private ConcurrentLinkedQueue<Pair<Integer, KeyEvent>> keyEventQueue = new ConcurrentLinkedQueue<>();
	
	@Override
	public void run() {
		addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mouseEventQueue.add(new Pair<>(MouseEvent.MOUSE_CLICKED, e));
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				mouseEventQueue.add(new Pair<>(MouseEvent.MOUSE_PRESSED, e));
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				mouseEventQueue.add(new Pair<>(MouseEvent.MOUSE_RELEASED, e));
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				mouseEventQueue.add(new Pair<>(MouseEvent.MOUSE_ENTERED, e));
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				mouseEventQueue.add(new Pair<>(MouseEvent.MOUSE_EXITED, e));
			}
		});
		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				mouseEventQueue.add(new Pair<>(MouseEvent.MOUSE_DRAGGED, e));
			}
			
			@Override
			public void mouseMoved(MouseEvent e) {
				mouseEventQueue.add(new Pair<>(MouseEvent.MOUSE_MOVED, e));
			}
		});
		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				keyEventQueue.add(new Pair<>(KeyEvent.KEY_TYPED, e));
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				keyEventQueue.add(new Pair<>(KeyEvent.KEY_PRESSED, e));
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				keyEventQueue.add(new Pair<>(KeyEvent.KEY_RELEASED, e));
			}
		});
		
		init();
		
		BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D)image.getGraphics();
		
		while(true) {
			processEvents();
			render(g);
			getGraphics().drawImage(image, 0, 0, null);
		}
	}
	
	protected void processEvents() {
		while(!mouseEventQueue.isEmpty()) {
			Pair<Integer, MouseEvent> pair = mouseEventQueue.poll();
			mouseEvent(pair.a, pair.b);
		}
		
		while(!keyEventQueue.isEmpty()) {
			Pair<Integer, KeyEvent> pair = keyEventQueue.poll();
			keyEvent(pair.a, pair.b);
		}
	}
	
	public void keyEvent(int evenType, KeyEvent keyEvent) {}
	
	public void mouseEvent(int eventType, MouseEvent mouseEvent) {}
	
	public void init() {}
	
	public void render(Graphics2D g) {}
}
