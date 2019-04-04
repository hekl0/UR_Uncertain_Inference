import bn.base.Assignment;
import bn.base.BayesianNetwork;
import bn.base.Distribution;
import bn.core.RandomVariable;
import bn.core.Value;
public class LikelihoodWeighting {
    public Distribution likelihoodWeighting(RandomVariable queryVariable, Assignment assignment, BayesianNetwork network, int numSamples) {
        Distribution distribution = new Distribution(queryVariable);
        for (Value var : queryVariable.getDomain())
            distribution.put(var,0.0);
        for (int i = 0; i < numSamples; i++){
            Assignment x = assignment.copy();
            double w = weightedSample(network,x);
            distribution.set(x.get(queryVariable),distribution.get(x.get(queryVariable))+w);
        }
        distribution.normalize();
        return distribution;
    }

    public double weightedSample(BayesianNetwork network, Assignment x) {
        double w = 1.0;
        for (RandomVariable var : network.getVariablesSortedTopologically()){
            if (x.containsKey(var)){
                w *= network.getNodeForVariable(var).cpt.get(x.get(var),x);
            }
            else {
                x.put(var,RejectSampling.sampling(var,x.copy(),network));
            }
        }
        return w;
    }
}
