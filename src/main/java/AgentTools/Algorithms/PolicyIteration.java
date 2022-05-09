package AgentTools.Algorithms;

import AgentTools.Models.MDPModel;
import AgentTools.Policies.Policy;
import AgentTools.Policies.StateActionPolicy;
import AgentTools.Util.ValueSpace;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.apache.commons.math3.linear.*;

public class PolicyIteration extends RLAlgorithm  {
    protected MDPModel model;
    protected StateActionPolicy policy;
    protected ValueSpace stateSpace;
    protected ValueSpace actionSpace;
    protected Random random;
    protected HashMap<Object, Double> vFunction;
    protected double discountRate;

    public PolicyIteration(MDPModel model, double discountRate, int nSteps, Random random) {
        super(model.getStateSpace(), model.getActionSpace());
        this.model = model;
        this.policy = new StateActionPolicy();

        this.stateSpace = model.getStateSpace();
        this.actionSpace = model.getActionSpace();
        this.random = random;
        this.discountRate = discountRate;
        this.randomizePolicy();
    }

    protected PolicyIteration(MDPModel model, double discountRate, int nSteps) {
        this(model, discountRate, nSteps, new Random());
    }

    private void randomizePolicy() {
        for (Iterator it = this.stateSpace.iterator(); it.hasNext(); ) {
            Object state = it.next();

            this.policy.setStateAction(state, this.actionSpace.getRealization(this.random));
        }
    }

    private void policyEvaluation() {
        RealMatrix bellmanMatrix = MatrixUtils.createRealMatrix(this.model.getBellmanMatrix(discountRate, this.policy));
    }
}
