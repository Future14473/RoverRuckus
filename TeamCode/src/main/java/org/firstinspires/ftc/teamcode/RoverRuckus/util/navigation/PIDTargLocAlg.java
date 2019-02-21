package org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation;

import com.qualcomm.robotcore.hardware.PIDCoefficients;

public interface PIDTargLocAlg extends TargetLocationAlgorithm {
	
	void setTranslationPIDCoefficients(PIDCoefficients coefficients);
	
	void setAngularPIDCoefficients(PIDCoefficients coefficients);
	
}
