package AgentTools.Policies;
import AgentTools.Algorithms.RLAlgorithm;

public class GreedyPolicy extends Policy {
    public GreedyPolicy() {

    }

    @Override
    public PolicyType getType(RLAlgorithm algorithm) {
        return PolicyType.Optimal;
    }
}
