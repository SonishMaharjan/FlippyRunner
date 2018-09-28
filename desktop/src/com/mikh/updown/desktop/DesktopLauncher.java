package com.mikh.updown.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mikh.updown.helpers.GameInfo;
import com.mikh.updown.GameMain;

import ads.AdController;
import ads.DesktopAdController;
import services.DesktopPlayServices;

public class DesktopLauncher {

	public static final AdController AD_CONTROLLER = new DesktopAdController();
	public  static DesktopPlayServices DESKTOP_PLAY_SERVICES =  new DesktopPlayServices();


	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height =(int) GameInfo.HEIGHT;
		config.width =(int) GameInfo.WIDTH;
		new LwjglApplication(new GameMain(AD_CONTROLLER,DESKTOP_PLAY_SERVICES), config);
	}
}
