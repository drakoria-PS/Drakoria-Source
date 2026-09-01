package com.skyfire;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.skyfire.util.RSAKeyGenerator;
import com.skyfire.util.ShutdownHook;
import com.skyfire.world.content.minigames.impl.Cows;

/**
 * The starting point of Arlania.
 * @author Gabriel
 * @author Samy
 */
public class SkyFireLauncher {

	private static final GameLoader loader = new GameLoader(GameSettings.GAME_PORT);
	private static final Logger logger = Logger.getLogger("Skyfire");
	private static boolean updating;

	public static void main(String[] params) {
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
		try {

			logger.info("We are now starting up game files...");
			loader.init();
			loader.finish();
			logger.info("The loader has finished loading utility tasks.");
			logger.info("Skyfire is now online on port "+GameSettings.GAME_PORT+"! Have a great day!");
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Could not start Skyfire! Program terminated.", ex);
			System.exit(1);
		}
	}

	public static GameLoader getLoader() {
		return loader;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setUpdating(boolean updating) {
		SkyFireLauncher.updating = updating;
	}

	public static boolean isUpdating() {
		return SkyFireLauncher.updating;
	}
}