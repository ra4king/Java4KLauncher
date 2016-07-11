package com.java4k.core;

import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.InputEvent;

public abstract class Game implements EventHandler<InputEvent> {
	public abstract void init();
	
	public abstract void render(GraphicsContext g);
}
