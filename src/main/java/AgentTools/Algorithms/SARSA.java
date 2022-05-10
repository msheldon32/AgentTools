package AgentTools.Algorithms;

import AgentTools.Function.QFunction;
import AgentTools.Function.TabularQ;
import AgentTools.Policies.Policy;
import AgentTools.Policies.PolicyType;
import AgentTools.Util.ValueSpace;
import com.sun.jdi.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SARSA extends RLAlgorithm {
    protected QFunction qFunction;

    Object nextAction;
    Policy policy;

    public SARSA(AlgoConfiguration algoConfiguration, Policy policy, QFunction qFunction) {
        super(algoConfiguration);
        this.qFunction = qFunction;

        this.nextAction = null;
        this.policy = policy;
    }

    public SARSA(AlgoConfiguration algoConfiguration, Policy policy) {
        this(algoConfiguration, policy, new TabularQ(algoConfiguration.actionSpace, algoConfiguration.random));
    }

    protected void findNextAction(Object state, Policy policy) {
        PolicyType policyType = policy.getType(this);
        if (policyType == PolicyType.Optimal) {
            this.nextAction = this.qFunction.getMaxAction(state);
        } else if (policyType == PolicyType.Random) {
            this.nextAction = this.algoConfiguration.actionSpace.getRealization(policy.getRandom());
        } else if (policyType == PolicyType.FixedStateAction) {
            this.nextAction = policy.getAction(state);
        } else if (policyType == PolicyType.Probablistic) {
            this.nextAction = policy.getCdf(state).generate();
        }
    }

    @Override
    public Object getAction(Object state, Policy policy) {
        if (this.nextAction == null) {
            this.findNextAction(state, policy);
        }
        Object outAction = this.nextAction;

        this.nextAction = null;

        return outAction;
    }

    @Override
    public void reinforce(Object startState, Object endState, Object action, double reward) {
        this.findNextAction(endState, this.policy);

        double action_q = this.qFunction.getValue(endState, this.nextAction);

        double old_q = this.qFunction.getValue(startState, action);
        double temp_difference = this.algoConfiguration.learningRate * (reward + (this.algoConfiguration.discountRate * action_q) - old_q);

        double new_q = old_q + temp_difference;

        this.qFunction.updateValue(startState, action, new_q);
    }
}
