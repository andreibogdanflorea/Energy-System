package business;

import entities.Consumer;
import entities.Distributor;

import java.util.List;

public final class Transactions {
    /**
     * Add monthly incomes to non-bankrupt consumers
     *
     * @param consumers the list of consumers to add salaries to
     */
    public void addSalaries(List<Consumer> consumers) {
        for (Consumer consumer : consumers) {
            if (consumer.getBankrupt()) {
                continue;
            }
            consumer.setBudget(Formulas.computeNewBudgetOf(consumer, consumer.getMonthlyIncome()));
        }
    }

    /**
     * Simulate the payments of all contracts between consumers and distributors
     * Makes consumers go bankrupt or makes them postpone payment if necessary
     * <p>
     * A consumer should pay their whole bill (current month contract price and overdue payment,
     * if applicable) and if they cannot, they will try to postpone the payment for a month.
     *
     * @param consumers the list of current consumers
     */
    public void payContracts(List<Consumer> consumers) {
        for (Consumer consumer : consumers) {
            if (consumer.getBankrupt()) {
                continue;
            }

            Integer totalCost = Formulas.getTotalConsumerCost(consumer);
            Distributor distributor = consumer.getContract().getDistributor();

            if (consumer.getBudget() < totalCost) {
                tryPostponing(consumer);
            } else {
                consumer.setBudget(Formulas.computeNewBudgetOf(consumer, -totalCost));
                distributor.setBudget(Formulas
                        .computeNewBudgetOf(distributor, consumer.getContract().getPrice()));
                if (consumer.hasDuePayment()) {
                    Distributor distributorOwed = consumer.getDebt().getDistributorOwed();
                    distributorOwed.setBudget(Formulas
                            .computeNewBudgetOf(distributorOwed,
                                    consumer.getDebt().getDuePayment()));
                    consumer.removeDebt();
                }
            }

            consumer.getContract().decreaseRemainedContractMonths();
        }
    }

    private void tryPostponing(final Consumer consumer) {
        if (!consumer.hasDuePayment()) {
            Integer contractPrice = consumer.getContract().getPrice();
            Integer duePayment = Formulas.computeDuePayment(contractPrice);
            consumer.postponePayment(duePayment, consumer.getContract().getDistributor());
            return;
        }

        Integer duePayment = consumer.getDebt().getDuePayment();
        Distributor distributorOwed = consumer.getDebt().getDistributorOwed();

        if (!consumer.getContract().getDistributor().equals(distributorOwed)) {
            if (consumer.getBudget() < duePayment) {
                consumer.setBankrupt(true);
                return;
            }

            consumer.setBudget(Formulas.computeNewBudgetOf(consumer, -duePayment));
            distributorOwed.setBudget(Formulas.computeNewBudgetOf(distributorOwed, duePayment));

            consumer.postponePayment(
                    Formulas.computeDuePayment(consumer.getContract().getPrice()),
                    consumer.getContract().getDistributor());
            return;
        }

        consumer.setBankrupt(true);
    }

    /**
     * Simulate the payments of all distributors
     * Makes a distributor bankrupt if they cannot pay
     *
     * @param distributors the list of distributors that pay their costs
     */
    public void distributorPayments(List<Distributor> distributors) {
        for (Distributor distributor : distributors) {
            if (distributor.getBankrupt()) {
                continue;
            }

            Integer cost = Formulas.computeDistributorPayment(distributor);
            distributor.setBudget(Formulas.computeNewBudgetOf(distributor, -cost));

            if (distributor.getBudget() < 0) {
                distributor.setBankrupt(true);
                Distributor.ContractsInfo contractsInfo = distributor.getContractsInfo();

                contractsInfo.getContracts().forEach(contract ->
                        contract.getConsumer().setContract(null));
                contractsInfo.getContracts().clear();
            }
        }
    }
}
