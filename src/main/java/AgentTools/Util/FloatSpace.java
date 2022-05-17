package AgentTools.Util;

import java.util.Random;

public class FloatSpace extends ValueSpace<Double> {
    protected double min;
    protected double upperBound;
    protected double eps;

    public FloatSpace(double min, double upperBound) {
        this.min = min;
        this.upperBound = upperBound;

        this.eps = 0.0001;
    }

    @Override
    public Double getRealization(Random random) {
        return random.nextDouble()*(upperBound-min)+min;
    }

    @Override
    public int getSize() {
        return Integer.MAX_VALUE;
    }

    public double getMin() {
        return this.min;
    }

    public double getMax() {
        return this.upperBound - this.eps;
    }
}