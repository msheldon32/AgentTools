package AgentTools.Function;

import java.util.HashMap;
import java.util.Map;

public class TabularQ implements QFunction {
    protected Map<Object, HashMap<Object, Double>> qMap;

    public TabularQ() {
        this.qMap = new HashMap<Object, HashMap<Object, Double>>();
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
}
