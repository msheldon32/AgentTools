package AgentTools.Function;

import AgentTools.Util.ValueSpace;

import java.util.*;

public class TabularQ implements QFunction {
    protected Map<Object, HashMap<Object, Double>> qMap;
    protected ValueSpace actionSpace;
    protected Random random;

    public TabularQ(ValueSpace actionSpace, Random random) {
        this.qMap = new HashMap<Object, HashMap<Object, Double>>();
        this.actionSpace = actionSpace;
        this.random = random;
    }

    public double getValue(Object state, Object action) {
        if (!qMap.containsKey(state)) {
            return 0;
        }

        if (!qMap.get(state).containsKey(action)) {
            return 0;
        }

        return qMap.get(state).get(action);
    }

    public void updateValue(Object state, Object action, double newVal) {
        if (!qMap.containsKey(state)) {
            this.qMap.put(state, new HashMap<Object, Double>());
        }

        this.qMap.get(state).put(action, newVal);
    }

    public Object getMaxAction(Object state) {
        if (!this.qMap.containsKey(state)) {
            return this.actionSpace.getRealization(this.random);
        }
        HashMap<Object, Double> actionQ = this.qMap.get(state);
        List<Object> bestActions = new ArrayList<Object>();

        double maxScore = Double.NEGATIVE_INFINITY;
        for (Object action : actionQ.keySet()) {
            double actionScore = actionQ.get(action);
            if (actionScore > maxScore) {
                bestActions.clear();
                bestActions.add(action);
                maxScore = actionScore;
            } else if (actionScore == maxScore) {
                bestActions.add(action);
            }
        }

        return bestActions.get(this.random.nextInt(bestActions.size()));
    }
}
