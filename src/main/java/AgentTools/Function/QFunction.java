package AgentTools.Function;

public interface QFunction {
    double getValue(Object state, Object action);
    void updateValue(Object state, Object action, double newVal);
    Object getMaxAction(Object state);
}
