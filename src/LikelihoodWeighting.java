import bn.base.Assignment;
import bn.base.BayesianNetwork;
import bn.base.Distribution;
import bn.core.RandomVariable;
import bn.core.Value;


public class LikelihoodWeighting {
    public Distribution likelihoodWeighting(RandomVariable X, Assignment e, BayesianNetwork bn, int numSamples) {
        Distribution distribution = new Distribution(X);
        for (Value value : X.getDomain()){
            distribution.put(value,0.0);
        }
        for (int i = 0; i < numSamples; i++){
            Assignment x = new Assignment();
            double w = weightedSample(bn,e,x);
            distribution.set(x.get(X),distribution.get(x.get(X)) + w);
        }
        distribution.normalize();
        return distribution;
    }
    public double weightedSample(BayesianNetwork bn, Assignment e, Assignment x){
        double result = 1.0;
        x = e.copy();
        for (RandomVariable var : bn.getVariablesSortedTopologically()){
            if (!x.containsKey(var)){
                x.put(var,var.getDomain().iterator().next());
            }
        }
        for (RandomVariable var : bn.getVariablesSortedTopologically()){
            if (e.containsKey(var)){
                bn.base.BayesianNetwork.Node node = bn.getNodeForVariable(var);

                result *= node.cpt.get(e.get(var), e);
                for (bn.base.BayesianNetwork.Node children: node.children)
                    result *= children.cpt.get(e.get(children.variable), e);

            }
            else {
                //x.put(var,Gibbs.sampling(var,x,bn));
            }
        }
        return result;
    }
}
