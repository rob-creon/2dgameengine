package com.creon.engine.test.game;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.glClearColor;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import com.creon.engine.test.input.Input;
import com.creon.engine.test.render.TestRender;
import com.creon.engine.test.window.GameWindow;
import com.creon.engine.test.window.GameWindow.GameWindowListener;
import com.creon.engine.test.window.WindowGLFW;

public class Game implements GameWindowListener {

	public static String WINDOW_TITLE = "creon game";

	private static Game instance;

	public synchronized static Game getInstance() {
		if (instance == null) {
			instance = new Game();
		}
		return instance;
	}

	private double delta;

	private boolean running = false;

	private GameWindow window;
	private TestRender render;

	public Game() {
		initGLFW();
		window = new WindowGLFW(WINDOW_TITLE, 640, 480, this);

		initGL();
		render = new TestRender();

	}

	private void initGLFW() {
		GLFWErrorCallback.createPrint(System.err).set();
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
	}

	private void initGL() {
		GL.createCapabilities();

		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	}

	private void destroy() {
		render.destroy();
		window.destroy();

		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	public void start() {
		running = true;
		loop();
	}

	public void stop() {
		running = false;
	}

	private void loop() {

		long lastSecFrameTime = System.nanoTime();
		long lastFrameTime = System.nanoTime();
		int frames = 0;
		while (running) {

			window.loopTick();
			render.render();

			// do game tick

			if (Input.getInstance().getBtnEscape().isReleased()) {
				stop();
				continue;
			}

			delta = (System.nanoTime() - lastFrameTime) / 1e+9d;
			lastFrameTime = System.nanoTime();
			Input.getInstance().tick();
			

			frames++;
			if (System.nanoTime() - lastSecFrameTime > 1e+9) { // 1e+9 = # of nano seconds in a second
				window.updateWindowName(WINDOW_TITLE + " FPS: " + frames);
				frames = 0;
				lastSecFrameTime = System.nanoTime();
			}
		}

		destroy();
	}

	@Override
	public void requestClose() {
		this.stop();
	}

	public double getDelta() {
		return delta;
	}

}
