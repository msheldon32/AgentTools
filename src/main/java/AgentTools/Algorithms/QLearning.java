package AgentTools.Algorithms;

import AgentTools.Policies.GreedyPolicy;
import AgentTools.Policies.Policy;
import AgentTools.Policies.PolicyType;
import AgentTools.Algorithms.RLAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QLearning extends RLAlgorithm {
    protected HashMap<Object, HashMap<Object, Double>> qFunction;
    double learningRate;
    double discountRate;
    Policy greedyPolicy;

    public QLearning(double discountRate, double learningRate) {
        super();
        this.qFunction = new HashMap<Object, HashMap<Object, Double>>();

        this.learningRate = learningRate;
        this.discountRate = discountRate;
        this.greedyPolicy = new GreedyPolicy();
    }

    public QLearning() {
        this(0.05, 0.05);
    }

    @Override
    public Object getAction(Object state, Policy policy) {
        if (policy.getType(this) == PolicyType.Optimal) {
            if (!this.qFunction.containsKey(state)) {
                return this.actionSpace.getRealization(policy.getRandom());
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

            return bestActions.get(policy.getRandom().nextInt(bestActions.size()));
        } else if (policy.getType(this) == PolicyType.Random) {
            return this.actionSpace.getRealization(policy.getRandom());
        } else if (policy.getType(this) == PolicyType.FixedStateAction) {
            return policy.getAction(state);
        } else if (policy.getType(this) == PolicyType.Probablistic) {
            return policy.getCdf(state).generate();
        }

        return null;
    }

    @Override
    public void reinforce(Object startState, Object endState, Object action, double reward) {
        Object idealAction = this.getAction(endState, this.greedyPolicy);
        double action_q = 0;
        if (this.qFunction.containsKey(endState) && this.qFunction.get(endState).containsKey(idealAction)) {
            action_q = this.qFunction.get(endState).get(idealAction);
        }

        double old_q = 0;

        if (this.qFunction.containsKey(startState) && this.qFunction.get(startState).containsKey(action)) {
            old_q = this.qFunction.get(startState).get(action);
        } else if (!this.qFunction.containsKey(startState)) {
            this.qFunction.put(startState, new HashMap<Object, Double>());
        }

        double new_q = (1-this.learningRate) * old_q + (this.learningRate * (reward + (this.discountRate * action_q)));
        this.qFunction.get(startState).put(action, new_q);
    }
}
