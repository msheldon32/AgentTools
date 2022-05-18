package AgentTools.Algorithms;

import AgentTools.Algorithms.Configuration.AlgoConfiguration;
import AgentTools.Algorithms.Configuration.MonteCarloTraceMethod;
import AgentTools.Function.TabularV;
import AgentTools.Function.VFunction;
import AgentTools.Policies.Policy;
import AgentTools.Policies.PolicyType;

import java.util.*;

public class MonteCarlo extends RLAlgorithm {
    protected Policy policy;
    protected MonteCarloTraceMethod traceMethod;

    protected VFunction vFunction;
    protected List<Object> actionList;
    protected List<Object> stateList;
    protected List<Double> rewardList;

    public MonteCarlo(AlgoConfiguration algoConfiguration, Policy policy, MonteCarloTraceMethod traceMethod) {
        super(algoConfiguration);
        this.traceMethod = traceMethod;

        this.vFunction = new TabularV();
        this.actionList = new ArrayList<Object>();
        this.stateList = new ArrayList<Object>();
        this.rewardList = new ArrayList<Double>();
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
        this.actionList.add(action);
        this.stateList.add(startState);
        this.rewardList.add(reward);
    }

    public VFunction evaluateFirstVisits() {
        assert (this.stateList.size() == this.rewardList.size());

        HashMap<Object, Integer> firstVisits = new HashMap<Object,Integer>();

        for (int i = 0; i < this.stateList.size(); i++) {
            if (firstVisits.containsKey(this.stateList.get(i))) {
                continue;
            }
            firstVisits.put(this.stateList.get(i), i);
        }

        double totalReward = 0;

        for (int i = this.stateList.size()-1; i >= 0; i--) {
            totalReward *= this.algoConfiguration.discountRate;
            totalReward += this.rewardList.get(i);

            Object state = this.stateList.get(i);

            if (firstVisits.get(state) != i) {
                continue;
            }

            this.vFunction.updateValue(state, totalReward);
        }

        return this.vFunction;
    }

    public VFunction evaluateAllVisits() {
        assert (this.stateList.size() == this.rewardList.size());

        Map<Object, Integer> digitCount = new HashMap<Object, Integer>();

        double totalReward = 0;

        for (int i = this.stateList.size()-1; i >= 0; i--) {
            totalReward *= this.algoConfiguration.discountRate;
            totalReward += this.rewardList.get(i);

            Object state = this.stateList.get(i);

            int nVisits = 0;
            if (digitCount.containsKey(state)) {
                nVisits = digitCount.get(state);
            }

            nVisits += 1;

            this.vFunction.updateValue(state, (this.vFunction.getValue(state)+totalReward)/((double) nVisits));

            digitCount.put(state, nVisits);
        }

        return this.vFunction;
    }

    public VFunction evaluate() {
        if (this.traceMethod == MonteCarloTraceMethod.EveryVisit) {
            return this.evaluateAllVisits();

        } else if (this.traceMethod == MonteCarloTraceMethod.FirstVisit) {
            return this.evaluateFirstVisits();
        }

        return null;
    }
}
