package AgentTools.Environments;

import AgentTools.Util.IntegerSpace;
import AgentTools.Util.SingleSpace;
import AgentTools.Util.ValueSpace;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.distribution.AbstractRealDistribution;

public class KArmBandit extends Environment {
    protected ValueSpace actionSpace;

    protected Random random;
    protected Object state;

    protected List<AbstractRealDistribution> arms;

    protected int nArms;

    public KArmBandit(int nArms, Random random) {
        this.actionSpace = new IntegerSpace(0, nArms);

        this.random = random;
        this.state = 0;
        this.nArms = nArms;

        arms = new ArrayList<AbstractRealDistribution>();
        for (int i = 0; i < this.nArms; i++) {
            this.arms.add(null);
        }
    }

    public KArmBandit(int nArms) {
        this(nArms, new Random());
    }

    public void setArm(int armNo, AbstractRealDistribution dist) {
        this.arms.set(armNo, dist);
    }

    @Override
    public ValueSpace getStateSpace() {
        return new SingleSpace();
    }

    @Override
    public ValueSpace getActionSpace() {
        return this.actionSpace;
    }

    @Override
    public Object getState() {
        return this.state;
    }


    @Override
    public double applyAction(Object action) {
        return this.arms.get((Integer)action).sample();
    }
}
