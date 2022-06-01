package zad4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zad4.dao.AccountDao;
import zad4.interfaces.IAccountDao;
import zad4.models.Account;
import zad4.models.AccountOperation;
import zad4.models.OperationType;
import zad4.models.TransferOperation;

import javax.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static zad4.utils.JpaFactory.getEntityManager;

public class AccountTest {

    IAccountDao dao = new AccountDao();

    @BeforeEach
    public void setup() {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.createQuery("delete from AccountOperation").executeUpdate();
        em.createQuery("delete from Account").executeUpdate();
        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void  findByNameAndAdresss_shouldReturnOneAccount() {

        Account expected = new Account("Test","ul. Testów", new BigDecimal(0L));

        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(expected);
        em.getTransaction().commit();
        em.close();

        List<Account> results = dao.findByNameAndAdresss(expected.getName(), expected.getAddress());

        assert results != null;
        assert results.size() == 1;
        assert Objects.equals(expected.getName(), results.get(0).getName());
        assert Objects.equals(expected.getAddress(), results.get(0).getAddress());
    }

    @Test
    public void  findByNameAndAdresss_shouldReturnEmptyAccountList() {

        Account expected = new Account("Test","ul. Testów", new BigDecimal(0L));

        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(expected);
        em.getTransaction().commit();
        em.close();

        List<Account> results = dao.findByNameAndAdresss("WrongName", expected.getAddress());

        assert results == null;
    }

    @Test
    public void findByNameStartsWith_shouldReturnTwoAccounts() {
        String prefix = "expected";

        Account expected1 = new Account(prefix+"1","ul. Testów", new BigDecimal(0L));
        Account expected2 = new Account(prefix+"2","ul. Testów", new BigDecimal(0L));
        Account additional1 = new Account("additional1","ul. Testów", new BigDecimal(0L));
        Account additional2 = new Account("additional2","ul. Testów", new BigDecimal(0L));

        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(expected1);
        em.persist(expected2);
        em.persist(additional1);
        em.persist(additional2);
        em.getTransaction().commit();
        em.close();

        List<Account> results = dao.findByNameStartsWith(prefix);

        assert results != null;
        assert results.size() == 2;
        assert results.get(0).getName().startsWith(prefix);
        assert results.get(1).getName().startsWith(prefix);
    }

    @Test
    public void findByBalance_shouldReturnTwoAccounts() {

        BigDecimal minValue = new BigDecimal(10L), maxValue = new BigDecimal(50L);

        Account additional1 = new Account("additional1","ul. Testów", new BigDecimal(9L));
        Account expected1 = new Account("expected1","ul. Testów", minValue);
        Account expected2 = new Account("expected2","ul. Testów", maxValue);
        Account additional2 = new Account("additional2","ul. Testów", new BigDecimal(51L));

        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(expected1);
        em.persist(expected2);
        em.persist(additional1);
        em.persist(additional2);
        em.getTransaction().commit();
        em.close();

        List<Account> results = dao.findByBalance(minValue, maxValue);

        assert results != null;
        assert results.size() == 2;
        assert results.get(0).getAmount().compareTo(minValue) >= 0 && results.get(0).getAmount().compareTo(maxValue) <= 0;
        assert results.get(1).getAmount().compareTo(minValue) >= 0 && results.get(1).getAmount().compareTo(maxValue) <= 0;
    }

    @Test
    public void findWithMaxBalance_shouldReturnTwoAccounts() {

        Account additional1 = new Account("additional1","ul. Testów", new BigDecimal(9L));
        Account expected1 = new Account("expected1","ul. Testów", new BigDecimal(50L));
        Account expected2 = new Account("expected2","ul. Testów", new BigDecimal(50L));
        Account additional2 = new Account("additional2","ul. Testów", new BigDecimal(39L));

        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(expected1);
        em.persist(expected2);
        em.persist(additional1);
        em.persist(additional2);
        em.getTransaction().commit();
        em.close();

        List<Account> results = dao.findWithMaxBalance();

        assert results != null;
        assert results.size() == 2;
    }

    @Test
    public void findWithNoOperationHistory_shouldReturnTwoAccounts() {

        Account expected1 = new Account("expected1","ul. Testów", new BigDecimal(50L));
        Account expected2 = new Account("expected2","ul. Testów", new BigDecimal(50L));
        Account additional1 = new Account("additional1","ul. Testów", new BigDecimal(9L));
        Account additional2 = new Account("additional2","ul. Testów", new BigDecimal(39L));

        AccountOperation op1 = new AccountOperation(additional1, new BigDecimal(1L), OperationType.DEPOSIT);
        AccountOperation op2 = new AccountOperation(additional2, new BigDecimal(1L), OperationType.DEPOSIT);
        TransferOperation op3 = new TransferOperation(additional1, new BigDecimal(1L), "TEST", additional2);

        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(expected1);
        em.persist(expected2);
        em.persist(additional1);
        em.persist(additional2);
        em.persist(op1);
        em.persist(op2);
        em.persist(op3);
        em.getTransaction().commit();
        em.close();

        List<Account> results = dao.findWithNoOperationHistory();

        assert results != null;
        assert results.size() == 2;
    }

    @Test
    public void findWithMostOperations_shouldReturnTwoAccounts() {

        Account expected1 = new Account("expected1","ul. Testów", new BigDecimal(50L));
        Account expected2 = new Account("expected2","ul. Testów", new BigDecimal(50L));
        Account additional1 = new Account("additional1","ul. Testów", new BigDecimal(9L));
        Account additional2 = new Account("additional2","ul. Testów", new BigDecimal(39L));

        AccountOperation op1 = new AccountOperation(expected1, new BigDecimal(1L), OperationType.DEPOSIT);
        AccountOperation op2 = new AccountOperation(expected1, new BigDecimal(1L), OperationType.DEPOSIT);
        TransferOperation op3 = new TransferOperation(expected2, new BigDecimal(1L), "TEST", additional2);
        TransferOperation op4 = new TransferOperation(expected2, new BigDecimal(1L), "TEST", additional2);

        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(expected1);
        em.persist(expected2);
        em.persist(additional1);
        em.persist(additional2);
        em.persist(op1);
        em.persist(op2);
        em.persist(op3);
        em.persist(op4);
        em.getTransaction().commit();
        em.close();

        List<Account> results = dao.findWithMostOperations();

        assert results != null;
        assert results.size() == 2;
    }
}
