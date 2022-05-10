package AgentTools.Algorithms;

import AgentTools.Policies.Policy;
import AgentTools.Algorithms.RLAlgorithm;
import AgentTools.Util.ValueSpace;

import java.util.Random;

public class RandomAlgorithm extends RLAlgorithm {

    public RandomAlgorithm(AlgoConfiguration algoConfiguration) {
        super(algoConfiguration);
    }

    public Object getAction(Object state, Policy policy) {
        return this.algoConfiguration.actionSpace.getRealization(this.algoConfiguration.random);
    }
}