package AgentTools.Algorithms;

import AgentTools.Policies.Policy;
import AgentTools.Algorithms.RLAlgorithm;

import java.util.Random;

public class RandomAlgorithm extends RLAlgorithm {
    protected Random random;
    public RandomAlgorithm(Random random) {
        super();
        this.random = random;
    }
    public RandomAlgorithm()
    {
        this(new Random());
        this.random = random;
    }

    public Object getAction(Object state, Policy policy) {
        return this.actionSpace.getRealization(this.random);
    }
}