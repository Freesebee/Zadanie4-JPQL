package zad4;

import org.junit.jupiter.api.*;
import zad4.interfaces.IBank;
import zad4.models.Account;
import zad4.models.Bank;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static zad4.utils.JpaFactory.getEntityManager;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class BankTest {

    private IBank bank;

    @BeforeEach
    public void setup() {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.createQuery("delete from AccountOperation").executeUpdate();
        em.createQuery("delete from Account").executeUpdate();
        em.getTransaction().commit();
        em.close();

        bank = new Bank();
    }

    @Test
    public void createAccount_shouldCreateAccount() {

        Long id = bank.createAccount("Jan Nowak","ul Szkolna 17");

        assert id != null;
    }

    @Test
    public void findAccount_shouldReturnAccount() {

        Account match = new Account("Jan Nowak","ul Szkolna 17");
        match.setCreationDate(new Date(System.currentTimeMillis()));
        match.setModificationDate(new Date(System.currentTimeMillis()));

        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(match);
        em.getTransaction().commit();
        em.close();

        Account found = bank.findAccount(match.getId());

        assert found != null;
        assert found.getAmount().compareTo(match.getAmount()) == 0;
        assert Objects.equals(found.getAddress(), match.getAddress());
        assert Objects.equals(found.getName(), match.getName());
    }

    @Test
    public void findAccount_shouldReturnNull() {
        Account account = bank.findAccount(0L); //db is empty at the beggining

        assert account == null;
    }

    @Test
    public void deposit_shouldAddAmount() {
        Account match = new Account("Jan Nowak","ul Szkolna 17");
        match.setCreationDate(new Date(System.currentTimeMillis()));
        match.setModificationDate(new Date(System.currentTimeMillis()));
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(match);
        em.getTransaction().commit();
        em.close();

        bank.deposit(match.getId(), BigDecimal.valueOf(100));

        em = getEntityManager();
        Account account = em.find(Account.class, match.getId());
        em.close();

        assert account.getAmount().intValue() == 100;
    }

    @Test
    public void deposit_shouldThrowAccountIdException() {

        Throwable exception = assertThrows(Exception.class, () -> {
            bank.deposit((long) 0, BigDecimal.valueOf(100));
        });

        assert exception != null;
        assert exception instanceof IBank.AccountIdException;
    }

    @Test
    public void getBalance_shouldReturnBalance() {
        Account match = new Account("Jan Nowak","ul Szkolna 17");
        match.setCreationDate(new Date(System.currentTimeMillis()));
        match.setModificationDate(new Date(System.currentTimeMillis()));
        match.setAmount(new BigDecimal(100));
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(match);
        em.getTransaction().commit();
        em.close();

        BigDecimal balance = bank.getBalance(match.getId());

        assert balance != null;
        assert balance.intValue() == 100;
    }

    @Test
    public void getBalance_shouldThrowAccountIdException() {

        Throwable exception = assertThrows(Exception.class, () -> {
            bank.getBalance((long) 0);
        });

        assert exception != null;
        assert exception instanceof IBank.AccountIdException;
    }

    @Test
    public void withdraw_shouldReturnTakenAmount() {
        Account match = new Account("Jan Nowak","ul Szkolna 17");
        match.setCreationDate(new Date(System.currentTimeMillis()));
        match.setModificationDate(new Date(System.currentTimeMillis()));
        match.setAmount(new BigDecimal(100));
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(match);
        em.getTransaction().commit();
        em.close();

        bank.withdraw(match.getId(), BigDecimal.valueOf(100));

        em = getEntityManager();
        em.getTransaction().begin();
        Account account = em.find(Account.class, match.getId());
        em.close();

        assert account.getAmount().intValue() == 0;
    }

    @Test
    public void withdraw_shouldThrowAccountIdException() {
        Throwable exception = assertThrows(Exception.class, () -> {
            bank.withdraw((long) 0, BigDecimal.valueOf(100));
        });

        assert exception != null;
        assert exception instanceof IBank.AccountIdException;
    }

    @Test
    public void withdraw_shouldThrowInsufficientFundsException() {
        Account match = new Account("Jan Nowak","ul Szkolna 17");
        match.setCreationDate(new Date(System.currentTimeMillis()));
        match.setModificationDate(new Date(System.currentTimeMillis()));
        match.setAmount(new BigDecimal(100));
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(match);
        em.getTransaction().commit();
        em.close();

        Throwable exception = assertThrows(Exception.class, () -> {
            bank.withdraw(match.getId(), BigDecimal.valueOf(1000));
        });

        assert exception != null;
        assert exception instanceof IBank.InsufficientFundsException;
    }

    @Test
    public void transfer_shouldTransferAmount() {
        Account source = new Account("Jan Nowak","ul Szkolna 17");
        source.setCreationDate(new Date(System.currentTimeMillis()));
        source.setModificationDate(new Date(System.currentTimeMillis()));
        source.setAmount(new BigDecimal(100));

        Account destination = new Account("Adam Potmalnik","ul Wiejska 17");
        destination.setCreationDate(new Date(System.currentTimeMillis()));
        destination.setModificationDate(new Date(System.currentTimeMillis()));
        destination.setAmount(new BigDecimal(0));

        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(source);
        em.persist(destination);
        em.getTransaction().commit();
        em.close();

        bank.transfer(source.getId(), destination.getId(), BigDecimal.valueOf(100), "TEST");

        em = getEntityManager();
        em.getTransaction().begin();
        source = em.find(Account.class, source.getId());
        destination = em.find(Account.class, destination.getId());
        em.close();

        assert source.getAmount().intValue() == 0;
        assert destination.getAmount().intValue() == 100;
    }

    @Test
    public void transfer_shouldThrowAccountIdException() {

        Throwable exception = assertThrows(Exception.class, () -> {
            bank.withdraw((long) 0, BigDecimal.valueOf(100));
        });

        assert exception != null;
        assert exception instanceof IBank.AccountIdException;
    }

    @Test
    public void transfer_shouldThrowInsufficientFundsException() {
        Account match1 = new Account("Jan Nowak","ul Szkolna 17");
        match1.setCreationDate(new Date(System.currentTimeMillis()));
        match1.setModificationDate(new Date(System.currentTimeMillis()));
        match1.setAmount(new BigDecimal(100));

        Account match2 = new Account("Adam Potmalnik","ul Wiejska 17");
        match2.setCreationDate(new Date(System.currentTimeMillis()));
        match2.setModificationDate(new Date(System.currentTimeMillis()));
        match2.setAmount(new BigDecimal(0));

        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(match1);
        em.persist(match2);
        em.getTransaction().commit();
        em.close();

        Throwable exception = assertThrows(Exception.class, () -> {
            bank.transfer(match1.getId(), match2.getId(), BigDecimal.valueOf(1000), "TEST");
        });

        assert exception != null;
        assert exception instanceof IBank.InsufficientFundsException;
    }
}
