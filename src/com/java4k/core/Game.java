package com.java4k.core;

import javafx.animation.AnimationTimer;
import javafx.event.EventType;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * @author Roi Atalla
 */
public abstract class Game extends Canvas {
	private static class Pair<A, B> {
		A a;
		B b;
		
		Pair(A a, B b) {
			this.a = a;
			this.b = b;
		}
	}
	
	public Game() {
		super(800, 600);
	}
	
	public void run() {
		addEventHandler(MouseEvent.MOUSE_MOVED, e -> mouseEvent(MouseEvent.MOUSE_MOVED, e));
		addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> mouseEvent(MouseEvent.MOUSE_DRAGGED, e));
		addEventHandler(MouseEvent.MOUSE_CLICKED, e -> mouseEvent(MouseEvent.MOUSE_CLICKED, e));
		addEventHandler(MouseEvent.MOUSE_PRESSED, e -> mouseEvent(MouseEvent.MOUSE_PRESSED, e));
		addEventHandler(MouseEvent.MOUSE_RELEASED, e -> mouseEvent(MouseEvent.MOUSE_RELEASED, e));
		addEventHandler(MouseEvent.MOUSE_ENTERED, e -> mouseEvent(MouseEvent.MOUSE_ENTERED, e));
		addEventHandler(MouseEvent.MOUSE_EXITED, e -> mouseEvent(MouseEvent.MOUSE_EXITED, e));
		
		addEventHandler(KeyEvent.KEY_PRESSED, e -> keyEvent(KeyEvent.KEY_PRESSED, e));
		addEventHandler(KeyEvent.KEY_RELEASED, e -> keyEvent(KeyEvent.KEY_RELEASED, e));
		addEventHandler(KeyEvent.KEY_TYPED, e -> keyEvent(KeyEvent.KEY_TYPED, e));
		
		init();
		
		new AnimationTimer() {
			public void handle(long nanoTime) {
				render(getGraphicsContext2D());
			}
		}.start();
	}
	
	public void mouseEvent(EventType<MouseEvent> type, MouseEvent mouseEvent) {}
	
	public void keyEvent(EventType<KeyEvent> type, KeyEvent keyEvent) {}
	
	public abstract void init();
	
	public abstract void render(GraphicsContext g);
}
