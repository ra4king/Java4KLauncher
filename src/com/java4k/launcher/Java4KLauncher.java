package com.java4k.launcher;

import java.awt.Dimension;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.java4k.core.Game;

/**
 * @author Roi Atalla
 */
public class Java4KLauncher {
	public static void main(String[] args) throws Exception {
		if(args.length != 3) {
			System.out.println("Usage: java -jar Java4KLauncher.jar JarURL my.main.class");
			return;
		}
		
		URLClassLoader classLoader = new URLClassLoader(new URL[] { new URL(args[0]) });
		Game game = (Game)classLoader.loadClass(args[1]).newInstance();
		
		JFrame frame = new JFrame(args[2]);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		game.setMinimumSize(new Dimension(800, 600));
		game.setPreferredSize(new Dimension(800, 600));
		game.setMaximumSize(new Dimension(800, 600));
		frame.add(game);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		new Thread(game).start();
	}
}
