package AgentTools.Util;

import java.util.HashMap;

public class QFunction {
    protected HashMap<Object, HashMap<Object, Double>> qMap;

    public QFunction() {
        this.qMap = new HashMap<Object, HashMap<Object, Double>>();
    }

    public double get(Object state, Object action) {
        return this.qMap.get(state).get(action);
    }
}
