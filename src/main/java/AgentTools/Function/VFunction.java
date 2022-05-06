package AgentTools.Function;

public interface VFunction {
    double getValue(Object state);
    void updateValue(Object state, double newVal);
}
