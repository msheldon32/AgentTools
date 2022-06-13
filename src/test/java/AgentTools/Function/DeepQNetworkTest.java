package AgentTools.Function;

import AgentTools.Models.MDPModel;
import AgentTools.Util.*;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.LossLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.junit.jupiter.api.Test;
import org.nd4j.linalg.api.ops.LossFunction;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DeepQNetworkTest {
    private DeepQNetwork deepQNetwork;
    private MultiLayerConfiguration configuration;
    private ValueSpace pairSpace;
    private MDPModel model;

    private int nRuns;
    private int nEpochs;
    private int nStates;
    private int nActions;

    private ValueSpace actionSpace;
    private ValueSpace stateSpace;
    private ValueSpace stateSpaceBox;

    private Random random;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        this.nRuns = 1000;
        this.nEpochs = 1000;
        this.nStates = 10;
        this.nActions = 10;

        this.actionSpace = new IntegerSpace(0, this.nActions);
        this.stateSpace = new IntegerSpace(0, this.nStates);

        List<ValueSpace> stateSpaces = new ArrayList<ValueSpace>();
        stateSpaces.add(this.stateSpace);
        this.stateSpaceBox = new MultiSpace(stateSpaces);

        this.pairSpace = new PairSpace(this.stateSpaceBox, this.actionSpace);

        this.random = new Random();

        this.configuration = new NeuralNetConfiguration.Builder().seed(this.random.nextInt())
                .optimizationAlgo(OptimizationAlgorithm.CONJUGATE_GRADIENT)
                .list()
                .layer(new DenseLayer.Builder().nIn(1).nOut(5).build())
                .layer(new DenseLayer.Builder().nIn(5).nOut(5).build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.SQUARED_LOSS).nIn(5).nOut(this.nActions).build())
                .build();

        this.deepQNetwork = new DeepQNetwork(this.configuration,this.pairSpace,new Random());

        this.model = new ModelGenerator(this.nStates, this.nActions).getModel();
    }

    @Test
    void getSA() {
    }

    @Test
    void getValue() {
    }

    @Test
    void getV() {
    }

    @Test
    void getAllQ() {
    }

    @Test
    void fitResult() {
        for (int i = 0; i < nRuns; i++) {
            this.configuration = new NeuralNetConfiguration.Builder().seed(this.random.nextInt())
                    .optimizationAlgo(OptimizationAlgorithm.CONJUGATE_GRADIENT)
                    .list()
                    .layer(new DenseLayer.Builder().nIn(1).nOut(5).build())
                    .layer(new DenseLayer.Builder().nIn(5).nOut(5).build())
                    .layer(new OutputLayer.Builder(LossFunctions.LossFunction.SQUARED_LOSS).nIn(5).nOut(this.nActions).build())
                    .build();

            this.deepQNetwork = new DeepQNetwork(configuration,pairSpace,new Random());

            this.model = new ModelGenerator(this.nStates, this.nActions).getModel();

            for (int j = 0; j < this.nEpochs; j++) {
                int curState = this.random.nextInt(this.nStates);
                List<Object> stateArray = new ArrayList<Object>();
                stateArray.add(curState);
                int actionTaken = this.random.nextInt(this.nActions);
                this.model.setState(curState);
                this.deepQNetwork.fitResult(new Pair<List, Integer>(stateArray, actionTaken), this.model.applyAction(actionTaken));
            }
        }
    }

    @Test
    void getMaxAction() {
    }
}