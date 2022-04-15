package AgentTools.Util;

import java.util.List;
import java.util.Random;

public class ListSpace<T> extends ValueSpace<T> {
    protected List<T> values;
    public ListSpace(List<T> values) {
        this.values = values;
    }

    @Override
    public T getRealization(Random random) {
        return values.get(random.nextInt(values.size()));
    }
}
