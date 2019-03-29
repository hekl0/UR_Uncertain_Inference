import bn.core.*;

import java.util.ArrayList;
import java.util.List;

public class ExactInference {

    public Distribution EnumerationAsk(RandomVariable X, Assignment e, bn.base.BayesianNetwork bn) {
        Distribution Q = new bn.base.Distribution(X);

        for (Value x: X.getDomain()) {
            e.put(X, x);
            Q.put(x, EnumerateAll(bn.getVariablesSortedTopologically(), e, bn));
            e.remove(X);
        }

        Q.normalize();
        return Q;
    }

    double EnumerateAll(List<RandomVariable> vars, Assignment e, bn.base.BayesianNetwork bn) {
        if (vars.isEmpty())
            return 1;

        RandomVariable Y = vars.get(0);

        List<RandomVariable> rest = new ArrayList<>();
        for (int i = 1; i < vars.size(); i++)
            rest.add(vars.get(i));

        if (e.containsKey(Y))
            return bn.getNodeForVariable(Y).cpt.get(e.get(Y), e) * EnumerateAll(rest, e, bn);
        else {
            double sum = 0;
            for (Value y: Y.getDomain()) {
                e.put(Y, y);
                sum += bn.getNodeForVariable(Y).cpt.get(y, e) * EnumerateAll(rest, e, bn);
                e.remove(Y);
            }
            return sum;
        }
    }
}
