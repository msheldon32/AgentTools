package AgentTools.Util;

import java.util.List;
import java.util.Random;

public class SingleSpace extends ValueSpace<Integer> {
    protected int value;
    public SingleSpace(int value) {
        super();
        this.value = value;
    }

    public SingleSpace() {
        this(0);
    }

    @Override
    public Integer getRealization(Random random) {
        return this.value;
    }

    @Override
    public int getSize() {
        return 1;
    }
}
