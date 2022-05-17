package AgentTools.Function;

import AgentTools.Util.ValueSpace;

import java.util.HashMap;

public class ApproximateQ implements QFunction {
    FunctionApproximator approximator;

    public ApproximateQ(FunctionApproximator approximator) {
        this.approximator = approximator;
    }

    public double getValue(Object state, Object action) {
        return this.approximator.getQ(state, action);
    }

    public void updateValue(Object state, Object action, double newVal) {
        this.approximator.fitResultQ(state, action, newVal);
    }

    public Object getMaxAction(Object state, ValueSpace actionSpace) {
        return this.approximator.getMaxAction(state, actionSpace);
    }
}