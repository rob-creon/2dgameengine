package com.creon.engine.test.window;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowCloseCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowCloseCallback;
import org.lwjgl.glfw.GLFWWindowCloseCallbackI;
import org.lwjgl.system.MemoryStack;

import com.creon.engine.test.input.Input;

public class WindowGLFW extends GameWindow {

	private long handle;

	private GLFWWindowCloseCallbackI closeCallback = new GLFWWindowCloseCallback() {

		@Override
		public void invoke(long window) {
			windowListener.requestClose();
		}

	};

	private GLFWKeyCallbackI keyCallback = new GLFWKeyCallbackI() {

		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
//			System.out.println("keyCallback.invoke(key=" + key + ", scancode=" + scancode + ", action=" + action
//					+ ", mods=" + mods);
			Input.getInstance().handleGLFWKeyEvent(key, scancode, action, mods);
		}

	};

	public WindowGLFW(String name, int width, int height, GameWindowListener windowListener) {
		super(name, width, height, windowListener);
		init();
	}

	private void init() {
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		handle = glfwCreateWindow(width, height, name, NULL, NULL);
		if (handle == NULL) {
			throw new RuntimeException("Failed to create GLFW window");
		}

		glfwSetKeyCallback(handle, keyCallback);
		glfwSetWindowCloseCallback(handle, closeCallback);

		try (MemoryStack stack = stackPush()) {
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(handle, (vidmode.width() - this.width) / 2, (vidmode.height() - this.height) / 2);
		}
		glfwMakeContextCurrent(handle);

		glfwShowWindow(handle);

	}

	@Override
	public void loopTick() {

		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		//glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glfwSwapBuffers(handle);
		glfwPollEvents();
	}

	@Override
	protected void updateName() {
		glfwSetWindowTitle(handle, this.name);
	}

	@Override
	public void destroy() {
		glfwFreeCallbacks(handle);
		glfwDestroyWindow(handle);
	}

	@Override
	public void enableVsync(boolean enable) {
		glfwSwapInterval(enable ? 1 : 0); // true==1, false==0
	}

}
