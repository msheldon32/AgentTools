package AgentTools.Algorithms;

import AgentTools.Function.TabularV;
import AgentTools.Function.VFunction;
import AgentTools.Policies.GreedyPolicy;
import AgentTools.Policies.Policy;
import AgentTools.Policies.PolicyType;
import AgentTools.Util.ValueSpace;

import java.util.HashMap;

public class TemporalDifferenceZero extends RLAlgorithm {
    protected VFunction vFunction;

    Policy greedyPolicy;

    public TemporalDifferenceZero(AlgoConfiguration algoConfiguration, VFunction vFunction) {
        super(algoConfiguration);
        this.vFunction = vFunction;

        this.greedyPolicy = new GreedyPolicy();
    }

    public TemporalDifferenceZero(AlgoConfiguration algoConfiguration) {
        this(algoConfiguration, new TabularV());
    }

    @Override
    public Object getAction(Object state, Policy policy) {
        PolicyType policyType = policy.getType(this);
        if (policyType == PolicyType.Optimal) {
            throw new RuntimeException("Invalid Policy for Algorithm");
        } else if (policyType == PolicyType.Random) {
            return this.algoConfiguration.actionSpace.getRealization(policy.getRandom());
        } else if (policyType == PolicyType.FixedStateAction) {
            return policy.getAction(state);
        } else if (policyType == PolicyType.Probablistic) {
            return policy.getCdf(state).generate();
        }

        return null;
    }

    @Override
    public void reinforce(Object startState, Object endState, Object action, double reward) {
        double end_v = this.vFunction.getValue(endState);

        double old_v = this.vFunction.getValue(startState);

        double temporalDifference = this.algoConfiguration.learningRate * (reward + (this.algoConfiguration.discountRate * end_v) - old_v);

        double new_v = old_v + temporalDifference;
        this.vFunction.updateValue(startState, new_v);
    }
}
