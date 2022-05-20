package AgentTools.Function;

import AgentTools.Util.*;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.shade.protobuf.Value;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DeepQNetwork extends FunctionApproximator{
    /*
            This is a wrapper for deeplearning4j
     */

    protected MultiLayerConfiguration configuration;
    protected MultiLayerNetwork network;
    protected Random random;
    protected int nActions;

    protected HashMap<Object, Integer> actionMap;

    public DeepQNetwork(MultiLayerConfiguration configuration, ValueSpace domain, Random random) {
        super(null, domain, 0);

        assert (domain instanceof PairSpace);

        ValueSpace actionSpace = ((PairSpace)domain).rightSpace();

        this.nActions = actionSpace.getSize();

        this.network = new MultiLayerNetwork(configuration);
        network.init();

        this.random = random;

        this.actionMap = new HashMap<Object, Integer>();

        Iterator actionIterator = actionSpace.iterator();
        int idx = 0;
        while (actionIterator.hasNext()) {
            this.actionMap.put(actionIterator.next(), idx);
            idx += 1;
        }
    }

    @Override
    public Object getSA(Object state, Object action) {
        return new Pair<Collection<Object>, Integer>((Collection<Object>) state, (Integer) action);
    }

    @Override
    public double getValue(Object inValue) {
        Pair<Collection, Integer> inPair = (Pair<Collection, Integer>)inValue;

        int actionIdx = this.actionMap.get(inPair.getRight());

        INDArray inputs  = Nd4j.create((List<Double>)(inPair.getLeft().stream().collect(Collectors.toList())));

        return this.network.output(inputs).getDouble(actionIdx);
    }

    @Override
    public double getV(Object state) {
        throw new RuntimeException("Deep Q Networks cannot approximate the V Function");
    }

    public double[] getAllQ(Collection state) {
        INDArray inputs  = Nd4j.create((List<Double>)(state.stream().collect(Collectors.toList())));

        double[] outQ = this.network.output(inputs).toDoubleVector();

        return outQ;
    }

    @Override
    public void fitResult(Object inValue, double newVal) {
        Pair<Collection, Object> inPair = (Pair<Collection, Object>)inValue;
        INDArray inputs  = Nd4j.create((List<Double>)(inPair.getLeft().stream().collect(Collectors.toList())));

        double[] r_vals = this.getAllQ(inPair.getLeft());

        r_vals[this.actionMap.get(inPair.getRight())] = newVal;


        INDArray outputs = Nd4j.createFromArray(r_vals);
        this.network.fit(inputs, outputs);
    }

    @Override
    public Object getMaxAction(Object state, ValueSpace actionSpace) {
        Iterator<Object> actionIterator = actionSpace.iterator();
        int curInt = (Integer) actionIterator.next();
        Object stateAction = this.getSA(state, curInt);
        List<Integer> argMax = new ArrayList<Integer>();
        argMax.add(curInt);
        double maxQ = this.getValue(stateAction);

        while (actionIterator.hasNext()) {
            curInt = (Integer) actionIterator.next();
            stateAction = this.getSA(state, curInt);
            double curQ = this.getValue(stateAction);

            if (curQ > maxQ) {
                argMax.clear();
                argMax.add(curInt);
                maxQ = curQ;
            } else if (curQ == maxQ) {
                argMax.add(curInt);
            }
        }

        int argIdx = this.random.nextInt(argMax.size());
        return argMax.get(argIdx);
    }
}
