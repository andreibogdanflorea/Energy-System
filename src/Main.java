import fileio.Input;
import fileio.InputParser;
import fileio.OutputWriter;
import simulation.MonthlySimulation;


/**
 * Entry point to the simulation
 */
public final class Main {

    private Main() {
    }

    /**
     * Main function which reads the input file and starts simulation
     *
     * @param args input and output files
     * @throws Exception might error when reading/writing/opening files, parsing JSON
     */
    public static void main(final String[] args) throws Exception {
        InputParser inputParser = new InputParser(args[0]);
        Input input = inputParser.loadInput();

        MonthlySimulation simulation = new MonthlySimulation(input);
        simulation.start();

        OutputWriter outputWriter = new OutputWriter(args[1]);
        outputWriter.writeOutput(simulation.getDatabase());
    }
}
