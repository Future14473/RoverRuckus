package org.firstinspires.ftc.teamcode.RoverRuckus.util;

@SuppressWarnings({"unused", "WeakerAccess"})
public class PIDControl2D {
	private static final double MAX_ELAPSED_TIME  = 0.15;
	private static final double ELAPSED_TIME_MULT = 100;
	private              double p, i, d;
	private double maxOutputRamp, maxError;
	private double  maxOutput;
	private double  maxIOutput;
	private boolean reversed = false;
	
	private XY iOutput = new XY();
	
	private boolean noPrev     = true;
	private XY      target     = new XY();
	private XY      lastInput  = new XY();
	private XY      lastOutput = new XY();
	
	public PIDControl2D(double p, double i, double d) {
		this.setP(p);
		this.setI(i);
		this.setD(d);
	}
	
	public PIDControl2D(
			double p, double i, double d, double maxOutputRamp, double maxError,
			double maxOutput, double maxIOutput) {
		this.setP(p);
		this.setI(i);
		this.setD(d);
		this.maxOutputRamp = maxOutputRamp;
		this.maxError = maxError;
		this.maxOutput = maxOutput;
		this.maxIOutput = maxIOutput;
	}
	
	public void setPID(double p, double i, double d) {
		this.setP(p);
		this.setI(i);
		this.setD(d);
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
	
	public void setMaxOutput(double maxOutput) {
		this.maxOutput = maxOutput;
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
			elapsedTime = MAX_ELAPSED_TIME;
			noPrev = false;
		}
		elapsedTime *= ELAPSED_TIME_MULT;
		XY error = target.subtract(input);
		if (reversed) error = error.scale(-1);
		if (maxError != 0) error = error.limitMagnitudeTo(maxError);
		if (maxIOutput != 0) iOutput = iOutput.limitMagnitudeTo(maxIOutput);
		
		XY p = error.scale(this.getP());
		XY i = this.iOutput.scale(this.getI() * elapsedTime);
		XY d = input.subtract(lastInput).scale(-this.getD() / elapsedTime);
		XY output = p.add(i).add(d);
		
		
		double ramp = maxOutputRamp * elapsedTime / ELAPSED_TIME_MULT;
		if (maxOutput != 0 && output.magnitude() > maxOutput) {
			iOutput = XY.ZERO;
			output.limitMagnitudeTo(maxOutput);
		} else if (ramp != 0) {
			XY diff = output.subtract(lastOutput);
			if (diff.magnitude() > ramp) {
				iOutput = XY.ZERO;
				output = lastOutput.rampTo(output, ramp);
			}
		}
		
		iOutput = iOutput.add(error);
		
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
	
	public double getP() {
		return p;
	}
	
	public void setP(double p) {
		this.p = p;
	}
	
	public double getI() {
		return i;
	}
	
	public void setI(double i) {
		this.i = i;
	}
	
	public double getD() {
		return d;
	}
	
	public void setD(double d) {
		this.d = d;
	}
}
