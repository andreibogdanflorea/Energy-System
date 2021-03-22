package fileio;

public final class ConsumerInput implements EnergyEntityInput {
    private Integer id;
    private Integer initialBudget;
    private Integer monthlyIncome;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getInitialBudget() {
        return initialBudget;
    }

    public void setInitialBudget(final Integer initialBudget) {
        this.initialBudget = initialBudget;
    }

    public Integer getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(final Integer monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }
}
