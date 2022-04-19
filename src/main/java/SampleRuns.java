import AgentTools.Agent;
import AgentTools.Algorithms.*;
import AgentTools.Environments.KArmBandit;
import AgentTools.Environments.MDPEnvironment;
import AgentTools.Policies.EpsilonGreedyPolicy;
import AgentTools.Policies.Policy;
import AgentTools.Util.IntegerSpace;
import AgentTools.Util.ValueSpace;
import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;

import java.util.Random;

public class SampleRuns {
    public static void main(String[] args) {
        mdpMain();
    }

    public static void mdpMain() {
        ValueSpace stateSpace = new IntegerSpace(0,3);
        ValueSpace actionSpace = new IntegerSpace(0,3);
        MDPEnvironment environment = new MDPEnvironment(actionSpace,stateSpace,new Random());

        environment.addTransition(0,0,0,0.8,0);
        environment.addTransition(0,1,0,0.1,1);
        environment.addTransition(0,2,0,0.1,2);
        environment.addTransition(0,0,1,0.3,0);
        environment.addTransition(0,1,1,0.6,1);
        environment.addTransition(0,2,1,0.1,2);
        environment.addTransition(0,0,2,0.3,0);
        environment.addTransition(0,1,2,0.1,1);
        environment.addTransition(0,2,2,0.6,2);

        environment.addTransition(1,0,0,0.8,1);
        environment.addTransition(1,1,0,0.1,2);
        environment.addTransition(1,2,0,0.1,3);
        environment.addTransition(1,0,1,0.1,1);
        environment.addTransition(1,1,1,0.8,2);
        environment.addTransition(1,2,1,0.1,3);
        environment.addTransition(1,0,2,0.1,1);
        environment.addTransition(1,1,2,0.2,2);
        environment.addTransition(1,2,2,0.7,3);

        environment.addTransition(2,0,0,0.8,2);
        environment.addTransition(2,1,0,0.1,3);
        environment.addTransition(2,2,0,0.1,4);
        environment.addTransition(2,0,1,0.1,2);
        environment.addTransition(2,1,1,0.8,3);
        environment.addTransition(2,2,1,0.1,4);
        environment.addTransition(2,0,2,0.1,2);
        environment.addTransition(2,1,2,0.1,3);
        environment.addTransition(2,2,2,0.8,4);

        Policy eGreedy = new EpsilonGreedyPolicy(new Random(), 0.05);
        Agent randAgent = new Agent(environment, new RandomAlgorithm(), eGreedy, environment.getStateSpace(), environment.getActionSpace());
        Agent qAgent = new Agent(environment, new QLearning(), eGreedy, environment.getStateSpace(), environment.getActionSpace());
        Agent sarsaAgent = new Agent(environment, new SARSA(eGreedy), eGreedy, environment.getStateSpace(), environment.getActionSpace());
        Agent tdlAgent = new Agent(environment, new TemporalDifferenceLambda(), eGreedy, environment.getStateSpace(), environment.getActionSpace());
        Agent tdzAgent = new Agent(environment, new TemporalDifferenceZero(), eGreedy, environment.getStateSpace(), environment.getActionSpace());

        int num_episodes = 1000;
        int num_pulls = 1000000;

        for (int episode_no = 0; episode_no < num_episodes; episode_no++) {
            environment.setState(0);
            for (int pull_no = 0; pull_no < num_pulls; pull_no++) {
                randAgent.setState(environment.getState());
                double reward = environment.applyAction(randAgent.takeAction());
                randAgent.reinforce(reward);
            }

            environment.reset();
            environment.setState(0);

            for (int pull_no = 0; pull_no < num_pulls; pull_no++) {
                qAgent.setState(environment.getState());
                double reward = environment.applyAction(qAgent.takeAction());
                qAgent.reinforce(reward);
            }

            //((QLearning)qAgent.getAlgorithm()).printQ();

            environment.reset();
            environment.setState(0);

            for (int pull_no = 0; pull_no < num_pulls; pull_no++) {
                sarsaAgent.setState(environment.getState());
                double reward = environment.applyAction(sarsaAgent.takeAction());
                sarsaAgent.reinforce(reward);
            }

            environment.reset();
            environment.setState(0);

            /*for (int pull_no = 0; pull_no < num_pulls; pull_no++) {
                tdlAgent.setState(environment.getState());
                double reward = environment.applyAction(tdlAgent.takeAction());
                tdlAgent.reinforce(reward);
            }

            environment.reset();

            for (int pull_no = 0; pull_no < num_pulls; pull_no++) {
                tdzAgent.setState(environment.getState());
                double reward = environment.applyAction(tdzAgent.takeAction());
                tdzAgent.reinforce(reward);
            }*/
        }
        ((QLearning)qAgent.getAlgorithm()).printQ();
        System.out.format("Total reward (Random): %f\n", randAgent.getTotalReward());
        System.out.format("Total reward (Q Learning): %f\n", qAgent.getTotalReward());
        System.out.format("Total reward (SARSA): %f\n", sarsaAgent.getTotalReward());
        System.out.format("Total reward (TD Lambda): %f\n", tdlAgent.getTotalReward());
        System.out.format("Total reward (TD Zero): %f\n", tdzAgent.getTotalReward());
    }

    public static void karmMain() {
        KArmBandit environment = new KArmBandit(10);

        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            AbstractRealDistribution dist = new UniformRealDistribution(0, random.nextInt(19)+1);
            environment.setArm(i, dist);
        }

        Policy eGreedy = new EpsilonGreedyPolicy(random, 0.05);
        Agent randAgent = new Agent(environment, new RandomAlgorithm(), eGreedy, environment.getStateSpace(), environment.getActionSpace());
        Agent qAgent = new Agent(environment, new QLearning(), eGreedy, environment.getStateSpace(), environment.getActionSpace());
        Agent sarsaAgent = new Agent(environment, new SARSA(eGreedy), eGreedy, environment.getStateSpace(), environment.getActionSpace());
        Agent tdlAgent = new Agent(environment, new TemporalDifferenceLambda(), eGreedy, environment.getStateSpace(), environment.getActionSpace());
        Agent tdzAgent = new Agent(environment, new TemporalDifferenceZero(), eGreedy, environment.getStateSpace(), environment.getActionSpace());

        int num_episodes = 100;
        int num_pulls = 100;

        for (int episode_no = 0; episode_no < num_episodes; episode_no++) {
            for (int pull_no = 0; pull_no < num_pulls; pull_no++) {
                randAgent.setState(environment.getState());
                double reward = environment.applyAction(randAgent.takeAction());
                randAgent.reinforce(reward);
            }

            environment.reset();

            for (int pull_no = 0; pull_no < num_pulls; pull_no++) {
                qAgent.setState(environment.getState());
                double reward = environment.applyAction(qAgent.takeAction());
                qAgent.reinforce(reward);
            }

            environment.reset();

            for (int pull_no = 0; pull_no < num_pulls; pull_no++) {
                sarsaAgent.setState(environment.getState());
                double reward = environment.applyAction(sarsaAgent.takeAction());
                sarsaAgent.reinforce(reward);
            }

            environment.reset();

            /*for (int pull_no = 0; pull_no < num_pulls; pull_no++) {
                tdlAgent.setState(environment.getState());
                double reward = environment.applyAction(tdlAgent.takeAction());
                tdlAgent.reinforce(reward);
            }

            environment.reset();

            for (int pull_no = 0; pull_no < num_pulls; pull_no++) {
                tdzAgent.setState(environment.getState());
                double reward = environment.applyAction(tdzAgent.takeAction());
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
