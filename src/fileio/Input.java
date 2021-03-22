package fileio;

import java.util.List;

public final class Input {
    private Integer numberOfTurns;
    private InitialDataInput initialData;
    private List<MonthlyUpdateInput> monthlyUpdates;

    public Integer getNumberOfTurns() {
        return numberOfTurns;
    }

    public void setNumberOfTurns(final Integer numberOfTurns) {
        this.numberOfTurns = numberOfTurns;
    }

    public InitialDataInput getInitialData() {
        return initialData;
    }

    public void setInitialData(final InitialDataInput initialData) {
        this.initialData = initialData;
    }

    public List<MonthlyUpdateInput> getMonthlyUpdates() {
        return monthlyUpdates;
    }

    public void setMonthlyUpdates(final List<MonthlyUpdateInput> monthlyUpdates) {
        this.monthlyUpdates = monthlyUpdates;
    }
}
