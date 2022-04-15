package AgentTools.Policies;

import java.util.HashMap;
import AgentTools.Algorithms.RLAlgorithm;

public class StateActionPolicy extends Policy {
    HashMap<Object, Object> stateValueMap;
    public StateActionPolicy(HashMap<Object, Object> stateValueMap) {
        super();

        this.stateValueMap = stateValueMap;
    }

    @Override
    public PolicyType getType(RLAlgorithm algorithm) {
        return PolicyType.FixedStateAction;
    }

    @Override
    public Object getAction(Object state) {
        return this.stateValueMap.get(state);
    }
}
