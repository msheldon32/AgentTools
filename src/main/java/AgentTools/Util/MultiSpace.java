package AgentTools.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MultiSpace extends ValueSpace<List> {
    protected List<ValueSpace> spaces;

    public MultiSpace(List<ValueSpace> spaces) {
        this.spaces = spaces;
    }

    @Override
    public List getRealization(Random random) {
        List<Object> outList = new ArrayList<Object>();
        for (ValueSpace vs : this.spaces) {
            outList.add(vs.getRealization(random));
        }
        return outList;
    }
}
