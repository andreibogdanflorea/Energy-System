package business;

import entities.Consumer;
import entities.Distributor;

/**
 * Class for modeling a contract between a consumer and a distributor
 */
public final class Contract {
    private final Consumer consumer;
    private final Distributor distributor;
    private final Integer price;
    private Integer remainedContractMonths;

    public Contract(final Consumer consumer,
                    final Distributor distributor,
                    final Integer remainedContractMonths,
                    final Integer price) {
        this.consumer = consumer;
        this.distributor = distributor;
        this.remainedContractMonths = remainedContractMonths;
        this.price = price;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getRemainedContractMonths() {
        return remainedContractMonths;
    }

    /**
     * Decreases number of remaining months in the contract
     */
    public void decreaseRemainedContractMonths() {
        remainedContractMonths--;
    }

    /**
     * Checks whether or not the contract has just ended
     *
     * @return true if the remaining contract months are 0, false otherwise
     */
    public Boolean ended() {
        return remainedContractMonths.equals(0);
    }
}
