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
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        Scanner scanner = new Scanner(System.in);

        XMLBIFParser parser = new XMLBIFParser();
        BayesianNetwork network = (BayesianNetwork) parser.readNetworkFromFile(args[0]);

        RandomVariable queryVariable = network.getVariableByName(args[1]);

        Assignment assignment = new Assignment();
        int index = 2;
        while (index < args.length) {
            RandomVariable variable = network.getVariableByName(args[index++]);
            Value value = ((bn.base.Domain) variable.getDomain()).getValueByString(args[index++]);
            assignment.put(variable, value);
        }

        System.out.println("Uncertain Inference made by ....");
        System.out.println();
        System.out.println("Please choose algorithm:");
        System.out.println("  1. Exact Inference");
        System.out.println("  2. Quen ten r ~~");
        System.out.println("  3. Quen ten not");
        System.out.println("  4. Gibbs");
        System.out.print("Your choice: ");
        int algo = scanner.nextInt();

        switch (algo) {
            case 1:
                ExactInference inference = new ExactInference();
                System.out.println(inference.EnumerationAsk(queryVariable, assignment, network));
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                System.out.print("Number of Samples: ");
                int numSamples = scanner.nextInt();
                Gibbs gibbs = new Gibbs();
                System.out.println(gibbs.GibbsAsk(queryVariable, assignment, network, numSamples));
                break;
        }
    }
}
