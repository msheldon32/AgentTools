package AgentTools.Algorithms;

import AgentTools.Policies.Policy;
import AgentTools.Policies.PolicyType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SARSA extends RLAlgorithm {
    protected HashMap<Object, HashMap<Object, Double>> qFunction;
    double learningRate;
    double discountRate;
    Object nextAction;
    Policy policy;

    public SARSA(Policy policy, double discountRate, double learningRate) {
        super();
        this.qFunction = new HashMap<Object, HashMap<Object, Double>>();

        this.learningRate = learningRate;
        this.discountRate = discountRate;
        this.nextAction = null;
        this.policy = policy;
    }

    public SARSA(Policy policy) {
        this(policy, 0.05, 0.05);
    }

    protected void findNextAction(Object state, Policy policy) {

        PolicyType policyType = policy.getType(this);
        if (policyType == PolicyType.Optimal) {
            if (!this.qFunction.containsKey(state)) {
                this.nextAction = this.actionSpace.getRealization(policy.getRandom());
                return;
            }
            HashMap<Object, Double> actionQ = this.qFunction.get(state);
            List<Object> bestActions = new ArrayList<Object>();

            double maxScore = Double.NEGATIVE_INFINITY;
            for (Object action : actionQ.keySet()) {
                double actionScore = actionQ.get(action);
                if (actionScore > maxScore) {
                    bestActions.clear();
                    bestActions.add(action);
                    maxScore = actionScore;
                } else if (actionScore == maxScore) {
                    bestActions.add(action);
                }
            }

            this.nextAction =  bestActions.get(policy.getRandom().nextInt(bestActions.size()));
        } else if (policyType == PolicyType.Random) {
            this.nextAction = this.actionSpace.getRealization(policy.getRandom());
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
        this.findNextAction(endState, policy);

        double action_q = 0;
        if (this.qFunction.containsKey(endState) && this.qFunction.get(endState).containsKey(this.nextAction)) {
            action_q = this.qFunction.get(endState).get(this.nextAction);
        }

        double old_q = 0;

        if (this.qFunction.containsKey(startState) && this.qFunction.get(startState).containsKey(action)) {
            old_q = this.qFunction.get(startState).get(action);
        }

        double new_q = (1-this.learningRate) * old_q + (this.learningRate * (reward + ((1-this.discountRate) * action_q)));
        if (!this.qFunction.containsKey(startState)) {
            this.qFunction.put(startState, new HashMap<Object,Double>());
        }
        this.qFunction.get(startState).put(action, new_q);
    }
}
