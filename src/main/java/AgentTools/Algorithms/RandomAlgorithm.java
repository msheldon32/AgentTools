package AgentTools.Algorithms;

import AgentTools.Policies.Policy;
import AgentTools.Algorithms.RLAlgorithm;
import AgentTools.Util.ValueSpace;

import java.util.Random;

public class RandomAlgorithm extends RLAlgorithm {
    protected Random random;
    public RandomAlgorithm(ValueSpace stateSpace, ValueSpace actionSpace, Random random) {
        super(stateSpace, actionSpace);
        this.random = random;
    }
    public RandomAlgorithm(ValueSpace stateSpace, ValueSpace actionSpace)
    {
        this(stateSpace, actionSpace, new Random());
        this.random = random;
    }

    public Object getAction(Object state, Policy policy) {
        return this.actionSpace.getRealization(this.random);
    }
}