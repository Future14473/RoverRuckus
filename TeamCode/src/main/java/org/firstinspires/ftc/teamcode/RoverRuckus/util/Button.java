package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import java.util.function.BooleanSupplier;

/**
 * Utility for monitoring button state.
 */
public class Button {
	private final BooleanSupplier cur;
	private boolean past = false;
	
	public Button(BooleanSupplier button) {this.cur = button;}
	
	public State getState() {
		State o = cur.getAsBoolean() ? (past ? State.HELD : State.PRESSED) : (past ? State.RELEASED : State.UP);
		past = cur.getAsBoolean();
		return o;
	}
	
	public boolean open() {
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
	
	public boolean isDown() {
		return cur.getAsBoolean();
	}
	
	public enum State {
		UP, PRESSED, HELD, RELEASED;
		
		public boolean isDown() {
			return this == PRESSED || this == HELD;
		}
	}
	
}
