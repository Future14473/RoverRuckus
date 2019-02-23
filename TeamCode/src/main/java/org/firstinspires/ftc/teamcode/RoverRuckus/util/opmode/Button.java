package org.firstinspires.ftc.teamcode.RoverRuckus.util.opmode;

import java.util.function.BooleanSupplier;

/**
 * Utility for monitoring button state.
 */
@SuppressWarnings("unused")
public class Button implements BooleanSupplier {
	private final BooleanSupplier button;
	private       boolean         past = false;
	
	public Button(BooleanSupplier button) {
		this.button = button;
	}
	
	public State getState() {
		State o = button.getAsBoolean() ? (past ? State.HELD : State.PRESSED) :
		          (past ? State.RELEASED : State.UP);
		past = button.getAsBoolean();
		return o;
	}
	
	public boolean up() {
		return getState() == State.UP;
	}
	
	public boolean pressed() {
		return getState() == State.PRESSED;
	}
	
	public boolean held() {
		return getState() == State.HELD;
	}
	
	public boolean released() {
		return getState() == State.RELEASED;
	}
	
	@Override
	public boolean getAsBoolean() {
		return button.getAsBoolean();
	}
	
	public boolean isDown() {
		return button.getAsBoolean();
	}
	
	public enum State {
		UP,
		PRESSED,
		HELD,
		RELEASED
	}
	
}
