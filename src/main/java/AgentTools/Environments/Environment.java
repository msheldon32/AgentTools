package AgentTools.Environments;
import AgentTools.Util.ValueSpace;

public class Environment {
    public Environment() {

    }

    public ValueSpace getStateSpace() {
        return null;
    }

    public ValueSpace getActionSpace() {
        return null;
    }

    public Object getState() {
        return null;
    }

    public double applyAction(Object action) {
        return 0.0;
    }
}