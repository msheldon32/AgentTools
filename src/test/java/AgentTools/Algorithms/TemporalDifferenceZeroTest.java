package AgentTools.Algorithms;

import AgentTools.Function.TabularV;
import AgentTools.Function.VFunction;
import AgentTools.Policies.Policy;
import AgentTools.Policies.StateActionPolicy;
import AgentTools.Util.IntegerSpace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class TemporalDifferenceZeroTest {
    private TemporalDifferenceZero tdZero;
    private Policy sameActionPolicy;

    private VFunction initialVFunction;

    private AlgoConfiguration algoConfiguration;

    private int nRuns;
    private double tolerance;

    private Random random;

    @BeforeEach
    void setUp() {
        HashMap<Object, Object> sameActionMap = new HashMap<Object, Object>();
        sameActionMap.put(1,1);
        sameActionMap.put(2,2);
        sameActionMap.put(3,3);

        this.initialVFunction = new TabularV();
        this.initialVFunction.updateValue(1,1.0);
        this.initialVFunction.updateValue(2,4.0);
        this.initialVFunction.updateValue(3,9.0);

        this.algoConfiguration = new AlgoConfiguration();
        this.algoConfiguration.lambda = 0.5;
        this.algoConfiguration.discountRate = 0.95;
        this.algoConfiguration.learningRate = 0.05;
        this.tdZero = new TemporalDifferenceZero(this.algoConfiguration, this.initialVFunction);

        this.sameActionPolicy = new StateActionPolicy(sameActionMap);
        this.tdZero.setPolicy(this.sameActionPolicy);

        this.nRuns = 1000000;
        this.tolerance = 0.05;

        this.random = new Random();
    }

    @Test
    void getAction() {
        // TD-Zero chooses the action according to the given policy
        assertEquals(1, this.tdZero.getAction(1, this.sameActionPolicy));
        assertEquals(2, this.tdZero.getAction(2, this.sameActionPolicy));
        assertEquals(3, this.tdZero.getAction(3, this.sameActionPolicy));
    }

    @Test
    void reinforce() {
        // update v-function accordingly

        // Assume each action i takes you to state i with probability 0.5, and other states with probability 0.25
        // Thus, with the sameActionPolicy we get:
        // v(1) = 0.95(0.5*v(1) + 0.25*v(2) + 0.25*v(3)) + 1 = 88.52
        // v(2) = 0.95(0.25*v(1) + 0.5*v(2) + 0.25*v(3)) + 4 = 92.45
        // v(3) = 0.95(0.25*v(1) + 0.25*v(2) + 0.5*v(3)) + 9 = 99.02
        int curState = 1;
        for (int i = 0; i < this.nRuns; i++) {
            int nextState = curState;
            double reward = curState*curState;

            if (this.random.nextDouble() > 0.5) {
                if (this.random.nextDouble() > 0.5) {
                    if (curState == 1) {
                        nextState = 3;
                    } else {
                        nextState = 1;
                    }
                } else {
                    if (curState == 2) {
                        nextState = 3;
                    } else {
                        nextState = 2;
                    }
                }
            }

            this.tdZero.reinforce(curState, nextState, curState, reward);
            curState = nextState;
        }


        double expv1 = 88.52;
        double expv2 = 92.45;
        double expv3 = 99.02;
        double newv1 = this.tdZero.vFunction.getValue(1);
        double newv2 = this.tdZero.vFunction.getValue(2);
        double newv3 = this.tdZero.vFunction.getValue(3);

        //System.out.format("v1: %f, v2: %f, v3: %f\n", newv1, newv2, newv3);

        assertTrue(newv1 > (expv1 * (1 - this.tolerance)));
        assertTrue(newv1 < (expv1 * (1 + this.tolerance)));
        assertTrue(newv2 > (expv2 * (1 - this.tolerance)));
        assertTrue(newv2 < (expv2 * (1 + this.tolerance)));
        assertTrue(newv3 > (expv3 * (1 - this.tolerance)));
        assertTrue(newv3 < (expv3 * (1 + this.tolerance)));
    }
}