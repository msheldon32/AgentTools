package AgentTools.Util;

import java.util.Random;

public class IntegerSpace extends ValueSpace<Integer> {
    protected int min;
    protected int upperBound;

    public IntegerSpace(int min, int upperBound) {
        this.min = min;
        this.upperBound = upperBound;
    }

    @Override
    public Integer getRealization(Random random) {
        return random.nextInt(upperBound-min) + min;
    }
}
