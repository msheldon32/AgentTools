package AgentTools.Environments;

import java.util.HashMap;
import java.util.Random;

import AgentTools.Util.Cdf;
import AgentTools.Util.ValueSpace;

public class MDPEnvironment extends Environment {
    protected ValueSpace actionSpace;
    protected ValueSpace stateSpace;
    protected HashMap<Object, HashMap<Object, HashMap<Object, Double>>> transitionMatrix;
    protected HashMap<Object, HashMap<Object, Double>> rewardMatrix;
    protected Random random;
    protected Object state;

    public MDPEnvironment(ValueSpace actionSpace, ValueSpace stateSpace, Random random) {
        this.actionSpace = actionSpace;
        this.stateSpace = stateSpace;
        this.transitionMatrix = new HashMap<Object, HashMap<Object, HashMap<Object, Double>>>();
        this.rewardMatrix = new HashMap<Object, HashMap<Object, Double>>();
        this.random = random;
        this.state = null;
    }

    public void addTransition(Object startState, Object endState, Object action, double probability, double reward) {
        if (!this.transitionMatrix.containsKey(startState)) {
            this.transitionMatrix.put(startState, new HashMap<Object, HashMap<Object, Double>>());
            this.transitionMatrix.get(startState).put(action, new HashMap<Object, Double>());
        } else if (!this.transitionMatrix.get(startState).containsKey(action)) {
            this.transitionMatrix.get(startState).put(action, new HashMap<Object, Double>());
        }
        this.transitionMatrix.get(startState).get(action).put(endState, probability);

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
}
