package AgentTools.Function;

import java.util.List;

public class FunctionApproximator {
    protected List<Object> params;

    public FunctionApproximator(List<Object> params) {
        this.params = params;
    }

    public double getQ(Object state, Object value) {
        return 0.0;
    }

    public double getV(Object state) {
        return 0.0;
    }

    public void fitResultV(Object state, double v) {

    }

    public void fitResultQ(Object state, Object action, double v) {

    }

    public void fitResults(List<Object> states, List<Double> rewards) {

    }

    public void fitResults(Object[] states, double[] rewards) {

    }
}
