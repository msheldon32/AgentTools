package AgentTools.Algorithms;

import AgentTools.Function.TabularV;
import AgentTools.Function.VFunction;
import AgentTools.Policies.GreedyPolicy;
import AgentTools.Policies.Policy;
import AgentTools.Policies.PolicyType;
import AgentTools.Util.ValueSpace;

import java.util.HashMap;

public class TemporalDifferenceLambda extends RLAlgorithm {
    protected HashMap<Object, Double> eFunction;

    protected VFunction vFunction;

    protected Policy greedyPolicy;
    protected TraceMethod traceMethod;

    public TemporalDifferenceLambda(AlgoConfiguration algoConfiguration, VFunction vFunction, TraceMethod traceMethod) {
        super(algoConfiguration);
        this.vFunction = vFunction;
        this.eFunction = new HashMap<Object, Double>();

        this.greedyPolicy = new GreedyPolicy();
        this.traceMethod = traceMethod;
    }

    public TemporalDifferenceLambda(AlgoConfiguration algoConfiguration, TraceMethod traceMethod) {
        this(algoConfiguration, new TabularV(), traceMethod);
    }

    public TemporalDifferenceLambda(AlgoConfiguration algoConfiguration, VFunction vFunction) {
        this(algoConfiguration, vFunction, TraceMethod.Replacing);
    }

    public TemporalDifferenceLambda(AlgoConfiguration algoConfiguration) {
        this(algoConfiguration, new TabularV(), TraceMethod.Replacing);
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

        double start_v = this.vFunction.getValue(startState);

        double delta = reward + (this.algoConfiguration.discountRate*end_v) - start_v;

        double curE = 0;

        if (this.eFunction.containsKey(startState)) {
            curE = this.eFunction.get(startState);
        }

        switch (this.traceMethod) {
            case Accumulating:
                this.eFunction.put(startState, curE+1);
            case Dutch:
                this.eFunction.put(startState, (1-this.algoConfiguration.learningRate)*curE+1);
            case Replacing:
                this.eFunction.put(startState, 1.0);
        }

        for (Object s: this.eFunction.keySet()) {
            double prev_v = this.vFunction.getValue(s);

            double start_e = 0;
            if (this.eFunction.containsKey(s)) {
                start_e = this.eFunction.get(s);
            }

            double end_e = this.algoConfiguration.learningRate*this.algoConfiguration.discountRate*start_e;

            double new_v = prev_v + start_e*delta*this.algoConfiguration.learningRate;

            this.vFunction.updateValue(s, new_v);

            this.eFunction.put(s, end_e);
        }
    }
}
