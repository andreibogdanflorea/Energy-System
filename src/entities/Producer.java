package entities;

import fileio.ProducerInput;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Class used for modeling a producer, contains information regarding the provided energy,
 * the current distributors that get energy from the producer and also maintains a list of
 * monthly stats
 */
public final class Producer extends Observable implements EnergyEntity {
    private final Integer id;
    private final EnergyInfo energyInfo;
    private final DistributorsInfo distributorsInfo;
    private final List<MonthlyStats> monthlyStats;


    public Producer(final ProducerInput producerInput) {
        id = producerInput.getId();
        monthlyStats = new ArrayList<>();
        distributorsInfo = new DistributorsInfo(producerInput.getMaxDistributors());
        energyInfo = new EnergyInfo(producerInput.getPriceKW(),
                producerInput.getEnergyType(),
                producerInput.getEnergyPerDistributor());
    }

    public Integer getId() {
        return id;
    }

    public EnergyInfo getEnergyInfo() {
        return energyInfo;
    }

    public DistributorsInfo getDistributorsInfo() {
        return distributorsInfo;
    }

    public List<MonthlyStats> getMonthlyStats() {
        return monthlyStats;
    }

    /**
     * Change the quantity of energy per distributor
     * Notifies all distributors that get energy from this producer
     *
     * @param energyPerDistributor the new value of the energy per distributor
     */
    public void changeEnergyPerDistributor(final Integer energyPerDistributor) {
        energyInfo.energyPerDistributor = energyPerDistributor;
        setChanged();
        notifyObservers();
    }

    /**
     * Add record to the list of monthly stats of this producer
     *
     * @param turn the month
     */
    public void addStatsRecord(final Integer turn) {
        monthlyStats.add(new MonthlyStats(turn, distributorsInfo.currentDistributors));
    }

    /**
     * Information about the energy offered by the producer; includes the price, the type
     * and the quantity of the energy
     */
    public static final class EnergyInfo {
        private final Double priceKW;
        private final EnergyType energyType;
        private Integer energyPerDistributor;

        public EnergyInfo(Double priceKW, EnergyType energyType, Integer energyPerDistributor) {
            this.priceKW = priceKW;
            this.energyType = energyType;
            this.energyPerDistributor = energyPerDistributor;
        }

        public Double getPriceKW() {
            return priceKW;
        }

        public EnergyType getEnergyType() {
            return energyType;
        }

        public Integer getEnergyPerDistributor() {
            return energyPerDistributor;
        }
    }

    /**
     * Information about the distributor that get energy from the producer; includes
     * the maximum number of distributors that can get energy from the producer and
     * a list of current distributors that get energy from the producer
     */
    public static final class DistributorsInfo {
        private final Integer maxDistributors;
        private final List<Distributor> currentDistributors;

        public DistributorsInfo(Integer maxDistributors) {
            this.maxDistributors = maxDistributors;
            currentDistributors = new ArrayList<>();
        }

        public Integer getMaxDistributors() {
            return maxDistributors;
        }

        public List<Distributor> getCurrentDistributors() {
            return currentDistributors;
        }
    }

    /**
     * Class used for modeling a single record from the monthly stats list;
     * consists of the distributors that get energy from the producer in the given month
     */
    public static final class MonthlyStats {
        private final Integer month;
        private final List<Integer> distributorsIds;

        public MonthlyStats(Integer month, List<Distributor> distributors) {
            this.month = month;
            distributorsIds = new ArrayList<>();
            distributors.forEach(distributor -> distributorsIds.add(distributor.getId()));
        }

        public Integer getMonth() {
            return month;
        }

        public List<Integer> getDistributorsIds() {
            return distributorsIds;
        }
    }
}
