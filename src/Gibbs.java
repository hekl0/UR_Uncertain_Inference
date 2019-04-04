import bn.base.Distribution;
import bn.core.*;

import java.util.ArrayList;
import java.util.List;

public class Gibbs {

    public Distribution GibbsAsk(RandomVariable X, Assignment e, bn.base.BayesianNetwork bn, int numSamples) {
        //create nonevidence variable list Z
        List<RandomVariable> nonevidenceVariables = new ArrayList<>();
        for (RandomVariable variable : bn.getVariablesSortedTopologically())
            if (!e.containsKey(variable))
                nonevidenceVariables.add(variable);

        //initialize one valid state
        Assignment currentState = e.copy();
        for (RandomVariable variable : nonevidenceVariables)
            currentState.put(variable, variable.getDomain().iterator().next());

        Distribution distribution = new Distribution(X);
        for (Value value: X.getDomain())
            distribution.put(value, 0.0);

        int index = 0;
        while (numSamples > 0) {
            RandomVariable samplingVariable =  nonevidenceVariables.get(index);
            currentState.put(samplingVariable, sampling(samplingVariable, currentState.copy(), bn));

            Value value = currentState.get(X);
            distribution.set(value, distribution.get(value) + 1);

            index++;
            if (index == nonevidenceVariables.size()) index = 0;
            numSamples--;
        }

        distribution.normalize();
        return distribution;
    }

    public Value sampling(RandomVariable variable, Assignment assignment, bn.base.BayesianNetwork bayesianNetwork) {
        Distribution distribution = new Distribution(variable);

        for (Value value: variable.getDomain()) {
            assignment.put(variable, value);
            bn.base.BayesianNetwork.Node node = bayesianNetwork.getNodeForVariable(variable);

            double w = node.cpt.get(value, assignment);
            for (bn.base.BayesianNetwork.Node children: node.children)
                w *= children.cpt.get(assignment.get(children.variable), assignment);

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
