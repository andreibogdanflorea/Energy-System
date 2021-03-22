package entities;


import java.util.Objects;

/**
 * Abstract class that the Consumer and Distributor extend
 */
public abstract class BusinessEntity implements EnergyEntity {
    private final Integer id;
    private Boolean isBankrupt;
    private Integer budget;

    public BusinessEntity(final Integer id, final Integer budget) {
        this.id = id;
        this.budget = budget;
        isBankrupt = false;
    }

    public final Integer getId() {
        return id;
    }

    public final Integer getBudget() {
        return budget;
    }

    public final void setBudget(final Integer budget) {
        this.budget = budget;
    }

    public final Boolean getBankrupt() {
        return isBankrupt;
    }

    public final void setBankrupt(final Boolean bankrupt) {
        isBankrupt = bankrupt;
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BusinessEntity that = (BusinessEntity) o;
        return id.equals(that.id)
                && Objects.equals(isBankrupt, that.isBankrupt)
                && Objects.equals(budget, that.budget);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id, isBankrupt, budget);
    }
}
