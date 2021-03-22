package simulation;

import entities.Consumer;
import entities.Distributor;
import entities.EnergyEntity;
import entities.EnergyEntityFactory;
import entities.Producer;
import fileio.ConsumerInput;
import fileio.DistributorChangesInput;
import fileio.DistributorInput;
import fileio.InitialDataInput;
import fileio.MonthlyUpdateInput;
import fileio.ProducerChangesInput;
import fileio.ProducerInput;

import java.util.ArrayList;
import java.util.List;

/**
 * The database of consumers, distributors and producers used for a single simulation
 * also contains information about the monthly updates
 */
public final class SimulationDatabase {
    private final List<Consumer> consumers;
    private final List<Distributor> distributors;
    private final List<Producer> producers;
    private final List<MonthlyUpdateInput> monthlyUpdates;

    public SimulationDatabase(final InitialDataInput initialData,
                              final List<MonthlyUpdateInput> monthlyUpdatesInput) {
        consumers = new ArrayList<>();
        distributors = new ArrayList<>();
        producers = new ArrayList<>();

        for (ConsumerInput consumerInput : initialData.getConsumers()) {
            EnergyEntity entity = EnergyEntityFactory.getInstance()
                    .createEnergyEntity(EnergyEntityFactory.EnergyEntityType.CONSUMER,
                            consumerInput);
            consumers.add((Consumer) entity);
        }

        for (DistributorInput distributorInput : initialData.getDistributors()) {
            EnergyEntity entity = EnergyEntityFactory.getInstance()
                    .createEnergyEntity(EnergyEntityFactory.EnergyEntityType.DISTRIBUTOR,
                            distributorInput);
            distributors.add((Distributor) entity);
        }

        for (ProducerInput producerInput : initialData.getProducers()) {
            EnergyEntity entity = EnergyEntityFactory.getInstance()
                    .createEnergyEntity(EnergyEntityFactory.EnergyEntityType.PRODUCER,
                            producerInput);
            producers.add((Producer) entity);
        }

        monthlyUpdates = monthlyUpdatesInput;
    }

    public List<Consumer> getConsumers() {
        return consumers;
    }

    public List<Distributor> getDistributors() {
        return distributors;
    }

    public List<Producer> getProducers() {
        return producers;
    }

    private Distributor getDistributorById(final Integer id) {
        return distributors.get(id);
    }

    private Producer getProducerById(final Integer id) {
        return producers.get(id);
    }

    private Distributor getOptimalDistributor() {
        Distributor chosenDistributor = null;
        int minimumPrice = Integer.MAX_VALUE;

        for (Distributor distributor : distributors) {
            Distributor.ContractsInfo contractsInfo = distributor.getContractsInfo();

            if (!distributor.getBankrupt() && contractsInfo.getContractPrice() < minimumPrice) {
                minimumPrice = contractsInfo.getContractPrice();
                chosenDistributor = distributor;
            }
        }

        return chosenDistributor;
    }

    private void signContractsWith(final Distributor distributor) {
        for (Consumer consumer : consumers) {
            if (consumer.getBankrupt()) {
                continue;
            }
            if (consumer.getContract() == null || consumer.getContract().ended()) {
                consumer.signContractWith(distributor);
            }
        }
    }

    private Boolean signContracts() {
        Distributor optimalDistributor = getOptimalDistributor();
        if (optimalDistributor == null) {
            return true;
        }
        signContractsWith(optimalDistributor);
        return false;
    }

    private void refreshDistributorsPrices() {
        distributors.forEach(Distributor::refreshPrices);
    }

    private void chooseProducersForDistributors() {
        for (Distributor distributor : distributors) {
            Distributor.ProducersInfo producersInfo = distributor.getProducersInfo();

            if (producersInfo.getNeedToUpdateProducers()) {
                producersInfo.getProducersStrategy().chooseProducersFor(distributor, producers);
                producersInfo.setNeedToUpdateProducers(false);
            }
        }
    }

    private void updateConsumersAndDistributors(final Integer turn) {
        addNewConsumers(turn);
        updateDistributorsCosts(turn);
    }

    private void addNewConsumers(final Integer turn) {
        List<ConsumerInput> newConsumers = monthlyUpdates.get(turn).getNewConsumers();

        for (ConsumerInput newConsumerInput : newConsumers) {
            EnergyEntity entity = EnergyEntityFactory.getInstance()
                    .createEnergyEntity(EnergyEntityFactory.EnergyEntityType.CONSUMER,
                            newConsumerInput);

            consumers.add((Consumer) entity);
        }
    }

    private void updateDistributorsCosts(final Integer turn) {
        List<DistributorChangesInput> distributorCostChanges
                = monthlyUpdates.get(turn).getDistributorChanges();

        for (DistributorChangesInput costChange : distributorCostChanges) {
            Distributor distributor = getDistributorById(costChange.getId());
            if (distributor == null) {
                System.out.println("Invalid distributor id.");
                System.exit(1);
            }

            distributor.getCosts().setInfrastructureCost(costChange.getInfrastructureCost());
        }

        refreshDistributorsPrices();
    }

    private void updateProducersCosts(final Integer turn) {
        List<ProducerChangesInput> producerChanges
                = monthlyUpdates.get(turn).getProducerChanges();

        for (ProducerChangesInput costChange : producerChanges) {
            Producer producer = getProducerById(costChange.getId());
            if (producer == null) {
                System.out.println("Invalid producer id.");
                System.exit(1);
            }

            producer.changeEnergyPerDistributor(costChange.getEnergyPerDistributor());
        }
    }

    private void updateProducersMonthlyStats(final Integer turn) {
        for (Producer producer : producers) {
            producer.addStatsRecord(turn + 1);
        }
    }

    private void removeNewlyBankruptConsumersContracts() {
        for (Consumer consumer : consumers) {
            if (consumer.getBankrupt() && consumer.getContract() != null) {
                Distributor distributor = consumer.getContract().getDistributor();
                distributor.getContractsInfo().getContracts().remove(consumer.getContract());
                consumer.setContract(null);
            }
        }
    }

    /**
     * Update the database according to the start of the simulation
     * <p>
     * Specifically, the distributors choose their producers, they compute their production cost
     * and contract price and the consumers sign their initial contracts
     */
    public void makeInitialUpdates() {
        chooseProducersForDistributors();
        refreshDistributorsPrices();
        signContracts();
    }

    /**
     * Apply the start of month updates, which consist of: adding new consumers, updating
     * distributors' costs and signing of new contracts by consumers
     *
     * @param turn the current month, used for gathering the right monthly updates
     * @return true if the simulation ends (when all distributors are bankrupt), false otherwise
     */
    public Boolean startOfMonthUpdates(Integer turn) {
        updateConsumersAndDistributors(turn);

        return signContracts();
    }

    /**
     * Apply the end of month updates, which consist of: removing bankrupt consumer contracts,
     * updating the energy given by producers, choosing producers for distributors whose producers
     * changed prices, adding a record to all producers for the current month
     *
     * @param turn the current month, used to selecting the right monthly update and needed for
     *             adding the monthly stats record to producers
     */
    public void endOfMonthUpdates(Integer turn) {
        removeNewlyBankruptConsumersContracts();
        updateProducersCosts(turn);
        chooseProducersForDistributors();
        updateProducersMonthlyStats(turn);
    }
}
