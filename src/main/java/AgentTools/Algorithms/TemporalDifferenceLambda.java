package AgentTools.Algorithms;

import AgentTools.Policies.GreedyPolicy;
import AgentTools.Policies.Policy;
import AgentTools.Policies.PolicyType;

import java.util.HashMap;

public class TemporalDifferenceLambda extends RLAlgorithm {
    protected HashMap<Object, Double> stateFunction;
    protected HashMap<Object, Double> eFunction;
    protected double learningRate;
    protected double discountRate;
    protected double lambda;
    protected Policy greedyPolicy;
    protected TraceMethod traceMethod;

    public TemporalDifferenceLambda(double lambda, double discountRate, double learningRate, TraceMethod traceMethod) {
        super();
        this.stateFunction = new HashMap<Object, Double>();
        this.eFunction = new HashMap<Object, Double>();

        this.learningRate = learningRate;
        this.discountRate = discountRate;
        this.lambda = lambda;
        this.greedyPolicy = new GreedyPolicy();
        this.traceMethod = traceMethod;
    }

    public TemporalDifferenceLambda() {
        this(0.05, 0.05, 0.05, TraceMethod.Replacing);
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

        double start_v = 0;

        if (this.stateFunction.containsKey(startState)) {
            start_v = this.stateFunction.get(startState);
        }

        double delta = reward + (this.discountRate*end_v) - start_v;

        switch (this.traceMethod) {
            case Accumulating:
                this.eFunction.put(startState, this.eFunction.get(startState)+1);
            case Dutch:
                this.eFunction.put(startState, (1-this.learningRate)*this.eFunction.get(startState)+1);
            case Replacing:
                this.eFunction.put(startState, 1.0);
        }

        for (Object s: this.stateFunction.keySet()) {
            double prev_v = this.stateFunction.get(s);
            this.stateFunction.put(s, prev_v + this.eFunction.get(s)*delta*this.learningRate);
            this.eFunction.put(s, this.lambda*this.discountRate*this.eFunction.get(s));
        }
    }
}
