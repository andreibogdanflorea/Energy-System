package entities;

import fileio.ConsumerInput;
import fileio.DistributorInput;
import fileio.EnergyEntityInput;
import fileio.ProducerInput;

/**
 * Singleton Factory pattern for creating energy entities
 */
public final class EnergyEntityFactory {
    private static EnergyEntityFactory instance = null;

    private EnergyEntityFactory() {
    }

    /**
     * Get the singleton instance of the Energy Entity Factory
     *
     * @return the instance of the Energy Entity Factory
     */
    public static EnergyEntityFactory getInstance() {
        if (instance == null) {
            instance = new EnergyEntityFactory();
        }
        return instance;
    }

    /**
     * Factory pattern for creating either a consumer or a distributor
     *
     * @param type  a consumer, a distributor or a producer
     * @param input the specific input, either a consumer input or a distributor input
     * @return the energy entity created
     */
    public EnergyEntity createEnergyEntity(final EnergyEntityType type,
                                           final EnergyEntityInput input) {
        return switch (type) {
            case CONSUMER -> new Consumer((ConsumerInput) input);
            case DISTRIBUTOR -> new Distributor((DistributorInput) input);
            case PRODUCER -> new Producer((ProducerInput) input);
        };
    }

    public enum EnergyEntityType {
        CONSUMER, DISTRIBUTOR, PRODUCER
    }
}
