package fileio;

public final class ProducerChangesInput {
    private Integer id;
    private Integer energyPerDistributor;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    public void setEnergyPerDistributor(final Integer energyPerDistributor) {
        this.energyPerDistributor = energyPerDistributor;
    }
}
