package com.creon.engine.test.input;

public class InputButton {

	public enum KeyAction {
		PRESSED, RELEASED, HELD
	}

	private String dbgName;
	private boolean isPressed, isTapped, isReleased, isHeld;
	private int frameCtr;

	public InputButton(String dbgName) {
		this.dbgName = dbgName;
		isPressed = false;
		isTapped = false;
		isReleased = false;
		isHeld = false;
		frameCtr = 0;
	}

	public void callbackInvoke(KeyAction action) {

		isPressed = false;
		isTapped = false;
		isReleased = false;
		isHeld = false;

		switch (action) {
		case PRESSED:
			isPressed = true;
			break;
		case RELEASED:
			isReleased = true;
			frameCtr = 0;
			break;
		case HELD:
			isHeld = true;
			break;
		default:
			break;
		}

		// System.out.println("cb-"+this);
	}

	public void tick() {

		frameCtr++;

		if (frameCtr == 1) {
			// System.out.println("framectr-"+this);
			isReleased = false;
		}

	}

	public String toString() {
		return "InputButton{name=\"" + dbgName + "\", isPressed=" + isPressed + ", isReleased=" + isReleased
				+ ", isHeld=" + isHeld + "}";
	}

	public String getDbgName() {
		return dbgName;
	}

	public boolean isPressed() {
		return isPressed;
	}

	public boolean isTapped() {
		return isTapped;
	}

	public boolean isReleased() {
		return isReleased;
	}

	public boolean isHeld() {
		return isHeld;
	}

}
