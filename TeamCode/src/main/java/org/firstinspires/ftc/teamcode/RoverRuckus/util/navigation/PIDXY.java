package org.firstinspires.ftc.teamcode.RoverRuckus.util.navigation;

import com.qualcomm.robotcore.hardware.PIDCoefficients;

import static org.firstinspires.ftc.teamcode.RoverRuckus.Constants.MAX_ELAPSED_TIME;

@SuppressWarnings({"unused", "WeakerAccess"})
public class PIDXY {
	private static final double ELAPSED_TIME_MULT = 200;
	
	public double p, i, d;
	private double maxOutputRamp, maxError;
	private double  maxOutput;
	private double  maxIOutput;
	private boolean reversed = false;
	
	private XY iOutput = XY.ZERO;
	
	private boolean noPrev     = true;
	private XY      target     = XY.ZERO;
	private XY      lastInput  = XY.ZERO;
	private XY      lastOutput = XY.ZERO;
	
	public PIDXY(double p, double i, double d) {
		this.p = p;
		this.i = i;
		this.d = d;
	}
	
	public PIDXY(PIDCoefficients coefficients) {
		this(coefficients.p, coefficients.i, coefficients.d);
	}
	
	public PIDXY(
			double p, double i, double d, double maxOutputRamp, double maxError,
			double maxOutput, double maxIOutput) {
		this.p = p;
		this.i = i;
		this.d = d;
		this.maxOutputRamp = maxOutputRamp;
		this.maxError = maxError;
		this.maxOutput = maxOutput;
		this.maxIOutput = maxIOutput;
	}
	
	public void setPID(double p, double i, double d) {
		this.p = p;
		this.i = i;
		this.d = d;
	}
	
	public double getMaxOutputRamp() {
		return maxOutputRamp;
	}
	
	public void setRampRate(double maxOutputRamp) {
		if (maxOutputRamp < 0) throw new IllegalArgumentException();
		this.maxOutputRamp = maxOutputRamp;
	}
	
	public double getMaxError() {
		return maxError;
	}
	
	public void setMaxError(double maxError) {
		if (maxOutputRamp < 0) throw new IllegalArgumentException();
		this.maxError = maxError;
	}
	
	public double getMaxOutput() {
		return maxOutput;
	}
	
	public void setMaxOutput(double v) {
		v = Math.abs(v);
		this.maxOutput = v;
	}
	
	public double getMaxIOutput() {
		return maxIOutput;
	}
	
	public void setMaxIOutput(double maxIOutput) {
		if (maxIOutput < 0) throw new IllegalArgumentException();
		this.maxIOutput = maxIOutput;
	}
	
	public XY getIOutput() {
		return iOutput;
	}
	
	/**
	 * Actually does the PID calculations.
	 *
	 * @param input       The input value, used to adjust
	 * @param elapsedTime Elapsed time since last call, for integral and derivative adjustments
	 * @return The calculated output
	 */
	@SuppressWarnings("Duplicates")
	public XY getOutput(XY input, double elapsedTime) {
		if (noPrev || elapsedTime > MAX_ELAPSED_TIME) {
			lastInput = input;
			elapsedTime = 0;
			noPrev = false;
		}
		XY error = target.subtract(input);
		if (reversed) error = error.scale(-1);
		if (maxError != 0) error = error.limitMagnitudeTo(maxError);
		if (maxIOutput != 0) iOutput = iOutput.limitMagnitudeTo(maxIOutput);
		
		XY p = error.scale(this.p);
		XY i = this.iOutput.scale(this.i * elapsedTime * ELAPSED_TIME_MULT);
		XY d = elapsedTime == 0 ? XY.ZERO :
		       input.subtract(lastInput).scale(-this.d / (elapsedTime * ELAPSED_TIME_MULT));
		XY output = p.add(i).add(d);
		
		
		double ramp = elapsedTime == 0 ? 0 : maxOutputRamp * elapsedTime;
		if (maxOutput != 0 && output.magnitude() > maxOutput) {
			iOutput = XY.ZERO;
			output = output.limitMagnitudeTo(maxOutput);
		}
		if (maxOutputRamp != 0 && output.subtract(lastOutput).magnitude() > ramp) {
			iOutput = XY.ZERO;
			output = lastOutput.rampTo(output, ramp);
		}
		iOutput = iOutput.add(error);
//		RobotLog.dd("PID",
//		            "%nCurrent: %.5f; Target: %.5f%nError: %.5f%nPID outputs: %.5f, %.5f, %" +
//		            ".5f%nOutput: %.5f",
//		            input, target, error, p, i, d, output);
		
		lastInput = input;
		lastOutput = output;
		return output;
	}
	
	private double limit(double v, double min, double max) {
		return v > max ? max : v < min ? min : v;
	}
	
	private double limit(double v, double m) {
		return limit(v, -m, m);
	}
	
	public XY getTarget() {
		return target;
	}
	
	public void setTarget(XY target) {
		this.target = target;
	}
	
	public boolean isReversed() {
		return reversed;
	}
	
	public void setReversed(boolean reversed) {
		this.reversed = reversed;
	}
	
	public void setCoefficients(PIDCoefficients coefficients) {
		this.p = coefficients.p;
		this.i = coefficients.i;
		this.d = coefficients.d;
	}
}
