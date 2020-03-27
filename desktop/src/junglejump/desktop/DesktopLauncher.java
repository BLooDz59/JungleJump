package junglejump.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import junglejump.JungleJump;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.forceExit = false;
		config.width = Integer.parseInt(arg[0]);
		config.height = Integer.parseInt(arg[1]);
		new LwjglApplication(new JungleJump(), config);
	}
}
