package fileio;

import business.Contract;
import entities.Consumer;
import entities.Distributor;
import entities.EnergyType;
import entities.Producer;
import simulation.SimulationDatabase;
import strategies.EnergyChoiceStrategyType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Output {
    private List<ConsumerOutput> consumers;
    private List<DistributorOutput> distributors;
    private List<ProducerOutput> energyProducers;

    public Output(SimulationDatabase database) {
        consumers = new ArrayList<>();
        distributors = new ArrayList<>();
        energyProducers = new ArrayList<>();

        for (Consumer consumer : database.getConsumers()) {
            consumers.add(new ConsumerOutput(consumer));
        }

        for (Distributor distributor : database.getDistributors()) {
            distributors.add(new DistributorOutput(distributor));
        }

        for (Producer producer : database.getProducers()) {
            energyProducers.add(new ProducerOutput(producer));
        }
    }

    public List<ConsumerOutput> getConsumers() {
        return consumers;
    }

    public void setConsumers(final List<ConsumerOutput> consumers) {
        this.consumers = consumers;
    }

    public List<DistributorOutput> getDistributors() {
        return distributors;
    }

    public void setDistributors(final List<DistributorOutput> distributors) {
        this.distributors = distributors;
    }

    public List<ProducerOutput> getEnergyProducers() {
        return energyProducers;
    }

    public void setEnergyProducers(List<ProducerOutput> energyProducers) {
        this.energyProducers = energyProducers;
    }
}

class ConsumerOutput {
    private Integer id;
    private Boolean isBankrupt;
    private Integer budget;

    ConsumerOutput(final Consumer consumer) {
        id = consumer.getId();
        budget = consumer.getBudget();
        isBankrupt = consumer.getBankrupt();
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(final Integer budget) {
        this.budget = budget;
    }

    public Boolean getIsBankrupt() {
        return isBankrupt;
    }

    public void setIsBankrupt(final Boolean bankrupt) {
        isBankrupt = bankrupt;
    }
}

class DistributorOutput {
    private Integer id;
    private Integer energyNeededKW;
    private Integer contractCost;
    private Integer budget;
    private EnergyChoiceStrategyType producerStrategy;
    private Boolean isBankrupt;
    private List<ContractOutput> contracts;

    DistributorOutput(final Distributor distributor) {
        id = distributor.getId();
        energyNeededKW = distributor.getProducersInfo().getEnergyNeededKW();
        contractCost = distributor.getContractsInfo().getContractPrice();
        budget = distributor.getBudget();
        producerStrategy = distributor.getProducersInfo().getStrategyType();
        isBankrupt = distributor.getBankrupt();
        contracts = new ArrayList<>();
        for (Contract contract : distributor.getContractsInfo().getContracts()) {
            contracts.add(new ContractOutput(contract));
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEnergyNeededKW() {
        return energyNeededKW;
    }

    public void setEnergyNeededKW(Integer energyNeededKW) {
        this.energyNeededKW = energyNeededKW;
    }

    public Integer getContractCost() {
        return contractCost;
    }

    public void setContractCost(Integer contractCost) {
        this.contractCost = contractCost;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public EnergyChoiceStrategyType getProducerStrategy() {
        return producerStrategy;
    }

    public void setProducerStrategy(EnergyChoiceStrategyType producerStrategy) {
        this.producerStrategy = producerStrategy;
    }

    public Boolean getIsBankrupt() {
        return isBankrupt;
    }

    public void setIsBankrupt(Boolean bankrupt) {
        isBankrupt = bankrupt;
    }

    public List<ContractOutput> getContracts() {
        return contracts;
    }

    public void setContracts(List<ContractOutput> contracts) {
        this.contracts = contracts;
    }
}

class ContractOutput {
    private Integer consumerId;
    private Integer price;
    private Integer remainedContractMonths;

    ContractOutput(final Contract contract) {
        consumerId = contract.getConsumer().getId();
        price = contract.getPrice();
        remainedContractMonths = contract.getRemainedContractMonths();
    }

    public Integer getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(final Integer consumerId) {
        this.consumerId = consumerId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(final Integer price) {
        this.price = price;
    }

    public Integer getRemainedContractMonths() {
        return remainedContractMonths;
    }

    public void setRemainedContractMonths(final Integer remainedContractMonths) {
        this.remainedContractMonths = remainedContractMonths;
    }
}

class ProducerOutput {
    private Integer id;
    private Integer maxDistributors;
    private Double priceKW;
    private EnergyType energyType;
    private Integer energyPerDistributor;
    private List<MonthlyStatsOutput> monthlyStats;

    ProducerOutput(final Producer producer) {
        id = producer.getId();
        maxDistributors = producer.getDistributorsInfo().getMaxDistributors();
        priceKW = producer.getEnergyInfo().getPriceKW();
        energyType = producer.getEnergyInfo().getEnergyType();
        energyPerDistributor = producer.getEnergyInfo().getEnergyPerDistributor();
        monthlyStats = new ArrayList<>();
        producer.getMonthlyStats()
                .forEach(monthlyStat -> monthlyStats.add(new MonthlyStatsOutput(monthlyStat)));
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
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

    public EnergyType getEnergyType() {
        return energyType;
    }

    public void setEnergyType(final EnergyType energyType) {
        this.energyType = energyType;
    }

    public Integer getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    public void setEnergyPerDistributor(final Integer energyPerDistributor) {
        this.energyPerDistributor = energyPerDistributor;
    }

    public List<MonthlyStatsOutput> getMonthlyStats() {
        return monthlyStats;
    }

    public void setMonthlyStats(final List<MonthlyStatsOutput> monthlyStats) {
        this.monthlyStats = monthlyStats;
    }
}

class MonthlyStatsOutput {
    private Integer month;
    private List<Integer> distributorsIds;

    MonthlyStatsOutput(final Producer.MonthlyStats monthlyStats) {
        month = monthlyStats.getMonth();
        distributorsIds = monthlyStats.getDistributorsIds();
        Collections.sort(distributorsIds);
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(final Integer month) {
        this.month = month;
    }

    public List<Integer> getDistributorsIds() {
        return distributorsIds;
    }

    public void setDistributorsIds(List<Integer> distributorsIds) {
        this.distributorsIds = distributorsIds;
    }
}
