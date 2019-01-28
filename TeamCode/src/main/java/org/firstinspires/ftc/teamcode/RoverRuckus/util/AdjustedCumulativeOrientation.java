package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.function.Supplier;

/**
 * A utility wrapper class around another Supplier of Orientations, that keeps track of
 * position cumulatively (can do multiple revolutions).
 */
public class AdjustedCumulativeOrientation implements Supplier<Orientation> {
	private final Supplier<Orientation> rawOrientation;
	private Orientation curOrientation;
	
	public AdjustedCumulativeOrientation(Supplier<Orientation> rawOrientation) {
		//paranoia!!!
		this.rawOrientation = rawOrientation instanceof AdjustedCumulativeOrientation ?
				((AdjustedCumulativeOrientation) rawOrientation).rawOrientation : rawOrientation;
		this.curOrientation = rawOrientation.get();
	}
	
	@Override
	public Orientation get() {
		Orientation newOrientation = rawOrientation.get();
		float delta1 = ((newOrientation.firstAngle - curOrientation.firstAngle) + 180) % 360 - 180;
		float delta2 = ((newOrientation.secondAngle - curOrientation.secondAngle) + 180) % 360 - 180;
		float delta3 = ((newOrientation.thirdAngle - curOrientation.thirdAngle) + 180) % 360 - 180;
		curOrientation.firstAngle += delta1;
		curOrientation.secondAngle += delta2;
		curOrientation.thirdAngle += delta3;
		return curOrientation;
	}
}
