import bn.base.Assignment;
import bn.base.BayesianNetwork;
import bn.base.Domain;
import bn.core.Value;
import bn.parser.XMLBIFParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Iterator;

public class Main {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        XMLBIFParser parser = new XMLBIFParser();
        BayesianNetwork network = (BayesianNetwork) parser.readNetworkFromFile("src/examples/aima-alarm.xml");
        System.out.println(network);

        ExactInference inference = new ExactInference();
        Assignment assignment = new Assignment();
        assignment.put(network.getVariableByName("M"), network.getVariableByName("M").getDomain().iterator().next());
        assignment.put(network.getVariableByName("J"), network.getVariableByName("J").getDomain().iterator().next());
        System.out.println(inference.EnumerationAsk(network.getVariableByName("B"), assignment, network));

        bn.base.Domain domain = (Domain) network.getVariableByName("M").getDomain();
        System.out.println(domain.getValueByString("true"));
    }
}
