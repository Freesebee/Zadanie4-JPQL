package zad4.models;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="\"ACCOUNTOPERATION\"")
@NamedQuery(name="AccountOperation.findByCreationDate", query="select o from AccountOperation o where o.creationDate >= ?1 and o.creationDate <= ?2")
@NamedQuery(name="AccountOperation.findTypeOfMostFrequentOperation", query="SELECT o.type " +
        "FROM AccountOperation o "+
        "WHERE o.account.id=?1 " +
        "GROUP BY o.type " +
        "HAVING COUNT(o.id) >= ALL( " +
            "SELECT COUNT(a.id)" +
            "FROM AccountOperation a " +
            "GROUP BY a.type )"
)
public class AccountOperation extends AbstractModel {

    @ManyToOne
    private Account account;
    private BigDecimal amount;
    private OperationType type;

    public AccountOperation() {
    }

    public AccountOperation(Account account, BigDecimal amount, OperationType type) {
        this.account = account;
        this.amount = amount;
        this.type = type;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public OperationType getType() {
        return type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }
}
