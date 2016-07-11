package com.java4k.launcher;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import com.java4k.core.Game;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Roi Atalla
 */
public class Java4KLauncher extends Application {
	public static void main(String[] args) throws Exception {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		List<String> args = this.getParameters().getUnnamed();
		if(args.size() != 3) {
			System.out.println("Usage: java -jar Java4KLauncher.jar JarURL my.main.class Name");
			System.exit(0);
		}
		
		try(URLClassLoader classLoader = new URLClassLoader(new URL[] { new URL(args.get(0)) })) {
			final Game game = (Game)classLoader.loadClass(args.get(1)).newInstance();
			
			primaryStage.setTitle(args.get(2));
			primaryStage.setResizable(false);
			primaryStage.setOnCloseRequest(e -> System.exit(0));
			
			Group root = new Group();
			primaryStage.setScene(new Scene(root));
			
			root.getChildren().add(game);
			
			primaryStage.show();
			
			game.run();
		}
	}
}
