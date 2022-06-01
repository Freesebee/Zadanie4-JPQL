package zad4.models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name="\"TRANSFEROPERATION\"")
public class TransferOperation extends AccountOperation{

    private String title;

    @ManyToOne
    private Account otherAccount;

    public TransferOperation() {
        setType(OperationType.TRANSFER);
    }

    public TransferOperation(Account account, BigDecimal amount, String title, Account otherAccount) {
        super(account, amount, OperationType.TRANSFER);
        this.title = title;
        this.otherAccount = otherAccount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Account getOtherAccount() {
        return otherAccount;
    }

    public void setOtherAccount(Account otherAccount) {
        this.otherAccount = otherAccount;
    }
}
