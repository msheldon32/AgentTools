package AgentTools.Algorithms;

import AgentTools.Algorithms.Configuration.AlgoConfiguration;
import AgentTools.Models.MDPModel;
import AgentTools.Policies.StateActionPolicy;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.math3.linear.*;

public class PolicyIteration extends RLAlgorithm  {
    protected MDPModel model;
    protected StateActionPolicy policy;


    protected HashMap<Object, Double> vFunction;
    protected double discountRate;

    public PolicyIteration(MDPModel model, AlgoConfiguration algoConfiguration) {
        super(algoConfiguration);
        this.model = model;
        this.policy = new StateActionPolicy();

        this.randomizePolicy();

        assert algoConfiguration.actionSpace == model.getActionSpace();
        assert algoConfiguration.stateSpace == model.getStateSpace();
    }

    private void randomizePolicy() {
        for (Iterator it = this.algoConfiguration.stateSpace.iterator(); it.hasNext(); ) {
            Object state = it.next();

            this.policy.setStateAction(state, this.algoConfiguration.actionSpace.getRealization(this.algoConfiguration.random));
        }
    }

    private void policyEvaluation() {
        RealMatrix bellmanMatrix = MatrixUtils.createRealMatrix(this.model.getBellmanMatrix(discountRate, this.policy));
    }
}
