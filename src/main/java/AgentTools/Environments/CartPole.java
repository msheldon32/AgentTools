package AgentTools.Environments;

import AgentTools.Util.*;
import org.apache.commons.math3.distribution.AbstractRealDistribution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CartPole extends Environment{
    protected ValueSpace stateSpace;
    protected ValueSpace actionSpace;
    protected Object currentState;

    public CartPole() {
        List<Integer> actionList = new ArrayList<Integer>();
        actionList.add(-1);
        actionList.add(1);

        this.actionSpace = new ListSpace(actionList);
        ValueSpace poleLocation = new IntegerSpace(0, 50);
        ValueSpace poleAngle = new FloatSpace(-90, 90);
        List<ValueSpace> spaceList = new ArrayList<ValueSpace>();
        spaceList.add(poleLocation);
        spaceList.add(poleAngle);
        this.stateSpace = new MultiSpace(spaceList);
        this.currentState = new ArrayList<Object>();
        ((List<Object>)this.currentState).add(25);
        ((List<Object>)this.currentState).add(0);
    }

    @Override
    public void reset() {
        this.currentState = new ArrayList<Object>();
        ((List<Object>)this.currentState).add(25);
        ((List<Object>)this.currentState).add(0);
    }

    @Override
    public ValueSpace getStateSpace() {
        return this.stateSpace;
    }

    @Override
    public ValueSpace getActionSpace() {
        return this.actionSpace;
    }

    @Override
    public Object getState() {
        return this.currentState;
    }


    @Override
    public double applyAction(Object action) {
        return 0.0
    }
}
