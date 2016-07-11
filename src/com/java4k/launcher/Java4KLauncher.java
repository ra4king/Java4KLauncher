package com.java4k.launcher;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.java4k.core.Game;

import javafx.application.Application;
import javafx.geometry.Insets;
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
	
	private static class GameInfo {
		private String jarUrl;
		private String className;
		private String name;
		
		GameInfo(String jarUrl, String className, String name) {
			this.jarUrl = jarUrl;
			this.className = className;
			this.name = name;
		}
	}
	
	private static HashMap<String, URLClassLoader> cachedJars = new HashMap<>();
	
	private List<GameInfo> gameList;
	private Game currentGame;
	
	private List<GameInfo> loadGameList(String file) {
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(file).openStream()))) {
			List<GameInfo> gameList = new ArrayList<>();
			
			String line;
			int lineNum = 0;
			while((line = reader.readLine()) != null) {
				lineNum++;
				
				line = line.trim();
				if(line.isEmpty())
					continue;
				
				int i = line.indexOf(' ');
				if(i == -1) {
					throw new IllegalArgumentException("Incorrect formatting on line " + lineNum);
				}
				
				int i2 = line.indexOf(' ', i + 1);
				if(i2 == -1) {
					throw new IllegalArgumentException("Incorrect formatting on line " + lineNum);
				}
				
				String jar = line.substring(0, i).trim();
				String className = line.substring(i + 1, i2).trim();
				String name = line.substring(i2 + 1).trim();
				gameList.add(new GameInfo(jar, className, name));
			}
			
			return gameList;
		} catch(Exception exc) {
			exc.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void start(Stage primaryStage) {
		List<String> args = this.getParameters().getUnnamed();
		if(args.size() != 1) {
			System.out.println("Usage: java -jar Java4KLauncher.jar gamelist.txt");
			System.exit(0);
		}
		
		gameList = loadGameList(args.get(0));
		
		primaryStage.setTitle("Java4K Launcher");
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(e -> System.exit(0));
		
		TabPane tabPane = new TabPane();
		
		Tab gameTab = new Tab("No game selected");
		gameTab.setClosable(false);
		gameTab.setContent(new Rectangle(800, 600));
		
		Tab controlTab = new Tab("Game Selection");
		controlTab.setClosable(false);
		
		VBox buttonsBox = new VBox();
		for(int i = 0; i < gameList.size(); i++) {
			GameInfo game = gameList.get(i);
			Button b = new Button(game.name);
			b.setOnAction(e -> loadGame(game, gameTab));
			buttonsBox.getChildren().add(b);
			VBox.setMargin(b, new Insets(10, 0, 0, 10));
		}
		
		controlTab.setContent(buttonsBox);
		
		tabPane.getTabs().add(controlTab);
		tabPane.getTabs().add(gameTab);
		
		primaryStage.setScene(new Scene(tabPane, Color.BLACK));
		
		primaryStage.show();
	}
	
	private void loadGame(GameInfo game, Tab gameTab) {
		if(currentGame != null) {
			currentGame.stop();
		}
		
		try {
			URLClassLoader classLoader;
			if(cachedJars.containsKey(game.jarUrl)) {
				classLoader = cachedJars.get(game.jarUrl);
			} else {
				classLoader = new URLClassLoader(new URL[] { new URL(game.jarUrl) });
				cachedJars.put(game.jarUrl, classLoader);
			}
			
			currentGame = (Game)classLoader.loadClass(game.className).newInstance();
			gameTab.setText(game.name);
			gameTab.setContent(currentGame);
			currentGame.run();
		} catch(Exception exc) {
			exc.printStackTrace();
		}
	}
}
