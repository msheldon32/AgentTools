package AgentTools.Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.IntStream;

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

    @Override
    public Iterator<Integer> iterator() {
        return IntStream.range(this.min, this.upperBound).iterator();
    }

    @Override
    public int getSize() {
        return upperBound-min;
    }
}
