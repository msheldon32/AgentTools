package AgentTools.Function;

import AgentTools.Util.MultiSpace;
import AgentTools.Util.ValueSpace;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LinearApproximator extends FunctionApproximator {
    public LinearApproximator(List<Object> params, ValueSpace domain) {
        super(params, domain);

        assert (domain instanceof MultiSpace);
    }

    @Override
    public Object getSA(Object state, Object action) {
        assert (state instanceof Collection);
        assert (action instanceof Collection);

        Stream saStream = Stream.concat(((Collection<?>) state).stream(), ((Collection<?>) action).stream());
        return saStream.collect(Collectors.toList());
    }

    @Override
    public double getValue(Object inValue) {
        Iterator<Object> spaceIter = ((Collection)inValue).iterator();
        Iterator<Object> weightIter = ((Collection<Object>)this.params).iterator();

        double acc = 0;

        while (spaceIter.hasNext() && weightIter.hasNext()) {
            acc += (Double)spaceIter.next() * (Double)weightIter.next();
        }

        return acc;
    }
}
