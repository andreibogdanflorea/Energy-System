package strategies;

/**
 * Singleton Factory pattern for creating strategies
 */
public final class StrategyFactory {
    private static StrategyFactory instance = null;

    private StrategyFactory() {
    }

    /**
     * Get the singleton instance of the Strategy Factory
     *
     * @return the instance of the Strategy Factory
     */
    public static StrategyFactory getInstance() {
        if (instance == null) {
            instance = new StrategyFactory();
        }
        return instance;
    }

    /**
     * Factory pattern for creating a strategy
     *
     * @param type GREEN, PRICE or QUANTITY strategies
     * @return the strategy for choosing producers
     */
    public ChooseProducersStrategy createStrategy(final EnergyChoiceStrategyType type) {
        return switch (type) {
            case GREEN -> new GreenStrategy();
            case PRICE -> new PriceStrategy();
            case QUANTITY -> new QuantityStrategy();
        };
    }
}
