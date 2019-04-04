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
        for (RandomVariable var : bn.getVariablesSortedTopologically()) {
            result.put(var, sampling(var,result.copy(),bn));
        }
        return result;
    }

    public Value sampling(RandomVariable variable, bn.core.Assignment assignment, bn.base.BayesianNetwork bayesianNetwork) {
        Distribution distribution = new Distribution(variable);

        for (Value value: variable.getDomain()) {
            assignment.put(variable, value);
            bn.base.BayesianNetwork.Node node = bayesianNetwork.getNodeForVariable(variable);

            double w = node.cpt.get(value, assignment);

            distribution.set(value, w);
        }

        distribution.normalize();

        double random = 1 - Math.random();
        for (Value value: distribution.keySet()) {
            random -= distribution.get(value);
            if (random <= 0)
                return value;
        }

        return null;
    }
}