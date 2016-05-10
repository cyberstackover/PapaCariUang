package com.mygdx.buildthetower.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.buildthetower.BuildTheTower;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = BuildTheTower.VIRTUAL_WIDTH;
		config.height = BuildTheTower.VIRTUAL_HEIGHT;
		config.title = BuildTheTower.NAME;

		new LwjglApplication(new BuildTheTower(true, null), config);
	}
}
