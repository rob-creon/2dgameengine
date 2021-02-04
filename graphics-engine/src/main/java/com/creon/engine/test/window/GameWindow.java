package com.creon.engine.test.window;

public abstract class GameWindow {

	public interface GameWindowListener {
		public void requestClose();
	}

	protected String name;
	protected int width, height;
	protected GameWindowListener windowListener;

	protected GameWindow(String name, int width, int height, GameWindowListener windowListener) {
		this.name = name;
		this.width = width;
		this.height = height;
		this.windowListener = windowListener;
	}
	
	public abstract void enableVsync(boolean enable);
	public abstract void destroy();
	public abstract void loopTick();
	protected abstract void updateName();
	
	public void updateWindowName(String name) {
		this.name = name;
		this.updateName();
	}
	
	public String getName() {
		return name;
	}
}
