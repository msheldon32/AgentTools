package AgentTools.Util;

import java.util.Random;

public class ValueSpace<T> {
    public ValueSpace() {

    }

    public T castValue(Object object) {
        return (T) object;
    }

    public T getRealization(Random random) {
        return null;
    }
}