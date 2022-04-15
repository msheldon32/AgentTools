package AgentTools.Policies;

import java.util.HashMap;
import java.util.Random;

import AgentTools.Algorithms.RLAlgorithm;
import AgentTools.Util.Cdf;

public class Policy {
    public Policy() {

    }

    public Random getRandom() {
        return new Random();
    }

    public PolicyType getType(RLAlgorithm algorithm) {
        return PolicyType.Random;
    }

    public Object getAction(Object state) {
        return null;
    }

    public HashMap<Object, Double> actionProbabilities(Object state) {
        return new HashMap<Object, Double>();
    }

    public Cdf<Object> getCdf(Object state) {
        Cdf<Object> outCdf = new Cdf<Object>(this.getRandom());
        outCdf.fromHashMap(this.actionProbabilities(state));
        return outCdf;
    }
}