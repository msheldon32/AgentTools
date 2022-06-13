package AgentTools.Function;

import AgentTools.Models.MDPModel;
import AgentTools.Policies.Policy;
import AgentTools.Util.IntegerSpace;

import java.util.Random;

public class ModelGenerator {
    protected int nActions;
    protected int nStates;

    protected MDPModel model;
    protected double minReward;
    protected double maxReward;

    protected Random random;

    public ModelGenerator(int nStates, int nActions) {
        this.nStates = nStates;
        this.nActions = nActions;


        this.random = new Random();

        this.model = new MDPModel(new IntegerSpace(0, nActions), new IntegerSpace(0, nStates), this.random);

        this.minReward = -10;
        this.maxReward = 10;
        this.generateTransitions();
        this.generateRewards();
    }

    public void generateTransitions() {
        for (int i = 0; i < this.nStates; i++) {
            for (int j = 0; j < this.nActions; j++) {
                double remProb = 1.0;

                for (int k = 0; k < this.nStates-1; k++) {
                    // this is biased towards lower states, but it's fine because this is just for testing.
                    double incProb = remProb*random.nextDouble();
                    this.model.addTransition(i,k,j,incProb);
                    remProb -= incProb;
                }

                this.model.addTransition(i, this.nStates-1, j,remProb);
            }
        }
    }

    public void generateRewards() {
        for (int i = 0; i < this.nStates; i++) {
            for (int j = 0; j < this.nActions; j++) {
                this.model.setReward(i,j, random.nextDouble()*(this.maxReward-this.minReward) + this.minReward);
            }
        }
    }

    public double[][] getBellmanMatrix(double discountRate, Policy policy) {
        return this.model.getBellmanMatrix(discountRate, policy);
    }

    public MDPModel getModel() {
        return this.model;
    }
}
