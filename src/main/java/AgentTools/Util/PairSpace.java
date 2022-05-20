package AgentTools.Util;

import java.util.Iterator;
import java.util.Random;

public class PairSpace<T, U> extends ValueSpace<Pair<T, U>> {
    protected Pair<ValueSpace<T>, ValueSpace<U>> valueSpaces;

    public PairSpace(ValueSpace<T> left, ValueSpace<U> right) {
        this.valueSpaces = new Pair<ValueSpace<T>, ValueSpace<U>>(left, right);
    }

    @Override
    public Pair<T,U> getRealization(Random random) {
        return new Pair<T, U>(this.valueSpaces.getLeft().getRealization(random), this.valueSpaces.getRight().getRealization(random));
    }

    @Override
    public Iterator<Pair<T, U>> iterator() {
        return null;
    }

    @Override
    public int getSize() {
        return valueSpaces.getLeft().getSize() * valueSpaces.getRight().getSize();
    }

    public ValueSpace<T> leftSpace() {
        return this.valueSpaces.getLeft();
    }

    public ValueSpace<U> rightSpace() {
        return this.valueSpaces.getRight();
    }
}
