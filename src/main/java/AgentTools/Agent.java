package AgentTools;

import AgentTools.Environments.Environment;
import AgentTools.Policies.Policy;
import AgentTools.Algorithms.RLAlgorithm;
import AgentTools.Util.ValueSpace;

public class Agent {
    protected double totalReward;
    protected ValueSpace stateSpace;
    protected ValueSpace actionSpace;
    protected Policy policy;
    protected RLAlgorithm algorithm;
    protected Object state;
    protected Object prevState;
    protected Object action;
    protected Object prevAction;
    protected Environment environment;

    public Agent(Environment environment, RLAlgorithm algorithm, Policy policy, ValueSpace stateSpace, ValueSpace actionSpace) {
        this.totalReward = 0.0;
        this.algorithm = algorithm;
        this.policy = policy;
        this.stateSpace = stateSpace;
        this.actionSpace = actionSpace;
        this.state = null;
        this.prevState = null;
        this.action = null;
        this.prevAction = null;
        this.environment = environment;
        algorithm.setModel(this.environment);
    }

    public void setState(Object newState) {
        this.prevState = this.state;
        this.state = newState;
    }

    public void reinforce (double reward) {
        this.totalReward += reward;
        this.algorithm.reinforce(this.prevState, this.state, this.action, reward);
    }

    public Object takeAction() {
        this.prevAction = this.action;
        this.action = this.algorithm.getAction(this.state, this.policy);

        return this.action;
    }

    public double getTotalReward() {
        return this.totalReward;
    }

    public RLAlgorithm getAlgorithm() {
        return this.algorithm;
    }
}
