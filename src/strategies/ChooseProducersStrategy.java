package strategies;

import entities.Distributor;
import entities.Producer;

import java.util.ArrayList;
import java.util.List;


/**
 * Generic strategy for choosing producers, it sorts the list of producers by a criteria
 * implemented by the concrete strategies and selects the best producers that give the
 * necessary quantity of energy
 */
public abstract class ChooseProducersStrategy {
    /**
     * Sorts the list of producers by a criteria specific to a certain strategy
     * in order to meet the required energy quantity for the distributor
     *
     * @param distributor the distributor to choose the list of producers for
     * @param producers   the list of producers currently in the database
     */
    public final void chooseProducersFor(final Distributor distributor,
                                         final List<Producer> producers) {
        Distributor.ProducersInfo producersInfo = distributor.getProducersInfo();

        for (Producer producer : producersInfo.getProducers()) {
            producer.deleteObserver(distributor);
            producer.getDistributorsInfo().getCurrentDistributors().remove(distributor);
        }
        producersInfo.getProducers().clear();

        List<Producer> copyProducers =
                new ArrayList<>(producers);
        copyProducers.sort(this::compare);

        int currentEnergy = 0;

        for (Producer producer : copyProducers) {
            Producer.DistributorsInfo distributorsInfo = producer.getDistributorsInfo();

            if (distributorsInfo.getMaxDistributors()
                    .equals(distributorsInfo.getCurrentDistributors().size())) {
                continue;
            }

            currentEnergy += producer.getEnergyInfo().getEnergyPerDistributor();
            producer.addObserver(distributor);
            distributorsInfo.getCurrentDistributors().add(distributor);
            producersInfo.getProducers().add(producer);
            if (currentEnergy > producersInfo.getEnergyNeededKW()) {
                break;
            }
        }
    }

    /**
     * Sorting criteria used differently by strategies
     *
     * @param o1 the first producer
     * @param o2 the second producer
     * @return a negative integer, zero, or a positive integer as the first producer is less than,
     * equal to, or greater than the second producer.
     */
    public abstract int compare(Producer o1, Producer o2);
}
