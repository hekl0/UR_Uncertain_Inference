import bn.base.Assignment;
import bn.base.BayesianNetwork;
import bn.base.Domain;
import bn.core.RandomVariable;
import bn.core.Value;
import bn.parser.XMLBIFParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Iterator;

public class Main {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        XMLBIFParser parser = new XMLBIFParser();
        BayesianNetwork network = (BayesianNetwork) parser.readNetworkFromFile(args[1]);

        RandomVariable queryVariable = network.getVariableByName(args[2]);

        Assignment assignment = new Assignment();
        int index = 3;
        while (index < args.length) {
            RandomVariable variable = network.getVariableByName(args[index++]);
            Value value = ((bn.base.Domain) variable.getDomain()).getValueByString(args[index++]);
            assignment.put(variable, value);
            System.out.println(variable);
            System.out.println(value);
        }

        System.out.println();
        System.out.println(network);

        ExactInference inference = new ExactInference();
        System.out.println(inference.EnumerationAsk(queryVariable, assignment, network));
    }
}
