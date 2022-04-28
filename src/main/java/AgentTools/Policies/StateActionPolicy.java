package AgentTools.Policies;

import java.util.HashMap;
import AgentTools.Algorithms.RLAlgorithm;

public class StateActionPolicy extends Policy {
    HashMap<Object, Object> stateActionMap;
    public StateActionPolicy(HashMap<Object, Object> stateActionMap) {
        super();

        this.stateActionMap = stateActionMap;
    }

    public StateActionPolicy() {
        this(new HashMap<Object, Object>());
    }

    @Override
    public PolicyType getType(RLAlgorithm algorithm) {
        return PolicyType.FixedStateAction;
    }

    @Override
    public Object getAction(Object state) {
        return this.stateActionMap.get(state);
    }

    public void setStateAction(Object state, Object action) {
        this.stateActionMap.put(state, action);
    }
}
