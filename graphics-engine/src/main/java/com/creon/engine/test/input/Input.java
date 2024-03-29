package com.creon.engine.test.input;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;

import java.util.ArrayList;

public class Input {

	private static final Input instance = new Input();

	public static Input getInstance() {
		return Input.instance;
	}

	private InputButton btn1;
	private InputButton btn2;

	private InputButton btnEscape;

	private ArrayList<InputButton> buttons;

	// TODO input axis
//	private InputAxis movementAxis;

	private Input() {
//		movementAxis = new InputAxis("movementAxis", GLFW_KEY_W, GLFW_KEY_S, GLFW_KEY_A, GLFW_KEY_D);
		btn1 = new InputButton("btn1");
		btn2 = new InputButton("btn2");
		btnEscape = new InputButton("btnEscape");

		buttons = new ArrayList<InputButton>();

		buttons.add(btn1);
		buttons.add(btn2);
		buttons.add(btnEscape);
	}

	public void tick() {

		for (InputButton b : buttons) {
			b.tick();
		}

	}

	public void handleGLFWKeyEvent(int key, int scancode, int action, int mods) {
		// TODO keybindings system
		// SEE https://www.glfw.org/docs/latest/group__keys.html

		
		
		InputButton whichBtn = null;
		if (key == GLFW_KEY_Z) {
			
			whichBtn = btn1;
		}
		if (key == GLFW_KEY_SPACE) {
			whichBtn = btn2;
		}
		if (key == GLFW_KEY_ESCAPE) {
			whichBtn = btnEscape;
		}

		if (whichBtn != null) {
			switch (action) {
			case 0:
				whichBtn.callbackInvoke(InputButton.KeyAction.RELEASED);
				break;
			case 1:
				whichBtn.callbackInvoke(InputButton.KeyAction.PRESSED);
				break;
			case 2:
				whichBtn.callbackInvoke(InputButton.KeyAction.HELD);
				break;
			default:
				System.err.println("Unknown GLFWKeyCallback action: " + action);
				break;
			}
		}
	}

	public InputButton getBtn1() {
		return btn1;
	}

	public InputButton getBtn2() {
		return btn2;
	}

	public InputButton getBtnEscape() {
		return btnEscape;
	}
}
