package AgentTools.Algorithms;

import AgentTools.Algorithms.Configuration.AlgoConfiguration;
import AgentTools.Function.TabularQ;
import AgentTools.Policies.EpsilonGreedyPolicy;
import AgentTools.Policies.GreedyPolicy;
import AgentTools.Policies.Policy;
import AgentTools.Util.IntegerSpace;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class QLearningTest {
    private QLearning otherQ;
    private QLearning withQ;

    private int nRuns;
    private double tolerance;

    private AlgoConfiguration algoConfiguration;

    private IntegerSpace threeSpace;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        this.threeSpace = new IntegerSpace(1,4);

        this.algoConfiguration = new AlgoConfiguration();

        this.algoConfiguration.discountRate = 0.95;
        this.algoConfiguration.learningRate = 0.05;
        this.algoConfiguration.actionSpace = threeSpace;
        this.algoConfiguration.random = new Random();

        TabularQ firstQ = new TabularQ(algoConfiguration.actionSpace, algoConfiguration.random);
        firstQ.updateValue(1,1,1.0);
        firstQ.updateValue(2,1,3.0);
        this.otherQ = new QLearning(this.algoConfiguration, firstQ);


        TabularQ secondQ = new TabularQ(algoConfiguration.actionSpace, algoConfiguration.random);
        secondQ.updateValue(1,1,1.0);
        secondQ.updateValue(1,2,3.0);
        secondQ.updateValue(1,3,2.0);

        this.withQ = new QLearning(algoConfiguration, secondQ);

        this.nRuns = 5000;
        this.tolerance = 0.05;
    }

    @Test
    void getAction() {
        // getting an action with a greedy policy returns the correct action each time
        Policy greedyPolicy = new GreedyPolicy();

        for (int i = 0; i < this.nRuns; i++) {
            assertEquals(2, this.withQ.getAction(1,greedyPolicy));
        }

        // getting an action with an epsilon-greedy policy returns the correct action (1-eps) + (eps*(1/n_states)) percentage of the time
        // incorrect actions happen eps*((1-n_states)/n_states) percetnage of the time
        double epsilon = 0.2;
        Policy epsGreedyPolicy = new EpsilonGreedyPolicy(new Random(), epsilon);
        int nMisses = 0;
        double expMisses = epsilon*(2.0/3.0);

        for (int i = 0; i < this.nRuns; i++) {
            Integer action = (Integer) this.withQ.getAction(1,epsGreedyPolicy);
            if (action != 2) {
                nMisses += 1;
            }
        }

        double missRate = (double)nMisses/(double)nRuns;

        assertTrue(expMisses-this.tolerance < missRate);
        assertTrue(expMisses+this.tolerance > missRate);
    }

    @Test
    void reinforce() {
        // reinforcing correctly improves the Q function

        // transition 1->3 with reward of -1

        // therefore, the q function for action 1 should equal 0.05*(-1 + 0.95*q(2)) + 0.95*1
        this.withQ.reinforce(1,1,1,-1);
        assertEquals(1.0425,this.withQ.qFunction.getValue(1,1));

        // with enough iterations, this should roughly converge, from below, to 0.95*q(2) - 1
        for (int i = 0; i < this.nRuns; i++) {
            this.withQ.reinforce(1,1,1,-1);
        }

        double newQ = this.withQ.qFunction.getValue(1,1);
        //double newQ = this.withQ.qFunction.get(1).get(1);

        double expQ = (0.95*3)-1;


        assertTrue(newQ > (expQ*(1-this.tolerance)));


        // now consider this.otherQ
        // state 2 rewards 3 and immediately returns to state 1
        // state 1 rewards -1 and then goes to state 2
        // then the Q function should approximate:
        //     q(1) = 0.95*q(2) - 1 = 18.97
        //     q(2) = 0.95*q(1) + 3 = 21.02
        for (int i = 0; i < this.nRuns; i++) {
            this.otherQ.reinforce(1,2,1,-1);
            this.otherQ.reinforce(2,1,1,3);
        }

        double expQ1 = 18.97;
        double expQ2 = 21.02;
        double newQ1 = this.otherQ.qFunction.getValue(1,1);
        double newQ2 = this.otherQ.qFunction.getValue(2,1);
        //double newQ1 = this.otherQ.qFunction.get(1).get(1);
        //double newQ2 = this.otherQ.qFunction.get(2).get(1);
        assertTrue(newQ1 > (expQ1*(1-this.tolerance)));
        assertTrue(newQ1 < (expQ1*(1+this.tolerance)));
        assertTrue(newQ2 > (expQ2*(1-this.tolerance)));
        assertTrue(newQ2 < (expQ2*(1+this.tolerance)));
    }

    @Test
    void printQ() {
    }
}