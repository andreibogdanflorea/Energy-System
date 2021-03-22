package entities;

import business.Contract;
import fileio.ConsumerInput;

/**
 * Class used for modeling a Consumer
 * Contains their monthly income, their contract and their debt
 */
public final class Consumer extends BusinessEntity {
    private final Integer monthlyIncome;
    private final Debt debt;
    private Contract contract;

    public Consumer(final ConsumerInput input) {
        super(input.getId(), input.getInitialBudget());
        monthlyIncome = input.getMonthlyIncome();
        debt = new Debt(0, null);
    }

    public Integer getMonthlyIncome() {
        return monthlyIncome;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(final Contract contract) {
        this.contract = contract;
    }

    public Debt getDebt() {
        return debt;
    }


    /**
     * Checks whether or not the consumer has due payment from the previous month
     *
     * @return true if the consumer has to pay from the previous month, false otherwise
     */
    public Boolean hasDuePayment() {
        return debt.duePayment != 0;
    }


    /**
     * Sign the contract with the given distributor
     *
     * @param distributor the distributor to sign the contract with
     */
    public void signContractWith(final Distributor distributor) {
        // if the consumer has 0 months left on the contract
        if (contract != null) {
            Distributor oldDistributor = getContract().getDistributor();
            oldDistributor.getContractsInfo().getContracts().remove(getContract());
        }

        Distributor.ContractsInfo contractsInfo = distributor.getContractsInfo();
        contract = new Contract(this, distributor,
                contractsInfo.getContractLength(), contractsInfo.getContractPrice());
        contractsInfo.getContracts().add(contract);
    }

    /**
     * Postpones the payment of a bill by a consumer
     *
     * @param duePayment      the amount of money to pay next month
     * @param distributorOwed the distributor that is owed the money
     */
    public void postponePayment(final Integer duePayment, final Distributor distributorOwed) {
        debt.duePayment = duePayment;
        debt.distributorOwed = distributorOwed;
    }

    /**
     * Removes the debt of the consumer by setting their postponed payment to 0
     */
    public void removeDebt() {
        debt.duePayment = 0;
        debt.distributorOwed = null;
    }

    /**
     * Class used for modeling a debt of a consumer; composed of the overdue payment
     * and the distributor that is owed the money
     */
    public static final class Debt {
        private Integer duePayment;
        private Distributor distributorOwed;

        public Debt(final Integer duePayment,
                    final Distributor distributorOwed) {
            this.duePayment = duePayment;
            this.distributorOwed = distributorOwed;
        }

        public Integer getDuePayment() {
            return duePayment;
        }

        public Distributor getDistributorOwed() {
            return distributorOwed;
        }
    }
}
