package simulation;

import business.Transactions;
import fileio.Input;

/**
 * Engine of the simulation; simulates the initial round and also the next rounds
 * Contains a reference to the database used for the current simulation
 */
public final class MonthlySimulation {
    private final SimulationDatabase database;
    private final Integer numberOfTurns;

    public MonthlySimulation(final Input input) {
        numberOfTurns = input.getNumberOfTurns();
        database = new SimulationDatabase(input.getInitialData(),
                input.getMonthlyUpdates());
    }

    public SimulationDatabase getDatabase() {
        return database;
    }

    /**
     * Entry point; starts the initial round of the simulation and also the following rounds
     * <p>
     * Initial round consists of: making the initial updates to the database and simulating
     * a month of transactions
     * <p>
     * The following rounds consist of: the start of month updates, the simulation of a month
     * of transactions, and the end of month updates
     * <p>
     * A month of transactions refers to consumers receiving salaries, paying their contracts
     * to the distributors, who in turn make their payments
     */
    public void start() {
        simulateInitialRound();
        simulateTurns();
    }

    private void simulateInitialRound() {
        database.makeInitialUpdates();
        simulateMonthOfTransactions();
    }

    private void simulateTurns() {
        for (int turn = 0; turn < numberOfTurns; turn++) {
            Boolean allDistributorsBankrupt = simulateTurn(turn);
            if (allDistributorsBankrupt) {
                return;
            }
        }
    }

    private Boolean simulateTurn(final Integer turn) {
        Boolean allDistributorsBankrupt = database.startOfMonthUpdates(turn);
        if (allDistributorsBankrupt) {
            return true;
        }

        simulateMonthOfTransactions();

        database.endOfMonthUpdates(turn);

        return false;
    }

    private void simulateMonthOfTransactions() {
        Transactions transactions = new Transactions();
        transactions.addSalaries(database.getConsumers());
        transactions.payContracts(database.getConsumers());
        transactions.distributorPayments(database.getDistributors());
    }
}
