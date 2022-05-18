package AgentTools.Algorithms;

import AgentTools.Algorithms.Configuration.AlgoConfiguration;
import AgentTools.Function.QFunction;
import AgentTools.Function.TabularQ;
import AgentTools.Policies.GreedyPolicy;
import AgentTools.Policies.Policy;
import AgentTools.Policies.PolicyType;

import java.util.Random;

public class QLearning extends RLAlgorithm {
    protected QFunction qFunction;


    Policy greedyPolicy;

    public QLearning(AlgoConfiguration algoConfiguration, QFunction qFunction) {
        super(algoConfiguration);
        this.qFunction = qFunction;

        this.greedyPolicy = new GreedyPolicy();
    }

    public QLearning(AlgoConfiguration algoConfiguration) {
        this(algoConfiguration, new TabularQ(algoConfiguration.actionSpace, new Random()));

        this.greedyPolicy = new GreedyPolicy();
    }

    @Override
    public Object getAction(Object state, Policy policy) {
        PolicyType policyType = policy.getType(this);

        if (policyType == PolicyType.Optimal) {
            return this.qFunction.getMaxAction(state, this.algoConfiguration.actionSpace);
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
        double action_q = this.qFunction.getValue(endState, this.qFunction.getMaxAction(endState, this.algoConfiguration.actionSpace));
        double old_q = this.qFunction.getValue(startState, action);

        double temp_diff = reward + (this.algoConfiguration.discountRate*action_q)-old_q;

        double new_q = old_q + (this.algoConfiguration.learningRate*temp_diff);
        this.qFunction.updateValue(startState, action, new_q);
    }
}
