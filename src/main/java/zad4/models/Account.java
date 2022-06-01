package zad4.models;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name="\"ACCOUNT\"") // konfilkt ze slowem kluczowym SQL w H2 w wersjach 2.x., wymagane escapowanie
@NamedQueries({
        @NamedQuery(name="findByNameAndAdresss",
                query="SELECT b FROM Account b WHERE b.Address=?1 AND b.Name=?2"),
        @NamedQuery(name="findByNameStartsWith", query="SELECT b FROM Account b WHERE b.Name LIKE ?1"),
        @NamedQuery(name="findByBalance",
                query="SELECT b FROM Account b where b.Amount between ?1 and ?2"),
        @NamedQuery(name="findWithMaxBalance",
                query="SELECT b FROM Account b WHERE b.Amount = (SELECT max(a.Amount)" +
                        "FROM Account a)"),
        @NamedQuery(name="findWithNoOperationHistory",
                query="SELECT b FROM Account b WHERE b.id NOT IN (SELECT DISTINCT o.account.id " +
                        "FROM AccountOperation o)"),
        @NamedQuery(name="findWithMostOperations",
                query="select b " +
                        "from Account b " +
                        "where b.id IN (SELECT o.account.id " +
                        "FROM AccountOperation o " +
                        "GROUP BY o.account.id " +
                        "HAVING COUNT(o.account.id) >= all( select count( c.account.id ) "+
                        "from AccountOperation c " +
                        "group by c.account.id ) )")
})
public class Account extends AbstractModel {

    @Basic
    @Column(unique = true)
    private String Name;

    @Basic
    @Column(name = "address")
    private String Address;

    @Basic
    @Column(name = "amount")
    private BigDecimal Amount;

    @OneToMany(mappedBy = "account", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<AccountOperation> operations = new LinkedList<>();

    public Account() {
    }

    public Account(String name, String address, BigDecimal amount) {
        this.Name = name;
        this.Address = address;
        this.Amount = amount;
    }

    public Account(String name, String address) {
        this.Name = name;
        this.Address = address;
        this.Amount = BigDecimal.valueOf(0);
    }

    public void addOperation(AccountOperation operation) {
        operations.add(operation);
        operation.setAccount(this);
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public BigDecimal getAmount() {
        return Amount;
    }

    public void setAmount(BigDecimal amount) {
        this.Amount = amount;
    }

    public List<AccountOperation> getOperations() {
        return operations;
    }

    public void setOperations(List<AccountOperation> operations) {
        this.operations = operations;
    }
}
