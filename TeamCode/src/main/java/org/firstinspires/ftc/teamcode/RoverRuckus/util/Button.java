package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import java.util.function.BooleanSupplier;

/**
 * Utility for monitoring button state.
 */
public class Button {
	private final BooleanSupplier cur;
	private boolean past = false;
	public Button(BooleanSupplier cur) {this.cur = cur;}
	
	public State getState() {
		State o = cur.getAsBoolean() ? (past ? State.HELD : State.PRESSED) : (past ? State.RELEASED : State.OPEN);
		past = cur.getAsBoolean();
		return o;
	}
	
	public enum State {
		OPEN, PRESSED, HELD, RELEASED
	}

}
