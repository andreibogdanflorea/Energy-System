package strategies;

import entities.Producer;

/**
 * Producers are prioritized by type of energy, then by price and then by quantity
 */
public final class GreenStrategy extends ChooseProducersStrategy {
    @Override
    public int compare(final Producer o1, final Producer o2) {
        Producer.EnergyInfo energyInfo1 = o1.getEnergyInfo();
        Producer.EnergyInfo energyInfo2 = o2.getEnergyInfo();

        if (energyInfo1.getEnergyType().isRenewable()
                && energyInfo2.getEnergyType().isRenewable()) {
            if (energyInfo1.getPriceKW().equals(energyInfo2.getPriceKW())) {
                if (energyInfo1.getEnergyPerDistributor()
                        .equals(energyInfo2.getEnergyPerDistributor())) {
                    return o1.getId().compareTo(o2.getId());
                }

                return energyInfo2.getEnergyPerDistributor()
                        .compareTo(energyInfo1.getEnergyPerDistributor());
            }

            return energyInfo1.getPriceKW().compareTo(energyInfo2.getPriceKW());
        }

        if (energyInfo1.getEnergyType().isRenewable()) {
            return -1;
        }

        return 1;
    }
}
