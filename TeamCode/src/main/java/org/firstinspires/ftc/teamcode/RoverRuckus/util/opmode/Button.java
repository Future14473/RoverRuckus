package org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode;

import java.util.function.BooleanSupplier;

/**
 * Utility for gamepad button presses.
 */
@SuppressWarnings("unused")
public class Button implements BooleanSupplier {
	private final BooleanSupplier button;
	private       boolean         past = false;
	
	public Button(BooleanSupplier button) {
		this.button = button;
	}
	
	public State getState() {
		boolean cur = button.getAsBoolean();
		State o = cur ?
		          (past ? State.HELD : State.PRESSED) :
		          (past ? State.RELEASED : State.UP);
		past = cur;
		return o;
	}
	
	public boolean up() {
		State state = getState();
		return state == State.UP || state == State.RELEASED;
	}
	
	public boolean pressed() {
		return getState() == State.PRESSED;
	}
	
	public boolean down() {
		State state = getState();
		return state == State.HELD || state == State.PRESSED;
	}
	
	public boolean released() {
		return getState() == State.RELEASED;
	}
	
	@Override
	public boolean getAsBoolean() {
		return button.getAsBoolean();
	}
	
	public enum State {
		UP,
		PRESSED,
		HELD,
		RELEASED
	}
	
}
