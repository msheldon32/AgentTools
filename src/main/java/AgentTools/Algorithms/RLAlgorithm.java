package AgentTools.Algorithms;

import AgentTools.Algorithms.Configuration.AlgoConfiguration;
import AgentTools.Environments.Environment;
import AgentTools.Policies.Policy;

public class RLAlgorithm {
    protected AlgoConfiguration algoConfiguration;
    protected Policy policy;
    protected Object state;

    public RLAlgorithm(AlgoConfiguration algoConfiguration) {
        this.algoConfiguration = algoConfiguration;
        this.policy = null;
        this.state = null;
    }

    public void reinforce(Object startState, Object endState, Object action, double reward) {

    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public Object getAction(Object state, Policy policy) {
        return null;
    }

    public Object getAction(Object state) {
        return this.getAction(state, this.policy);
    }

    public void setEnvironment(Environment environment) {

    }

}