package AgentTools.Function;

import AgentTools.Util.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LinearApproximator extends FunctionApproximator {
    Random random;
    public LinearApproximator(List<Object> params, ValueSpace domain, double learningRate, Random random) {
        super(params, domain, learningRate);

        assert (domain instanceof MultiSpace);

        this.random = random;
    }
    public LinearApproximator(List<Object> params, ValueSpace domain, double learningRate) {
        this(params, domain, learningRate, new Random());
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
        Iterator<Object> spaceIter = ((Collection)inValue).iterator();
        Iterator<Object> weightIter = ((Collection<Object>)this.params).iterator();

        double acc = 0;

        while (spaceIter.hasNext() && weightIter.hasNext()) {
            acc += (Double)spaceIter.next() * (Double)weightIter.next();
        }

        while (weightIter.hasNext()) {
            // bias terms
            acc += (Double)weightIter.next();
        }

        return acc;
    }

    public List<Double> getGradients(Object inValue) {
        Iterator<Object> spaceIter = ((Collection)inValue).iterator();
        Iterator<Object> weightIter = ((Collection<Object>)this.params).iterator();

        List<Double> outList = new ArrayList<Double>();

        while (spaceIter.hasNext() && weightIter.hasNext()) {
            outList.add((Double)spaceIter.next());
            weightIter.next();
        }

        while (weightIter.hasNext()) {
            // bias terms
            weightIter.next();
            outList.add(1.0);
        }

        return outList;
    }

    @Override
    public void fitResult(Object inValue, double newVal) {
        // 1. get gradients for func(inValue, weights)
        // 2. update:
        //     weights <-  weights + learning_rate * (newVal - func(inVal, weights)) * gradient_w
        double diffTerm = this.learningRate * (newVal - this.getValue(inValue));
        List<Double> grads = this.getGradients(inValue);

        for (int i = 0; i < this.params.size(); i++) {
            double w = (Double)this.params.get(i);
            double wUpdate = diffTerm * grads.get(i);

            /*System.out.format("diff: %f\n", diffTerm);
            System.out.format("w: %f\n", w);
            System.out.format("grad: %f\n", grads.get(i));
            System.out.format("wUpdate: %f\n", wUpdate);*/
            this.params.set(i, w + wUpdate);
        }
    }

    @Override
    public Object getMaxAction(Object state, ValueSpace actionSpace) {
        if (actionSpace instanceof IntegerSpace) {
            int minInt = ((IntegerSpace)actionSpace).getMin();
            int maxInt = ((IntegerSpace)actionSpace).getMax();
            Object minObj = this.getSA(state, minInt);
            Object maxObj = this.getSA(state, maxInt);

            if (this.getValue(minObj) > this.getValue(maxObj)) {
                return minInt;
            } else if (this.getValue(minObj) > this.getValue(maxObj)) {
                return maxInt;
            } else {
                if (this.random.nextDouble() <= 0.5) {
                    return minInt;
                } else {
                    return maxInt;
                }
            }
        }
        if (actionSpace instanceof FloatSpace) {
            double minVal = ((FloatSpace)actionSpace).getMin();
            double maxVal = ((FloatSpace)actionSpace).getMax();
            Object minObj = this.getSA(state, minVal);
            Object maxObj = this.getSA(state, maxVal);

            if (this.getValue(minObj) > this.getValue(maxObj)) {
                return minVal;
            } else if (this.getValue(minObj) > this.getValue(maxObj)) {
                return maxVal;
            } else {
                if (this.random.nextDouble() <= 0.5) {
                    return minVal;
                } else {
                    return maxVal;
                }
            }
        }
        if (actionSpace instanceof ListSpace) {
            Iterator spaceIter = ((ListSpace)actionSpace).iterator();

            double curMax = Double.NEGATIVE_INFINITY;
            Object argMax = null;

            while (spaceIter.hasNext()) {
                Object val = spaceIter.next();
                Object valSpace = this.getSA(state, val);
                double valValue = this.getValue(valSpace);
                if (valValue > curMax) {
                    curMax = valValue;
                    argMax = val;
                }
            }

            return argMax;
        }

        if (actionSpace instanceof MultiSpace) {
            List<Object> outActions = new ArrayList<Object>();
            for (ValueSpace subSpace : ((MultiSpace) actionSpace).getSpaces()) {
                outActions.add(this.getMaxAction(state, subSpace));
            }

            return outActions;
        }

        return null;
    }
}
