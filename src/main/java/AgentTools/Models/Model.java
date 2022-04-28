package AgentTools.Models;

import AgentTools.Util.ValueSpace;

public class Model {
    public Model() {

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

    public void reset() {
        return;
    }
}
