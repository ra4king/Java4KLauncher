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

import javax.swing.JPanel;

import com.java4k.core.Game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
	private Object currentGame;
	
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
		for(GameInfo gameInfo : gameList) {
			Button b = new Button(gameInfo.name);
			b.setOnAction(e -> loadGame(gameInfo, gameTab));
			buttonsBox.getChildren().add(b);
			VBox.setMargin(b, new Insets(10, 0, 0, 10));
		}
		
		controlTab.setContent(buttonsBox);
		
		tabPane.getTabs().add(controlTab);
		tabPane.getTabs().add(gameTab);
		
		primaryStage.setScene(new Scene(tabPane, Color.BLACK));
		
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
	
	private List<GameInfo> loadGameList(String file) {
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
			while((line = reader.readLine()) != null) {
				lineNum++;
				
				line = line.trim();
				if(line.isEmpty()) {
					continue;
				}
				
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
				gameTab.setContent(new Rectangle(800, 600));
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
				applet.setPreferredSize(new Dimension(800, 600));
				
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
				panel.setPreferredSize(new Dimension(800, 600));
				panel.add(applet);
				
				SwingNode swingNode = new SwingNode();
				swingNode.prefWidth(800);
				swingNode.prefHeight(600);
				swingNode.setContent(panel);
				
				gameTab.setText(game.name);
				gameTab.setContent(swingNode);
				currentGame = null;
				
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
