import bn.base.Assignment;
import bn.base.BayesianNetwork;
import bn.base.Distribution;
import bn.core.RandomVariable;
import bn.core.Value;


public class RejectSampling {
    public Distribution rejectionSampling(RandomVariable X, Assignment e, BayesianNetwork bn, int numSamples) {
        Distribution distribution = new Distribution(X);
        for (Value value : X.getDomain())
            distribution.put(value,0.0);
        for (int i = 0; i < numSamples; i++) {
            Assignment x = priorSample(bn);
            if (x.containsAll(e)) {
                distribution.set(x.get(X), distribution.get(x.get(X)) + 1);
            }
        }
        distribution.normalize();
        return distribution;

    }

    public Assignment priorSample(BayesianNetwork bn) {
        Assignment result = new Assignment();
        for (RandomVariable variable : bn.getVariablesSortedTopologically())
            result.put(variable, variable.getDomain().iterator().next());
        for (RandomVariable var : bn.getVariablesSortedTopologically()) {
            result.put(var,Gibbs.sampling(var,result.copy(),bn));
        }
        return result;
    }
}