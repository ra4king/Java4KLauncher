package com.java4k.launcher;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import com.java4k.core.Game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * @author Roi Atalla
 */
public class Java4KLauncher extends Application {
	public static void main(String[] args) throws Exception {
		launch(args);
	}
	
	private Game currentGame;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		List<String> args = this.getParameters().getUnnamed();
		if(args.size() != 3) {
			System.out.println("Usage: java -jar Java4KLauncher.jar JarURL my.main.class Name");
			System.exit(0);
		}
		
		primaryStage.setTitle(args.get(2));
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(e -> System.exit(0));
		
		TabPane tabPane = new TabPane();
		
		Tab gameTab = new Tab("No game selected");
		gameTab.setClosable(false);
		gameTab.setContent(new Rectangle(800, 600));
		
		Tab controlTab = new Tab("Game Selection");
		controlTab.setClosable(false);
		
		Button play = new Button("Play game");
		play.setOnAction(e -> {
			loadGame(args.get(0), args.get(1), args.get(2), gameTab);
		});
		
		Button play2 = new Button("Play game2");
		play2.setOnAction(e -> {
			loadGame(args.get(0), args.get(1) + "2", args.get(2) + "2", gameTab);
		});
		
		controlTab.setContent(new VBox(5, play, play2));
		
		tabPane.getTabs().add(controlTab);
		tabPane.getTabs().add(gameTab);
		
		primaryStage.setScene(new Scene(tabPane, Color.BLACK));
		
		primaryStage.show();
	}
	
	private void loadGame(String url, String className, String gameName, Tab gameTab) {
		if(currentGame != null) {
			currentGame.stop();
		}
		
		try(URLClassLoader classLoader = new URLClassLoader(new URL[] { new URL(url) })) {
			currentGame = (Game)classLoader.loadClass(className).newInstance();
			gameTab.setText(gameName);
			gameTab.setContent(currentGame);
			currentGame.run();
		} catch(Exception exc) {
			exc.printStackTrace();
		}
	}
}
