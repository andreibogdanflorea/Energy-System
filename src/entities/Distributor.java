package entities;

import business.Contract;
import business.Formulas;
import fileio.DistributorInput;
import strategies.ChooseProducersStrategy;
import strategies.EnergyChoiceStrategyType;
import strategies.StrategyFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Class for modeling a distributor, contains information about their contracts,
 * about the production and infrastructure costs and about their producers
 */
public final class Distributor extends BusinessEntity implements Observer {
    private final ContractsInfo contractsInfo;
    private final Costs costs;
    private final ProducersInfo producersInfo;

    public Distributor(final DistributorInput input) {
        super(input.getId(), input.getInitialBudget());
        contractsInfo = new ContractsInfo(input.getContractLength());
        costs = new Costs(input.getInitialInfrastructureCost());
        producersInfo = new ProducersInfo(input.getEnergyNeededKW(), input.getProducerStrategy());
    }

    public ContractsInfo getContractsInfo() {
        return contractsInfo;
    }

    public Costs getCosts() {
        return costs;
    }

    public ProducersInfo getProducersInfo() {
        return producersInfo;
    }

    /**
     * Get the current number of consumers of the distributor
     *
     * @return an Integer specifying the current number of consumers
     */
    public Integer getNumberOfConsumers() {
        return contractsInfo.contracts.size();
    }

    /**
     * Recomputes the cost of the distributor's contract
     */
    public void refreshPrices() {
        costs.productionCost = Formulas.computeProductionCost(this);
        contractsInfo.contractPrice = Formulas.computePriceOfContract(this);
    }

    /**
     * When one of the distributor's producers changes their price, the distributor needs to
     * choose their producers next month
     *
     * @param o   the producer that changes their price
     * @param arg null in this case
     */
    @Override
    public void update(final Observable o, final Object arg) {
        producersInfo.needToUpdateProducers = true;
    }

    /**
     * Information about the distributor's contracts, such as current contracts, contract length
     * and price
     */
    public static final class ContractsInfo {
        private final Integer contractLength;
        private final List<Contract> contracts;
        private Integer contractPrice;

        public ContractsInfo(Integer contractLength) {
            this.contractLength = contractLength;
            contracts = new ArrayList<>();
        }

        public Integer getContractPrice() {
            return contractPrice;
        }

        public Integer getContractLength() {
            return contractLength;
        }

        public List<Contract> getContracts() {
            return contracts;
        }
    }

    /**
     * Information about the distributor's infrastructure and production costs
     */
    public static final class Costs {
        private Integer infrastructureCost;
        private Integer productionCost;

        public Costs(Integer infrastructureCost) {
            this.infrastructureCost = infrastructureCost;
        }

        public Integer getInfrastructureCost() {
            return infrastructureCost;
        }

        public void setInfrastructureCost(Integer infrastructureCost) {
            this.infrastructureCost = infrastructureCost;
        }

        public Integer getProductionCost() {
            return productionCost;
        }
    }

    /**
     * Information regarding the producers, such as energy needed, the strategy used when
     * choosing producers, and the list of current producers that give energy to the distributor
     */
    public static final class ProducersInfo {
        private final Integer energyNeededKW;
        private final List<Producer> producers;
        private final EnergyChoiceStrategyType strategyType;
        private final ChooseProducersStrategy producersStrategy;
        private Boolean needToUpdateProducers;

        public ProducersInfo(Integer energyNeededKW, EnergyChoiceStrategyType strategyType) {
            this.energyNeededKW = energyNeededKW;
            this.strategyType = strategyType;
            producers = new ArrayList<>();
            producersStrategy = StrategyFactory.getInstance().createStrategy(strategyType);
            needToUpdateProducers = true;
        }

        public List<Producer> getProducers() {
            return producers;
        }

        public Integer getEnergyNeededKW() {
            return energyNeededKW;
        }

        public EnergyChoiceStrategyType getStrategyType() {
            return strategyType;
        }

        public ChooseProducersStrategy getProducersStrategy() {
            return producersStrategy;
        }

        public Boolean getNeedToUpdateProducers() {
            return needToUpdateProducers;
        }

        public void setNeedToUpdateProducers(final Boolean needToUpdateProducers) {
            this.needToUpdateProducers = needToUpdateProducers;
        }
    }
}
