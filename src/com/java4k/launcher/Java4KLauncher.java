package com.java4k.launcher;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import com.java4k.core.Game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.InputEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Java4KLauncher extends Application {
	long lastTime;
	long accumulator;

	static final long NANOS = 1_000_000_000;
	static final long FPS = 60;
	static final long FRAME_TIME = NANOS / FPS;

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		List<String> args = this.getParameters().getUnnamed();
		if (args.size() != 3) {
			System.out.println("Usage: java -jar Java4KLauncher.jar JarURL my.main.class");
			return;
		}

		try (URLClassLoader classLoader = new URLClassLoader(new URL[] { new URL(args.get(0)) })) {
			final Game game = (Game) classLoader.loadClass(args.get(1)).newInstance();

			primaryStage.setTitle(args.get(2));

			Group root = new Group();
			Scene scene = new Scene(root, Color.BLACK);
			primaryStage.setScene(scene);

			final Canvas canvas = new Canvas(800, 600);
			root.getChildren().add(canvas);

			canvas.addEventHandler(InputEvent.ANY, game);

			game.init();

			lastTime = System.nanoTime();

			new AnimationTimer() {
				public void handle(long nanoTime) {
					long delta = nanoTime - lastTime;
					lastTime = nanoTime;
					accumulator += delta;

					if (accumulator > FRAME_TIME) {
						GraphicsContext graphics = canvas.getGraphicsContext2D();
						game.render(graphics);
						accumulator = 0; // Just discard extra frames if it's running too slowly.
					}
				}
			}.start();

			primaryStage.show();
		}
	}
}
