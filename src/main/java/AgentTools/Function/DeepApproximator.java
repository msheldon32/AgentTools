package AgentTools.Function;

import AgentTools.Util.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DeepApproximator extends FunctionApproximator{
    /*
            This is a wrapper for deeplearning4j
     */
    public DeepApproximator(List<Object> params, ValueSpace domain, double learningRate) {
        super(params, domain, learningRate);

        assert (domain instanceof MultiSpace);
    }

    

    @Override
    public Object getSA(Object state, Object action) {
        Stream stateStream;
        Stream actionStream;

        if (state instanceof Collection) {
            stateStream = ((Collection)state).stream();
        } else {
            stateStream = Stream.of(state);
        }
        if (action instanceof Collection) {
            actionStream = ((Collection)action).stream();
        } else {
            actionStream = Stream.of(action);
        }

        Stream saStream = Stream.concat(stateStream, actionStream);
        return saStream.collect(Collectors.toList());
    }

    @Override
    public double getValue(Object inValue) {
        return 0.0;
    }

    @Override
    public void fitResult(Object inValue, double newVal) {

    }

    @Override
    public Object getMaxAction(Object state, ValueSpace actionSpace) {
    }
}
