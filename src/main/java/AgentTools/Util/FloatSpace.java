package AgentTools.Util;

import java.util.Random;

public class FloatSpace extends ValueSpace<Double> {
    protected double min;
    protected double upperBound;

    public FloatSpace(double min, double upperBound) {
        this.min = min;
        this.upperBound = upperBound;
    }

    @Override
    public Double getRealization(Random random) {
        return random.nextDouble()*(upperBound-min)+min;
    }
}