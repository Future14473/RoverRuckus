package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import java.util.function.BooleanSupplier;

/**
 * Utility for monitoring button state.
 */
@SuppressWarnings("unused")
public class Button implements BooleanSupplier {
	private final BooleanSupplier cur;
	private boolean past = false;
	
	public Button(BooleanSupplier button) {this.cur = button;}
	
	public State getState() {
		State o = cur.getAsBoolean() ? (past ? State.DOWN : State.PRESSED) : (past ? State.RELEASED : State.UP);
		past = cur.getAsBoolean();
		return o;
	}
	
	public boolean up() {
		return getState() == State.UP;
	}
	
	public boolean pressed() {
		return getState() == State.PRESSED;
	}
	
	public boolean down() {
		return getState() == State.DOWN;
	}
	
	public boolean released() {
		return getState() == State.RELEASED;
	}
	
	@Override
	public boolean getAsBoolean() {
		return cur.getAsBoolean();
	}
	
	public enum State {
		UP,
		PRESSED,
		DOWN,
		RELEASED
	}
	
}
