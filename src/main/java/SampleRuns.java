import AgentTools.Agent;
import AgentTools.Algorithms.*;
import AgentTools.Environments.KArmBandit;
import AgentTools.Policies.EpsilonGreedyPolicy;
import AgentTools.Policies.Policy;
import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;

import java.util.Random;

public class SampleRuns {
    public static void main(String[] args) {
        KArmBandit model = new KArmBandit(10);

        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            AbstractRealDistribution dist = new UniformRealDistribution(0, random.nextInt(19)+1);
            model.setArm(i, dist);
        }

        Policy eGreedy = new EpsilonGreedyPolicy(random, 0.05);
        Agent randAgent = new Agent(model, new RandomAlgorithm(), eGreedy, model.getStateSpace(), model.getActionSpace());
        Agent qAgent = new Agent(model, new QLearning(), eGreedy, model.getStateSpace(), model.getActionSpace());
        Agent sarsaAgent = new Agent(model, new SARSA(eGreedy), eGreedy, model.getStateSpace(), model.getActionSpace());
        Agent tdlAgent = new Agent(model, new TemporalDifferenceLambda(), eGreedy, model.getStateSpace(), model.getActionSpace());
        Agent tdzAgent = new Agent(model, new TemporalDifferenceZero(), eGreedy, model.getStateSpace(), model.getActionSpace());

        int num_episodes = 100;
        int num_pulls = 100;

        for (int episode_no = 0; episode_no < num_episodes; episode_no++) {
            for (int pull_no = 0; pull_no < num_pulls; pull_no++) {
                randAgent.setState(model.getState());
                double reward = model.applyAction(randAgent.takeAction());
                randAgent.reinforce(reward);
            }

            model.reset();

            for (int pull_no = 0; pull_no < num_pulls; pull_no++) {
                qAgent.setState(model.getState());
                double reward = model.applyAction(qAgent.takeAction());
                qAgent.reinforce(reward);
            }

            model.reset();

            for (int pull_no = 0; pull_no < num_pulls; pull_no++) {
                sarsaAgent.setState(model.getState());
                double reward = model.applyAction(sarsaAgent.takeAction());
                sarsaAgent.reinforce(reward);
            }

            model.reset();

            /*for (int pull_no = 0; pull_no < num_pulls; pull_no++) {
                tdlAgent.setState(model.getState());
                double reward = model.applyAction(tdlAgent.takeAction());
                tdlAgent.reinforce(reward);
            }

            model.reset();

            for (int pull_no = 0; pull_no < num_pulls; pull_no++) {
                tdzAgent.setState(model.getState());
                double reward = model.applyAction(tdzAgent.takeAction());
                tdzAgent.reinforce(reward);
            }*/
        }

        System.out.format("Total reward (Random): %f\n", randAgent.getTotalReward());
        System.out.format("Total reward (Q Learning): %f\n", qAgent.getTotalReward());
        System.out.format("Total reward (SARSA): %f\n", sarsaAgent.getTotalReward());
        System.out.format("Total reward (TD Lambda): %f\n", tdlAgent.getTotalReward());
        System.out.format("Total reward (TD Zero): %f\n", tdzAgent.getTotalReward());
    }
}
