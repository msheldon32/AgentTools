package AgentTools.Algorithms;

import AgentTools.Environments.Environment;
import AgentTools.Policies.Policy;
import AgentTools.Util.ValueSpace;

public class RLAlgorithm {
    protected ValueSpace actionSpace;
    protected ValueSpace stateSpace;
    protected Policy policy;
    protected Object state;

    public RLAlgorithm() {
        this.actionSpace = null;
        this.stateSpace  = null;
        this.policy = null;
        this.state = null;
    }

    public void reinforce(Object startState, Object endState, Object action, double reward) {

    }

    public void setEnvironment(Environment environment) {
        this.actionSpace = environment.getActionSpace();
        this.stateSpace = environment.getStateSpace();
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    void setActionSpace(ValueSpace actionSpace) {
        this.actionSpace = actionSpace;
    }

    void setStateSpace(ValueSpace stateSpace) {
        this.stateSpace = stateSpace;
    }

    public Object getAction(Object state, Policy policy) {
        return null;
    }

    public Object getAction(Object state) {
        return this.getAction(state, this.policy);
    }

}