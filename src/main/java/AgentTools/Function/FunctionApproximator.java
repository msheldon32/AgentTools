package AgentTools.Function;

import AgentTools.Util.Pair;
import AgentTools.Util.ValueSpace;

import java.util.ArrayList;
import java.util.List;

public class FunctionApproximator {
    protected List<Object> params;
    protected ValueSpace domain;
    protected double learningRate;

    public FunctionApproximator(List<Object> params, ValueSpace domain, double learningRate) {
        this.params = params;
        this.domain = domain;
        this.learningRate = learningRate;
    }

    public double getValue(Object inValue) {
        return 0.0;
    }

    public Object getSA(Object state, Object value) {
        List<Object> outList = new ArrayList<Object>(2);
        outList.add(state);
        outList.add(value);
        return outList;
    }

    public double getQ(Object state, Object value) {
        return this.getV(this.getSA(state, value));
    }

    public double getV(Object state) {
        return this.getValue(state);
    }

    public void fitResult(Object inValue, double newVal) {

    }

    public void fitResultV(Object state, double v) {
        this.fitResult(state, v);
    }

    public void fitResultQ(Object state, Object action, double v) {
        this.fitResult(this.getSA(state, action), v);
    }

    public Object getMaxAction(Object state, ValueSpace actionSpace) {
        return null;
    }
}
