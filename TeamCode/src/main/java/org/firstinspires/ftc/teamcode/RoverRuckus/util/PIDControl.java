package org.firstinspires.ftc.teamcode.RoverRuckus.util;

@SuppressWarnings({"unused", "WeakerAccess"})
public class PIDControl {
	private static final double MAX_ELAPSED_TIME  = 0.15;
	private static final double ELAPSED_TIME_MULT = 100;
	public               double p, i, d;
	private double maxOutputRamp, maxError;
	private double minOutput, maxOutput;
	private double  maxIOutput;
	private boolean reversed = false;
	
	private double iOutput = 0;
	
	private boolean noPrev     = true;
	private double  target     = 0;
	private double  lastInput  = 0;
	private double  lastOutput = 0;
	
	public PIDControl(double p, double i, double d) {
		this.p = p;
		this.i = i;
		this.d = d;
	}
	
	public PIDControl(
			double p, double i, double d, double maxOutputRamp, double maxError,
			double minOutput, double maxOutput, double maxIOutput) {
		this.p = p;
		this.i = i;
		this.d = d;
		this.maxOutputRamp = maxOutputRamp;
		this.maxError = maxError;
		this.minOutput = minOutput;
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
	
	public double getMinOutput() {
		return minOutput;
	}
	
	public void setMaxOutputs(double minOutput, double maxOutput) {
		if (maxOutput < minOutput) throw new IllegalArgumentException();
		this.minOutput = minOutput;
		this.maxOutput = maxOutput;
	}
	
	public double getMaxOutput() {
		return maxOutput;
	}
	
	public void setMaxOutputs(double v) {
		v = Math.abs(v);
		this.minOutput = -v;
		this.maxOutput = v;
	}
	
	public double getMaxIOutput() {
		return maxIOutput;
	}
	
	public void setMaxIOutput(double maxIOutput) {
		if (maxIOutput < 0) throw new IllegalArgumentException();
		this.maxIOutput = maxIOutput;
	}
	
	public double getIOutput() {
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
	public double getOutput(double input, double elapsedTime) {
		if (noPrev || elapsedTime > MAX_ELAPSED_TIME) {
			lastInput = input;
			elapsedTime = MAX_ELAPSED_TIME;
			noPrev = false;
		}
		elapsedTime *= ELAPSED_TIME_MULT;
		double error = target - input;
		if (reversed) error *= -1;
		if (maxError != 0) error = limit(error, maxError);
		if (maxIOutput != 0) iOutput = limit(iOutput, maxIOutput);
		
		double p = this.p * error;
		double i = this.i * this.iOutput * elapsedTime;
		double d = -this.d * (input - lastInput) / elapsedTime;
		double output = p + i + d;
		
		
		double ramp = maxOutputRamp * elapsedTime / ELAPSED_TIME_MULT;
		if (minOutput != maxOutput && (output < minOutput || output > maxOutput)) {
			iOutput = 0;
			output = limit(output, minOutput, maxOutput);
		} else if (ramp != 0 && (output < lastOutput - ramp ||
		                         output > lastOutput + ramp)) {
			iOutput = 0;
			output = limit(output, lastOutput - ramp, lastOutput + ramp);
		}
		iOutput += error;
		
		
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
	
	public double getTarget() {
		return target;
	}
	
	public void setTarget(double target) {
		this.target = target;
	}
	
	public boolean isReversed() {
		return reversed;
	}
	
	public void setReversed(boolean reversed) {
		this.reversed = reversed;
	}
}
