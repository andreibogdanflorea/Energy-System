package business;

import entities.BusinessEntity;
import entities.Consumer;
import entities.Distributor;
import entities.Producer;

/**
 * Helper class that is used for computing different formulas
 */
public final class Formulas {

    private Formulas() {
    }

    /**
     * Computes the price a consumer will have to pay in case they
     * postpone the payment of the contract price
     *
     * @param contractPrice the price of the customer's contract
     * @return the sum that the customer will have to pay the following month
     */
    public static Integer computeDuePayment(final Integer contractPrice) {
        return Math.toIntExact(
                Math.round(Math.floor(Constants.ADDITIONAL_PAYMENT_RATIO * contractPrice))
        );
    }

    /**
     * Computes the price the distributor has to pay at the end of the month
     *
     * @param distributor the distributor that pays
     * @return the price to pay
     */
    public static Integer computeDistributorPayment(final Distributor distributor) {
        Distributor.Costs costs = distributor.getCosts();
        return costs.getInfrastructureCost()
                + costs.getProductionCost() * distributor.getNumberOfConsumers();
    }

    /**
     * Computes the new budget of the energy entity after applying the value
     *
     * @param entity the Energy entity to compute the new budget of
     * @param value  the value to change the budget by; can be positive or negative
     * @return the new budget
     */
    public static Integer computeNewBudgetOf(final BusinessEntity entity, final Integer value) {
        return entity.getBudget() + value;
    }

    /**
     * Get the total cost of that the consumer has to pay in a month
     * The cost consists of that month's contract price and the price that the consumer
     * has postponed
     *
     * @param consumer the consumer whose payment cost is computed
     * @return the sum the consumer has to pay
     */
    public static Integer getTotalConsumerCost(final Consumer consumer) {
        return consumer.getContract().getPrice() + consumer.getDebt().getDuePayment();
    }

    /**
     * Computes the price of a distributor's contract
     *
     * @param distributor the distributor whose contract price is computed
     * @return the price of the contract
     */
    public static Integer computePriceOfContract(final Distributor distributor) {
        Integer profit = computeProfit(distributor);
        Distributor.Costs costs = distributor.getCosts();

        if (distributor.getNumberOfConsumers().equals(0)) {
            return costs.getInfrastructureCost()
                    + costs.getProductionCost()
                    + profit;
        }

        return Math.toIntExact(
                Math.round(Math.floor(
                        costs.getInfrastructureCost().floatValue()
                                / distributor.getNumberOfConsumers()
                                + costs.getProductionCost()
                                + profit
                ))
        );
    }

    /**
     * Computes the profit of the distributor according to their production cost
     *
     * @param distributor the distributor's profit to compute
     * @return the value of the profit
     */
    public static Integer computeProfit(final Distributor distributor) {
        return Math.toIntExact(
                Math.round(Math.floor(Constants.PROFIT_RATIO
                        * distributor.getCosts().getProductionCost()))
        );
    }

    /**
     * Computes the cost of production for a distributor, based on its list of chosen producers
     *
     * @param distributor the distributor to compute the cost for
     * @return the production cost
     */
    public static Integer computeProductionCost(final Distributor distributor) {
        double cost = 0.0;
        for (Producer producer : distributor.getProducersInfo().getProducers()) {
            Producer.EnergyInfo energyInfo = producer.getEnergyInfo();
            cost += energyInfo.getEnergyPerDistributor() * energyInfo.getPriceKW();
        }

        return Math.toIntExact(Math.round(Math.floor(cost * Constants.PRODUCTION_COST_RATIO)));
    }

    private static final class Constants {
        static final Double ADDITIONAL_PAYMENT_RATIO = 1.2;
        static final Double PROFIT_RATIO = 0.2;
        static final Double PRODUCTION_COST_RATIO = 0.1;

        private Constants() {
        }
    }
}
