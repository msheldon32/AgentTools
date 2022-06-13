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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SampleRuns {
    public static void main(String[] args) {

    }

    public static void queueRouting() {

    }

    protected class QueueRouting {
        List<Double> serviceRates;
        List<Double> rewards;
        List<Double> holdingCosts;
        List<Integer> queues;
        double totalReward;
        double arrivalRate;

        double t;
        double max_t;

        Random random;

        public QueueRouting(List<Double> serviceRates, List<Double> holdingCosts, List<Double> rewards, double arrivalRate, double max_t) {
            this.serviceRates = serviceRates;
            this.rewards = rewards;
            this.holdingCosts = holdingCosts;
            this.queues = new ArrayList<Integer>();
            this.arrivalRate = arrivalRate;
            this.totalReward = 0;

            this.t = 0;
            this.max_t = max_t;

            this.random = new Random();
        }

        public boolean isTerminal() {
            return this.t >= this.max_t;
        }

        public double getIncReward(double timeDelta) {
            double outReward = 0;
            for (int i = 0; i < this.queues.size(); i++) {
                outReward += this.queues.get(i) * this.holdingCosts.get(i);
            }

            return outReward*timeDelta;
        }

        public double step() {
            /*
                Run until an arrival comes.

                Return: total reward (rewards * service).
             */

            List<Double> rates;

            double totalReward = 0;

            while (this.t < this.max_t) {
                rates = new ArrayList<Double>();
                double totalMu = 0;
                for (int i = 0; i < this.serviceRates.size(); i++) {
                    double rate = 0.0;
                    if (queues.get(i) != 0) {
                        rate = this.serviceRates.get(i);
                    }
                    totalMu += rate;
                    rates.add(rate);
                }

                double timeDelta = -Math.log(this.random.nextDouble())/(this.arrivalRate+totalMu);

                if (this.t + timeDelta >= this.max_t) {
                    break;
                }

                this.t += timeDelta;

                totalReward += this.getIncReward(timeDelta);

                double prob = this.random.nextDouble();
                double cProb = (this.arrivalRate)/(this.arrivalRate+totalMu);
                if (prob <= cProb) {
                    break; // arrivalEvent
                }

                for (int i = 0; i < this.serviceRates.size(); i++) {
                    if (queues.get(i) == 0) {
                        continue;
                    }
                    cProb += (this.serviceRates.get(i)/(this.arrivalRate+totalMu));
                    if (prob <= cProb) {
                        queues.set(i, queues.get(i)-1);
                        totalReward += this.rewards.get(i);
                        break;
                    }
                }
            }

            return totalReward;
        }
    }
}
