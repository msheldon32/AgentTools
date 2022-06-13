package AgentTools.Function;

import AgentTools.Util.FloatSpace;
import AgentTools.Util.MultiSpace;
import AgentTools.Util.ValueSpace;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.Line;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class LinearApproximatorTest {
    private int nIterations;
    private int nTrials;
    private int nTests;
    private int maxParams;
    private Random random;
    private double rangeMin;
    private double rangeMax;
    private double learningRate;
    private double xTolerance;
    private double yTolerance;
    private double errStd;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        this.nIterations = 100000;
        this.nTrials = 100;
        this.nTests = 100;
        this.maxParams = 10;
        this.random = new Random();
        this.learningRate = 0.005;

        this.rangeMin = -10;
        this.rangeMax = 10;

        this.yTolerance = 1.0;
        this.xTolerance = 0.1;
        this.errStd = 0.1;
    }

    @Test
    void getSA() {
    }

    @Test
    void getValue() {
    }

    @Test
    void getGradients() {
    }

    @Test
    void fitResult() {
        for (int i = 0; i < this.nTrials; i++) {
            int nParams = this.random.nextInt(this.maxParams) + 1;
            List<Object> params = new ArrayList<Object>(nParams);
            List<Double> coefs = new ArrayList<Double>(nParams);

            List<ValueSpace> spaces = new ArrayList<>(nParams);

            for (int j = 0; j < nParams; j++) {
                double first = this.random.nextDouble() * (this.rangeMax-this.rangeMin) + this.rangeMin;
                double second = this.random.nextDouble() * (this.rangeMax-this.rangeMin) + this.rangeMin;
                double param = 0.0;
                params.add(param);
                spaces.add(new FloatSpace(Math.min(first, second), Math.max(first, second)));
            }

            MultiSpace domain = new MultiSpace(spaces);

            LinearApproximator approximator = new LinearApproximator(params, domain, this.learningRate);

            //

            for (int j = 0; j < nParams; j++) {
                coefs.add(this.random.nextDouble() * (this.rangeMax-this.rangeMin) + this.rangeMin);
            }

            for (int j = 0; j < this.nIterations; j++) {
                double realVal = 0;
                List<Object> xVal = domain.getRealization(this.random);
                for (int k = 0; k < nParams; k++) {
                    realVal += (Double)coefs.get(k) * (Double) xVal.get(k);
                }

                // add gaussian noise
                realVal += this.random.nextGaussian()*this.errStd;

                approximator.fitResult(xVal, realVal);
            }

            /*for (int j = 0; j < nParams; j++) {
                System.out.format("%d: expected %f, got %f\n", j, coefs.get(j), params.get(j));
            }*/

            for (int j = 0; j < this.nTests; j++) {
                double realVal = 0;
                List<Object> xVal = domain.getRealization(this.random);
                for (int k = 0; k < nParams; k++) {
                    realVal += (Double)coefs.get(k) * (Double) xVal.get(k);
                }

                double yHat = approximator.getValue(xVal);
                //System.out.format("Expected %f, got %f\n", realVal, yHat);
                assertTrue(yHat > realVal-this.yTolerance);
                assertTrue(yHat < realVal+this.yTolerance);
            }
        }
    }

    @Test
    void getMaxAction() {
        for (int i = 0; i < this.nTrials; i++) {
            int nParams = this.random.nextInt(this.maxParams) + 1;
            List<Object> params = new ArrayList<Object>(nParams);
            List<Double> coefs = new ArrayList<Double>(nParams);

            List<ValueSpace> spaces = new ArrayList<>(nParams);

            for (int j = 0; j < nParams; j++) {
                double first = this.random.nextDouble() * (this.rangeMax - this.rangeMin) + this.rangeMin;
                double second = this.random.nextDouble() * (this.rangeMax - this.rangeMin) + this.rangeMin;
                double param = 0.0;
                params.add(param);
                spaces.add(new FloatSpace(Math.min(first, second), Math.max(first, second)));
            }

            MultiSpace domain = new MultiSpace(spaces);

            LinearApproximator approximator = new LinearApproximator(params, domain, this.learningRate);

            //

            for (int j = 0; j < nParams; j++) {
                coefs.add(this.random.nextDouble() * (this.rangeMax - this.rangeMin) + this.rangeMin);
            }

            for (int j = 0; j < this.nIterations; j++) {
                double realVal = 0;
                List<Object> xVal = domain.getRealization(this.random);
                for (int k = 0; k < nParams; k++) {
                    realVal += (Double) coefs.get(k) * (Double) xVal.get(k);
                }

                // add gaussian noise
                realVal += this.random.nextGaussian() * this.errStd;

                approximator.fitResult(xVal, realVal);
            }

            // first n: state
            // last one: action

            if (nParams == 1) {
                continue;
            }

            List<Object> stateList = new ArrayList<Object>(nParams-1);
            List<Object> maxList = new ArrayList<Object>(nParams-1);
            List<Object> offList = new ArrayList<Object>(nParams-1);
            List<Object> minIn = new ArrayList<Object>(nParams-1);
            List<Object> maxIn = new ArrayList<Object>(nParams-1);
            for (int j = 0; j < nParams - 1; j++) {
                double realization = (Double) spaces.get(j).getRealization(this.random);
                stateList.add(realization);
                maxList.add(realization);
                offList.add(realization);
                minIn.add(realization);
                maxIn.add(realization);
            }

            double maxAction = ((double) approximator.getMaxAction(stateList, domain.getSpaces().get(nParams-1)));
            //System.out.format("Max action: %f\n", maxAction);
            double oneOff = 0.0;
            if (maxAction == ((FloatSpace)spaces.get(nParams-1)).getMax()) {
                oneOff = maxAction - 1;
            } else if (maxAction == ((FloatSpace)spaces.get(nParams-1)).getMin()) {
                oneOff = maxAction + 1;
            } else {
                assertTrue(false);
            }
            minIn.add(((FloatSpace)spaces.get(nParams-1)).getMin());
            maxIn.add(((FloatSpace)spaces.get(nParams-1)).getMax());

            maxList.add(maxAction);
            offList.add(oneOff);

            assertTrue(approximator.getValue(maxList) > approximator.getValue(offList));
            assertTrue(approximator.getValue(maxList) >= approximator.getValue(maxIn));
            assertTrue(approximator.getValue(maxList) >= approximator.getValue(minIn));
        }
    }
}