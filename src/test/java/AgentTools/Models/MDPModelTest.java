package AgentTools.Models;

import AgentTools.Policies.Policy;
import AgentTools.Policies.StateActionPolicy;
import AgentTools.Util.IntegerSpace;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MDPModelTest {
    private MDPModel deterministicMDP;
    private MDPModel probablisticMDP;
    private MDPModel emptyMDP;

    private int nRuns;
    private double tolerance;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        this.deterministicMDP = new MDPModel(new IntegerSpace(1,4), new IntegerSpace(1,4),new Random());
        this.probablisticMDP = new MDPModel(new IntegerSpace(1,4), new IntegerSpace(1,4),new Random());
        this.emptyMDP = new MDPModel(new IntegerSpace(1,4), new IntegerSpace(1,4),new Random());

        // deterministic MDP:
        //    the next state, and reward is equal to the action
        // probablistic MDP:
        //    the next state is [action] at 50%
        //    the next state is the other two at 25% each
        for (int startState = 1; startState <= 3; startState++) {
            this.deterministicMDP.addTransition(startState,1,1,1.0);
            this.deterministicMDP.addTransition(startState,2,2,1.0);
            this.deterministicMDP.addTransition(startState,3,3,1.0);

            for (int action = 1; action <= 3; action++) {
                this.deterministicMDP.setReward(startState, action, action);
                this.probablisticMDP.setReward(startState, action, action);
                this.probablisticMDP.addTransition(startState,action, action,0.5);
                for (int endState = 1; endState <= 3; endState++) {
                    if (endState == action) {
                        continue;
                    }
                    this.probablisticMDP.addTransition(startState,endState, action,0.25);
                }
            }
        }

        this.nRuns = 1000;
        this.tolerance = 0.05;
    }

    @org.junit.jupiter.api.Test
    void addTransition() {
        // adding transition to MDP works
        this.emptyMDP.addTransition(1,2,1,1.0);
        this.emptyMDP.setState(1);
        this.emptyMDP.applyAction(1);
        assertEquals(2,this.emptyMDP.getState());
    }

    @org.junit.jupiter.api.Test
    void applyAction() {
        // deterministic MDP changes state correctly
        for (int startState = 1; startState <= 3; startState++) {
            for (int action = 1; action <=3; action++) {
                this.deterministicMDP.setState(startState);
                double reward = this.deterministicMDP.applyAction(action);
                assertEquals(action, this.deterministicMDP.getState());
                assertEquals(action, reward);
            }
        }

        // random MDP has representative state changes
        for (int startState = 1; startState <= 3; startState++) {
            for (int action = 1; action <=3; action++) {
                int[] stateCounts = new int[3];
                double totalReward = 0;
                for (int i = 0; i < this.nRuns; i++) {
                    this.probablisticMDP.setState(startState);
                    totalReward += this.probablisticMDP.applyAction(action);
                    int endState = (Integer) this.probablisticMDP.getState();
                    stateCounts[endState-1]++;
                }
                assertEquals(action*this.nRuns,totalReward);
                for (int endState = 1; endState <=3; endState++) {
                    if (endState == action) {
                        //System.out.format("Expecting 50%, received count of: %d\n", )
                        assertTrue(stateCounts[endState-1] >= (0.5-this.tolerance)*this.nRuns);
                        assertTrue(stateCounts[endState-1] <= (0.5+this.tolerance)*this.nRuns);
                    } else {
                        assertTrue(stateCounts[endState-1] >= (0.25-this.tolerance)*this.nRuns);
                        assertTrue(stateCounts[endState-1] <= (0.25+this.tolerance)*this.nRuns);
                    }
                }
            }
        }

    }

    @org.junit.jupiter.api.Test
    void setState() {
        // setting MDP state works
        for (int state = 1; state <=3; state++) {
            this.deterministicMDP.setState(state);
            assertEquals(state,this.deterministicMDP.getState());
        }
    }

    @org.junit.jupiter.api.Test
    void getStateSpace() {
        // state space for all member MDP's is correct
        assertTrue(this.deterministicMDP.getStateSpace() instanceof IntegerSpace);
    }

    @org.junit.jupiter.api.Test
    void getActionSpace() {
        // action space for all member MDP's is correct
        assertTrue(this.deterministicMDP.getActionSpace() instanceof IntegerSpace);
    }

    @org.junit.jupiter.api.Test
    void getState() {
        // get state returns correct value after setState()
        for (int state = 1; state <=3; state++) {
            this.deterministicMDP.setState(state);
            assertEquals(state, this.deterministicMDP.getState());
        }
    }

    @org.junit.jupiter.api.Test
    void getBellmanMatrix() {
        double discountRate = 0.9;

        HashMap<Object, Object> sameActionMap = new HashMap<Object, Object>();
        for (int state=1; state <= 3; state++) {
            sameActionMap.put(state, state);
        }
        Policy sameActionPolicy = new StateActionPolicy(sameActionMap);

        // bellman matrix is returned correctly
        double[][] deterministicBellman = this.deterministicMDP.getBellmanMatrix(discountRate,sameActionPolicy);
        double[][] probablisticBellman = this.probablisticMDP.getBellmanMatrix(discountRate,sameActionPolicy);

        // assert the reward variable is fixed at 1
        assertEquals(1,deterministicBellman[0][0]);
        assertEquals(1,probablisticBellman[0][0]);
        for (int i = 1; i < 4; i++) {
            assertEquals(0,deterministicBellman[0][i]);
            assertEquals(0,probablisticBellman[0][i]);
        }
        assertEquals(1,deterministicBellman[0][4]);
        assertEquals(1,probablisticBellman[0][4]);
        //System.out.println(Arrays.deepToString(deterministicBellman));
        //System.out.println(Arrays.deepToString(probablisticBellman));
        for (int row = 1; row < 4; row++) {
            // the reward should equal the action
            assertEquals(deterministicBellman[row][0], row);
            assertEquals(probablisticBellman[row][0], row);

            for (int col = 1; col < 4; col++) {
                if (row == col) {
                    assertEquals(discountRate-1,deterministicBellman[row][col]);
                    assertEquals((0.5*discountRate)-1, probablisticBellman[row][col]);
                } else {
                    // with this policy, the chance of going to any state to another state is 0
                    assertEquals(0,deterministicBellman[row][col]);
                    assertEquals(0.25*discountRate,probablisticBellman[row][col]);
                }
                // the equations here sum to equality
                assertEquals(0,deterministicBellman[row][4]);
                assertEquals(0,probablisticBellman[row][4]);
            }
        }
    }
}