package fileio;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public final class InputParser {
    private final String inputPath;

    public InputParser(final String inputPath) {
        this.inputPath = inputPath;
    }

    /**
     * Loads the input from inputPath into an Input object
     *
     * @return the whole input contained in the Input object
     * @throws IOException in care parsing from the input file fails
     */
    public Input loadInput() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(inputPath), Input.class);
    }
}
