package AgentTools.Function;

public class ApproximateV implements VFunction {
    FunctionApproximator approximator;

    public ApproximateV(FunctionApproximator approximator) {
        this.approximator = approximator;
    }

    public double getValue(Object state) {
        return this.approximator.getV(state);
    }

    public void updateValue(Object state, double newVal) {
        this.approximator.fitResultV(state, newVal);
    }
}
