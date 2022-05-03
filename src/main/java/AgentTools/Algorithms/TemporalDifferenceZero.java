package AgentTools.Algorithms;

import AgentTools.Policies.GreedyPolicy;
import AgentTools.Policies.Policy;
import AgentTools.Policies.PolicyType;

import java.util.HashMap;

public class TemporalDifferenceZero extends RLAlgorithm {
    protected HashMap<Object, Double> stateFunction;
    double learningRate;
    double discountRate;
    Policy greedyPolicy;

    public TemporalDifferenceZero(double discountRate, double learningRate) {
        super();
        this.stateFunction = new HashMap<Object, Double>();

        this.learningRate = learningRate;
        this.discountRate = discountRate;
        this.greedyPolicy = new GreedyPolicy();
    }

    public TemporalDifferenceZero() {
        this(0.05, 0.05);
    }

    @Override
    public Object getAction(Object state, Policy policy) {
        PolicyType policyType = policy.getType(this);
        if (policyType == PolicyType.Optimal) {
            throw new RuntimeException("Invalid Policy for Algorithm");
        } else if (policyType == PolicyType.Random) {
            return this.actionSpace.getRealization(policy.getRandom());
        } else if (policyType == PolicyType.FixedStateAction) {
            return policy.getAction(state);
        } else if (policyType == PolicyType.Probablistic) {
            return policy.getCdf(state).generate();
        }

        return null;
    }

    @Override
    public void reinforce(Object startState, Object endState, Object action, double reward) {
        Object idealAction = this.getAction(endState);

        double end_v = 0;
        if (this.stateFunction.containsKey(endState)) {
            end_v = this.stateFunction.get(endState);
        }

        double old_v = 0;

        if (this.stateFunction.containsKey(startState)) {
            old_v = this.stateFunction.get(startState);
        }

        double new_v = (1-this.learningRate) * old_v + (this.learningRate * (reward + (this.discountRate * end_v)));
        this.stateFunction.put(startState, new_v);
    }
}
