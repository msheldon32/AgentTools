package AgentTools.Policies;

import java.util.Random;
import AgentTools.Algorithms.RLAlgorithm;

public class EpsilonGreedyPolicy extends Policy {
    protected Random random;
    protected double epsilon;

    public EpsilonGreedyPolicy(Random random, double epsilon) {
        this.random = random;
        this.epsilon = epsilon;
    }

    @Override
    public Random getRandom() {
        return this.random;
    }

    @Override
    public PolicyType getType(RLAlgorithm algorithm) {
        if (random.nextFloat() <= this.epsilon) {
            return PolicyType.Random;
        } else {
            return PolicyType.Optimal;
        }
    }
}
