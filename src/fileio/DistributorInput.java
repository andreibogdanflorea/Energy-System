package fileio;

import strategies.EnergyChoiceStrategyType;

public final class DistributorInput implements EnergyEntityInput {
    private Integer id;
    private Integer contractLength;
    private Integer initialBudget;
    private Integer initialInfrastructureCost;
    private Integer energyNeededKW;
    private EnergyChoiceStrategyType producerStrategy;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getContractLength() {
        return contractLength;
    }

    public void setContractLength(final Integer contractLength) {
        this.contractLength = contractLength;
    }

    public Integer getInitialBudget() {
        return initialBudget;
    }

    public void setInitialBudget(final Integer initialBudget) {
        this.initialBudget = initialBudget;
    }

    public Integer getInitialInfrastructureCost() {
        return initialInfrastructureCost;
    }

    public void setInitialInfrastructureCost(final Integer initialInfrastructureCost) {
        this.initialInfrastructureCost = initialInfrastructureCost;
    }

    public Integer getEnergyNeededKW() {
        return energyNeededKW;
    }

    public void setEnergyNeededKW(final Integer energyNeededKW) {
        this.energyNeededKW = energyNeededKW;
    }

    public EnergyChoiceStrategyType getProducerStrategy() {
        return producerStrategy;
    }

    public void setProducerStrategy(final EnergyChoiceStrategyType producerStrategy) {
        this.producerStrategy = producerStrategy;
    }
}
