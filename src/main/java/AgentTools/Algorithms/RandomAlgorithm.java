package AgentTools.Algorithms;

import AgentTools.Algorithms.Configuration.AlgoConfiguration;
import AgentTools.Policies.Policy;

public class RandomAlgorithm extends RLAlgorithm {

    public RandomAlgorithm(AlgoConfiguration algoConfiguration) {
        super(algoConfiguration);
    }

    public Object getAction(Object state, Policy policy) {
        return this.algoConfiguration.actionSpace.getRealization(this.algoConfiguration.random);
    }
}