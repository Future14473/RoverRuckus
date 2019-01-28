package org.firstinspires.ftc.teamcode.RoverRuckus.util;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.function.Supplier;

/**
 * A utility wrapper class around another Supplier of Orientations, that keeps track of
 * position cumulatively (can do multiple revolutions). For consistency, all orientations
 * are adjusted to: the following settings: DEGREES, ZYX, EXTRINSIC
 */
public class AdjustedCumulativeOrientation implements Supplier<Orientation> {
	private final Supplier<Orientation> rawOrientation;
	private Orientation curOrientation;
	
	public AdjustedCumulativeOrientation(Supplier<Orientation> rawOrientation) {
		//paranoia!!!
		this.rawOrientation = rawOrientation instanceof AdjustedCumulativeOrientation ?
				((AdjustedCumulativeOrientation) rawOrientation).rawOrientation : rawOrientation;
		this.curOrientation = adjustOrientation(rawOrientation.get());
	}
	
	@Override
	public Orientation get() {
		Orientation newOrientation = adjustOrientation(rawOrientation.get());
		float delta1 = ((newOrientation.firstAngle - curOrientation.firstAngle) + 180) % 360 - 180;
		float delta2 = ((newOrientation.secondAngle - curOrientation.secondAngle) + 180) % 360 - 180;
		float delta3 = ((newOrientation.thirdAngle - curOrientation.thirdAngle) + 180) % 360 - 180;
		curOrientation.firstAngle += delta1;
		curOrientation.secondAngle += delta2;
		curOrientation.thirdAngle += delta3;
		return curOrientation;
	}
	
	public static Orientation adjustOrientation(Orientation orientation) {
		return orientation.toAngleUnit(AngleUnit.DEGREES).toAxesOrder(AxesOrder.ZYX).toAxesReference(AxesReference.EXTRINSIC);
	}
}
