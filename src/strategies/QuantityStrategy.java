package strategies;

import entities.Producer;

/**
 * Producers are prioritized by quantity
 */
public final class QuantityStrategy extends ChooseProducersStrategy {
    @Override
    public int compare(final Producer o1, final Producer o2) {
        Producer.EnergyInfo energyInfo1 = o1.getEnergyInfo();
        Producer.EnergyInfo energyInfo2 = o2.getEnergyInfo();

        if (energyInfo1.getEnergyPerDistributor().equals(energyInfo2.getEnergyPerDistributor())) {
            return o1.getId().compareTo(o2.getId());
        }

        return energyInfo2.getEnergyPerDistributor()
                .compareTo(energyInfo1.getEnergyPerDistributor());
    }
}
