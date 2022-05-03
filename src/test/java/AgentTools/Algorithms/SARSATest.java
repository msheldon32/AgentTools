package AgentTools.Algorithms;

import AgentTools.Policies.EpsilonGreedyPolicy;
import AgentTools.Policies.GreedyPolicy;
import AgentTools.Policies.Policy;
import AgentTools.Util.IntegerSpace;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SARSATest {
    private SARSA otherSarsa;
    private SARSA withSarsa;

    private int nRuns;
    private double tolerance;

    private Policy greedyPolicy;
    private Policy epsGreedyPolicy;

    private IntegerSpace twoSpace;
    private IntegerSpace threeSpace;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {

        this.greedyPolicy = new GreedyPolicy();
        this.epsGreedyPolicy = new EpsilonGreedyPolicy(new Random(), 0.05);

        this.otherSarsa = new SARSA(this.epsGreedyPolicy, 0.95, 0.05);
        this.otherSarsa.qFunction = new HashMap<Object, HashMap<Object, Double>>();
        this.otherSarsa.qFunction.put(1, new HashMap<Object, Double>());
        this.otherSarsa.qFunction.put(2, new HashMap<Object, Double>());
        this.otherSarsa.qFunction.get(1).put(1, 1.0);
        this.otherSarsa.qFunction.get(2).put(1, 3.0);

        this.withSarsa = new SARSA(this.epsGreedyPolicy, 0.95, 0.05);
        this.withSarsa.qFunction = new HashMap<Object, HashMap<Object, Double>>();
        HashMap<Object, Double> state1_Map = new HashMap<Object, Double>();
        state1_Map.put(1, 1.0);
        state1_Map.put(2, 3.0);
        state1_Map.put(3, 2.0);
        this.withSarsa.qFunction.put(1, state1_Map);
        this.nRuns = 5000;
        this.tolerance = 0.05;

        this.threeSpace = new IntegerSpace(1, 4);
        this.twoSpace   = new IntegerSpace(1, 3);

        this.withSarsa.setActionSpace(this.threeSpace);
        this.otherSarsa.setActionSpace(this.twoSpace);
    }

    @Test
    void getAction() {
        // getting an action with a greedy policy returns the correct action each time


        for (int i = 0; i < this.nRuns; i++) {
            assertEquals(2, this.withSarsa.getAction(1, greedyPolicy));
        }

        // getting an action with an epsilon-greedy policy returns the correct action (1-eps) + (eps*(1/n_states)) percentage of the time
        // incorrect actions happen eps*((1-n_states)/n_states) percentage of the time
        double epsilon = 0.2;
        Policy epsGreedyPolicy = new EpsilonGreedyPolicy(new Random(), epsilon);
        int nMisses = 0;
        double expMisses = epsilon * (2.0 / 3.0);

        for (int i = 0; i < this.nRuns; i++) {
            Integer action = (Integer) this.withSarsa.getAction(1, epsGreedyPolicy);
            if (action != 2) {
                nMisses += 1;
            }
        }

        double missRate = (double) nMisses / (double) nRuns;

        assertTrue(expMisses - this.tolerance < missRate);
        assertTrue(expMisses + this.tolerance > missRate);
    }

    @Test
    void reinforce() {
        // reinforcing correctly improves the Q function

        // transition 1->3 with reward of -1

        // therefore, the q function for action 1 should equal 0.05*(-1 + 0.95*q(2)) + 0.95*1
        this.withSarsa.reinforce(1, 1, 1, -1);
        assertEquals(1.0425, this.withSarsa.qFunction.get(1).get(1));

        // with enough iterations, this should roughly converge, from below, to 0.95*q(2) - 1
        for (int i = 0; i < this.nRuns; i++) {
            this.withSarsa.reinforce(1, 1, 1, -1);
        }

        double newQ = this.withSarsa.qFunction.get(1).get(1);

        double expQ = (0.95 * 3) - 1;


        assertTrue(newQ > (expQ * (1 - this.tolerance)));


        // now consider this.otherQ
        // state 2 rewards 3 and immediately returns to state 1
        // state 1 rewards -1 and then goes to state 2
        // then the Q function should approximate:
        //     q(1,1) = (0.95*q(2,1))*0.975 - 1 = 12.52
        //     q(2,1) = (0.95*q(1,1))*0.975 + 3 = 14.60
        for (int i = 0; i < this.nRuns; i++) {
            this.otherSarsa.reinforce(1, 2, 1, -1);
            this.otherSarsa.reinforce(2, 1, 1, 3);
        }

        double expQ1 = 12.52;
        double expQ2 = 14.60;
        double newQ1 = this.otherSarsa.qFunction.get(1).get(1);
        double newQ2 = this.otherSarsa.qFunction.get(2).get(1);
        assertTrue(newQ1 > (expQ1 * (1 - this.tolerance)));
        assertTrue(newQ1 < (expQ1 * (1 + this.tolerance)));
        assertTrue(newQ2 > (expQ2 * (1 - this.tolerance)));
        assertTrue(newQ2 < (expQ2 * (1 + this.tolerance)));
    }

    @Test
    void printQ() {
    }
}