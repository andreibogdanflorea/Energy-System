package fileio;

import entities.EnergyType;

public final class ProducerInput implements EnergyEntityInput {
    private Integer id;
    private EnergyType energyType;
    private Integer maxDistributors;
    private Double priceKW;
    private Integer energyPerDistributor;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public EnergyType getEnergyType() {
        return energyType;
    }

    public void setEnergyType(final EnergyType energyType) {
        this.energyType = energyType;
    }

    public Integer getMaxDistributors() {
        return maxDistributors;
    }

    public void setMaxDistributors(final Integer maxDistributors) {
        this.maxDistributors = maxDistributors;
    }

    public Double getPriceKW() {
        return priceKW;
    }

    public void setPriceKW(final Double priceKW) {
        this.priceKW = priceKW;
    }

    public Integer getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    public void setEnergyPerDistributor(final Integer energyPerDistributor) {
        this.energyPerDistributor = energyPerDistributor;
    }
}
