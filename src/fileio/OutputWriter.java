package fileio;

import com.fasterxml.jackson.databind.ObjectMapper;
import simulation.SimulationDatabase;

import java.io.File;
import java.io.IOException;

public final class OutputWriter {
    private final String outputPath;

    public OutputWriter(final String outputPath) {
        this.outputPath = outputPath;
    }

    /**
     * Writes the output in JSON format to the designated output file
     *
     * @param database contains information about consumers, distributors and producers
     *                 at the end of the simulation
     * @throws IOException in case writing to the output file fails
     */
    public void writeOutput(SimulationDatabase database) throws IOException {
        Output output = new Output(database);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(outputPath), output);
    }
}
