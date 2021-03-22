package fileio;

import java.util.List;

public final class MonthlyUpdateInput {
    private List<ConsumerInput> newConsumers;
    private List<DistributorChangesInput> distributorChanges;
    private List<ProducerChangesInput> producerChanges;

    public List<ConsumerInput> getNewConsumers() {
        return newConsumers;
    }

    public void setNewConsumers(final List<ConsumerInput> newConsumers) {
        this.newConsumers = newConsumers;
    }

    public List<DistributorChangesInput> getDistributorChanges() {
        return distributorChanges;
    }

    public void setDistributorChanges(final List<DistributorChangesInput> costChanges) {
        this.distributorChanges = costChanges;
    }

    public List<ProducerChangesInput> getProducerChanges() {
        return producerChanges;
    }

    public void setProducerChanges(final List<ProducerChangesInput> producerChanges) {
        this.producerChanges = producerChanges;
    }
}
