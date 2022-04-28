package AgentTools.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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

    public Iterator<T> iterator() {
        throw new RuntimeException("Iteration is not supported by this ValueSpace!");
    }

    public int getSize() {
        return 0;
    }
}