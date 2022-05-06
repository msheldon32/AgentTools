package AgentTools.Function;

import java.util.HashMap;
import java.util.Map;

public class TabularV implements VFunction {
    protected Map<Object, Double> vMap;
    public TabularV() {
        this.vMap = new HashMap<Object, Double>();
    }


    public double getValue(Object state) {
        if (!this.vMap.containsKey(state)) {
            return 0;
        }

        return this.vMap.get(state);
    }

    public void updateValue(Object state, double newVal) {
        this.vMap.put(state, newVal);
    }
}
