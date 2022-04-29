package AgentTools.Models;

import AgentTools.Algorithms.PolicyIteration;
import AgentTools.Policies.Policy;
import AgentTools.Util.Cdf;
import AgentTools.Util.FloatSpace;
import AgentTools.Util.ValueSpace;

import java.io.ObjectStreamClass;
import java.util.*;

public class MDPModel extends Model {
    protected ValueSpace actionSpace;
    protected ValueSpace stateSpace;
    protected HashMap<Object, HashMap<Object, HashMap<Object, Double>>> transitionMatrix;
    protected HashMap<Object, HashMap<Object, Double>> rewardMatrix;
    protected Random random;
    protected Object state;

    protected int nStates;

    public MDPModel(ValueSpace actionSpace, ValueSpace stateSpace, Random random) {
        if (stateSpace instanceof FloatSpace) {
            throw new RuntimeException("Invalid State Space!");
        }

        this.actionSpace = actionSpace;
        this.stateSpace = stateSpace;
        this.transitionMatrix = new HashMap<Object, HashMap<Object, HashMap<Object, Double>>>();
        this.rewardMatrix = new HashMap<Object, HashMap<Object, Double>>();
        this.random = random;
        this.state = null;
        this.nStates = this.actionSpace.getSize();
    }

    public void addTransition(Object startState, Object endState, Object action, double probability) {
        if (!this.transitionMatrix.containsKey(startState)) {
            this.transitionMatrix.put(startState, new HashMap<Object, HashMap<Object, Double>>());
            this.transitionMatrix.get(startState).put(action, new HashMap<Object, Double>());
        } else if (!this.transitionMatrix.get(startState).containsKey(action)) {
            this.transitionMatrix.get(startState).put(action, new HashMap<Object, Double>());
        }
        this.transitionMatrix.get(startState).get(action).put(endState, probability);
    }

    public void setReward(Object startState, Object action, double reward) {
        if(!this.rewardMatrix.containsKey(startState)) {
            this.rewardMatrix.put(startState, new HashMap<Object, Double>());
        }
        this.rewardMatrix.get(startState).put(action, reward);
    }

    @Override
    public double applyAction(Object action) {
        double reward = 0;
        if (this.rewardMatrix.containsKey(this.state) && this.rewardMatrix.get(this.state).containsKey(action)) {
            reward = this.rewardMatrix.get(this.state).get(action);
        }

        //System.out.println(this.state);

        HashMap<Object, Double> transitions = this.transitionMatrix.get(this.state).get(action);
        Cdf<Object> stateCdf = new Cdf<Object>(this.random);
        stateCdf.fromHashMap(transitions);
        Object start_state = this.state;
        this.state = stateCdf.generate();
        if (this.state == null) {
            System.out.println("Transitioning to null!");
            System.out.println(start_state);
            System.out.println(action);
        }
        return reward;
    }

    public void setState(Object state) {
        this.state = state;
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
        return this.state;
    }

    public double[][] getBellmanMatrix(double discountRate, Policy policy) {
        double[][] outMatrix = new double[this.nStates+1][this.nStates+2];

        // array format:
        //   [1, 0,0,0,1] // ensures reward equals 1
        //   [REWARD(s1,a), V(s1), V(s2)...,0]
        int colIter = 0;
        int rowIter = 0;
        outMatrix[rowIter][colIter] = 1;
        colIter += 1;

        for (int i = 0; i < nStates; i++) {
            outMatrix[rowIter][colIter] = 0;
            colIter += 1;
        }
        outMatrix[rowIter][colIter] = 1;
        rowIter += 1;
        colIter = 0;

        for (Iterator it = this.stateSpace.iterator(); it.hasNext(); ) {
            Object state = it.next();
            Object action = policy.getAction(state);

            double reward = 0;
            if (this.rewardMatrix.containsKey(state)) {
                if (this.rewardMatrix.get(state).containsKey(action)) {
                    reward = this.rewardMatrix.get(state).get(action);
                }
            }

            outMatrix[rowIter][colIter] = reward;
            colIter += 1;

            for (Iterator next_it = this.stateSpace.iterator(); next_it.hasNext(); ) {
                Object nextState = next_it.next();
                double transitionProb = 0.0;

                if (this.transitionMatrix.containsKey(state)) {
                    if (this.transitionMatrix.get(state).containsKey(action)) {
                        if (this.transitionMatrix.get(state).get(action).containsKey(nextState)) {
                            transitionProb = this.transitionMatrix.get(state).get(action).get(nextState);
                        }
                    }
                }

                double discountedTransition = discountRate*transitionProb;

                if (it.equals(next_it)) {
                    discountedTransition -= 1;
                }
                outMatrix[rowIter][colIter] = discountedTransition;
                colIter += 1;
            }

            outMatrix[rowIter][colIter] = 0;
            colIter = 0;
            rowIter += 1;
        }
        return outMatrix;
    }
}
