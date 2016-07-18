package com.java4k.launcher;

import java.applet.Applet;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.swing.JPanel;

import com.java4k.core.Game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
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
		private String pageUrl;
		
		GameInfo(String jarUrl, String className, String name, String pageUrl) {
			this.jarUrl = jarUrl;
			this.className = className;
			this.name = name;
			this.pageUrl = pageUrl;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	private static HashMap<String, URLClassLoader> cachedJars = new HashMap<>();
	
	private Object currentGame;
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 800;
	
	@Override
	public void start(Stage primaryStage) {
		List<String> args = this.getParameters().getUnnamed();
		if(args.size() != 1) {
			System.out.println("Usage: java -jar Java4KLauncher.jar gamelist.txt");
			System.exit(0);
		}
		
		List<GameInfo> gameList = loadGameList(args.get(0));
		
		primaryStage.setTitle("Java4K Launcher");
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(e -> Platform.exit());
		
		TabPane tabPane = new TabPane();
		
		Tab gameTab = new Tab("No game selected");
		gameTab.setClosable(false);
		gameTab.setContent(new Rectangle(WIDTH, HEIGHT, Color.WHITESMOKE));
		
		Tab controlTab = new Tab("Game Selection");
		controlTab.setClosable(false);
		
		final int listWidth = 200;
		
		WebView webView = new WebView();
		webView.setMinWidth(WIDTH - listWidth);
		WebEngine webEngine = webView.getEngine();
		
		ListView<GameInfo> gameListView = new ListView<>();
		gameListView.setMinWidth(listWidth);
		gameListView.getItems().addAll(gameList);
		gameListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			loadGame(newValue, gameTab);
			webEngine.load(newValue.pageUrl);
		});
		
		HBox hBox = new HBox(gameListView, webView);
		hBox.setMinWidth(WIDTH);
		hBox.setPrefWidth(WIDTH);
		hBox.setMinHeight(HEIGHT);
		hBox.setPrefHeight(HEIGHT);
		controlTab.setContent(hBox);
		
		tabPane.getTabs().add(controlTab);
		tabPane.getTabs().add(gameTab);
		
		primaryStage.setScene(new Scene(tabPane, Color.BLACK));
		primaryStage.sizeToScene();
		primaryStage.show();
		
		new AnimationTimer() {
			public void handle(long nanoTime) {
				if(currentGame != null && currentGame instanceof Game) {
					Game game = (Game)currentGame;
					game.render(game.getGraphicsContext2D());
				}
			}
		}.start();
	}
	
	private static List<GameInfo> loadGameList(String file) {
		try {
			InputStream inputStream;
			try {
				inputStream = new URL(file).openStream();
			}
			catch(MalformedURLException exc) {
				inputStream = new FileInputStream(file);
			}
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			
			List<GameInfo> gameList = new ArrayList<>();
			
			String line;
			int lineNum = 0;
			try {
				while((line = reader.readLine()) != null) {
					lineNum++;
					
					Scanner scanner = new Scanner(line);
					
					String jar = scanner.next();
					String className = scanner.next();
					String pageUrl = scanner.next();
					String name = scanner.nextLine();
					gameList.add(new GameInfo(jar, className, name, pageUrl));
				}
			} catch(Exception e) {
				throw new IllegalArgumentException("Incorrect formatting on line " + lineNum, e);
			}
			
			return gameList;
		}
		catch(Exception exc) {
			new Alert(AlertType.ERROR, "Error while loading game list.", new ButtonType("Ok.", ButtonData.OK_DONE)).showAndWait();
			throw new RuntimeException(exc);
		}
	}
	
	private void loadGame(GameInfo game, Tab gameTab) {
		try {
			if(currentGame != null) {
				if(currentGame instanceof Game) {
					Game g = (Game)currentGame;
					g.destroy();
				} else if(currentGame instanceof Applet) {
					Applet applet = (Applet)currentGame;
					applet.stop();
					applet.destroy();
				}
				
				gameTab.setText("No game selected");
				gameTab.setContent(new Rectangle(WIDTH, HEIGHT, Color.WHITESMOKE));
			}
			
			URLClassLoader classLoader;
			if(cachedJars.containsKey(game.jarUrl)) {
				classLoader = cachedJars.get(game.jarUrl);
			} else {
				classLoader = loadJar(game.jarUrl);
				cachedJars.put(game.jarUrl, classLoader);
			}
			
			Object object = classLoader.loadClass(game.className).newInstance();
			if(object instanceof Game) {
				Game newGame = (Game)object;
				
				gameTab.setText(game.name);
				gameTab.setContent(newGame);
				
				newGame.addEventHandler(MouseEvent.ANY, (e) -> newGame.requestFocus());
				newGame.addEventHandler(MouseEvent.MOUSE_MOVED, newGame::mouseEvent);
				newGame.addEventHandler(MouseEvent.MOUSE_DRAGGED, newGame::mouseEvent);
				newGame.addEventHandler(MouseEvent.MOUSE_CLICKED, newGame::mouseEvent);
				newGame.addEventHandler(MouseEvent.MOUSE_PRESSED, newGame::mouseEvent);
				newGame.addEventHandler(MouseEvent.MOUSE_RELEASED, newGame::mouseEvent);
				newGame.addEventHandler(MouseEvent.MOUSE_ENTERED, newGame::mouseEvent);
				newGame.addEventHandler(MouseEvent.MOUSE_EXITED, newGame::mouseEvent);
				newGame.addEventHandler(KeyEvent.KEY_PRESSED, newGame::keyEvent);
				newGame.addEventHandler(KeyEvent.KEY_RELEASED, newGame::keyEvent);
				newGame.addEventHandler(KeyEvent.KEY_TYPED, newGame::keyEvent);
				newGame.init();
				
				currentGame = newGame;
			} else if(object instanceof Applet) {
				Applet applet = (Applet)object;
				applet.setPreferredSize(new Dimension(WIDTH, HEIGHT));
				
				JPanel panel = new JPanel() {
					private boolean init = false;
					@Override
					public void paintComponent(Graphics g) {
						if(!init) {
							applet.init();
							applet.start();
							init = true;
						}
						
						super.paintComponent(g);
					}
				};
				panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
				panel.add(applet);
				
				SwingNode swingNode = new SwingNode();
				
				gameTab.setText(game.name);
				gameTab.setContent(swingNode);
				swingNode.setContent(panel);
				
				currentGame = applet;
			} else {
				throw new IllegalArgumentException("Not a Game or Applet instance.");
			}
		}
		catch(Exception exc) {
			exc.printStackTrace();
			new Alert(AlertType.ERROR, "Error while loading game " + game.name, new ButtonType("Ok.", ButtonData.OK_DONE)).showAndWait();
		}
	}
	
	// TODO: set a secure context
	private URLClassLoader loadJar(String jarUrl) throws Exception {
		try {
			return new URLClassLoader(new URL[] { new URL(jarUrl) });
		} catch(Exception exc) {
			return new URLClassLoader(new URL[] { Java4KLauncher.class.getResource(jarUrl) });
		}
	}
}
