package zad4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zad4.dao.AccountOperationDao;
import zad4.interfaces.IAccountOperationDao;
import zad4.models.*;

import javax.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static zad4.utils.JpaFactory.getEntityManager;

public class AccountOperationTest {

    IAccountOperationDao dao = new AccountOperationDao();

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
    public void findByCreationDate_shouldReturnAllAccountOperations() {
        List<AccountOperation> operations = new ArrayList<>();

        Account sourceAccount = new Account("Tester1","ul. Testów Jednostkowych", new BigDecimal(100));
        Account destinationAccount = new Account("Tester2","ul. Testów Jednostkowych", new BigDecimal(100));

        operations.add(new AccountOperation(sourceAccount, new BigDecimal(150), OperationType.DEPOSIT));
        operations.add(new AccountOperation(sourceAccount, new BigDecimal(50), OperationType.WITHDRAW));
        operations.add(new TransferOperation(sourceAccount, new BigDecimal(50), "Test", destinationAccount));

        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(sourceAccount);
        em.persist(destinationAccount);
        for(AccountOperation op : operations) {
            em.persist(op);
        }
        em.getTransaction().commit();
        em.close();

        List<AccountOperation> results = dao.findByCreationDate(
                new java.sql.Date(System.currentTimeMillis()-86400000L),
                new java.sql.Date(System.currentTimeMillis()+86400000L));

        assert results != null;
        assert operations.size() == results.size();
        assert results.containsAll(operations);
    }

    @Test
    public void findByCreationDate_shouldReturnOneAccountOperation() {
        long currentTimeMillis = System.currentTimeMillis();
        List<AccountOperation> operations = new ArrayList<>();

        Account sourceAccount = new Account("Tester1","ul. Testów Jednostkowych", new BigDecimal(100));
        Account destinationAccount = new Account("Tester2","ul. Testów Jednostkowych", new BigDecimal(100));

        operations.add(new AccountOperation(sourceAccount, new BigDecimal(150), OperationType.DEPOSIT));
        operations.add(new AccountOperation(sourceAccount, new BigDecimal(50), OperationType.WITHDRAW));

        AccountOperation transferOperation = new TransferOperation(sourceAccount, new BigDecimal(50), "Test", destinationAccount);
        transferOperation.setCreationDate(new java.sql.Date(currentTimeMillis-(86400000L*2)));
        operations.add(transferOperation);

        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(sourceAccount);
        em.persist(destinationAccount);
        for(AccountOperation op : operations) {
            em.persist(op);
        }
        em.getTransaction().commit();
        em.close();

        List<AccountOperation> results = dao.findByCreationDate(
                new java.sql.Date(currentTimeMillis-(86400000L*3)),
                new java.sql.Date(currentTimeMillis-(86400000L*1)));

        assert results != null;
        assert results.size() == 1;
        assert results.contains(transferOperation);
    }

    @Test
    public void findByCreationDate_shouldReturnNoAccountOperations() {
        List<AccountOperation> operations = new ArrayList<>();

        Account sourceAccount = new Account("Tester1","ul. Testów Jednostkowych", new BigDecimal(100));
        Account destinationAccount = new Account("Tester2","ul. Testów Jednostkowych", new BigDecimal(100));

        operations.add(new AccountOperation(sourceAccount, new BigDecimal(150), OperationType.DEPOSIT));
        operations.add(new AccountOperation(sourceAccount, new BigDecimal(50), OperationType.WITHDRAW));
        operations.add(new TransferOperation(sourceAccount, new BigDecimal(50), "Test", destinationAccount));

        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(sourceAccount);
        em.persist(destinationAccount);
        for(AccountOperation op : operations) {
            em.persist(op);
        }
        em.getTransaction().commit();
        em.close();

        List<AccountOperation> results = dao.findByCreationDate(
                new java.sql.Date(System.currentTimeMillis()-(86400000L*2)),
                new java.sql.Date(System.currentTimeMillis()-86400000L));

        assert results != null;
        assert results.size() == 0;
    }

    @Test
    public void findTypeOfMostFrequentOperation_shouldReturnDEPOSIT() {

        List<AccountOperation> operations = new ArrayList<>();

        Account sourceAccount = new Account("Tester1","ul. Testów Jednostkowych", new BigDecimal(100));
        Account destinationAccount = new Account("Tester2","ul. Testów Jednostkowych", new BigDecimal(100));

        operations.add(new AccountOperation(sourceAccount, new BigDecimal(150), OperationType.DEPOSIT));
        operations.add(new AccountOperation(sourceAccount, new BigDecimal(50), OperationType.DEPOSIT));
        operations.add(new TransferOperation(sourceAccount, new BigDecimal(50), "Test", destinationAccount));

        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(sourceAccount);
        em.persist(destinationAccount);
        for(AccountOperation op : operations) {
            em.persist(op);
        }
        em.getTransaction().commit();
        em.close();

        List<OperationType> results = dao.findTypeOfMostFrequentOperation(sourceAccount.getId());

        assert results != null;
        assert results.size() == 1;
        assert results.get(0) == OperationType.DEPOSIT;
    }

    @Test
    public void findTypeOfMostFrequentOperation_shouldReturnAllOperationTypes() {
        List<AccountOperation> operations = new ArrayList<>();

        Account sourceAccount = new Account("Tester1","ul. Testów Jednostkowych", new BigDecimal(100));
        Account destinationAccount = new Account("Tester2","ul. Testów Jednostkowych", new BigDecimal(100));

        operations.add(new AccountOperation(sourceAccount, new BigDecimal(150), OperationType.DEPOSIT));
        operations.add(new AccountOperation(sourceAccount, new BigDecimal(50), OperationType.WITHDRAW));
        operations.add(new TransferOperation(sourceAccount, new BigDecimal(50), "Test", destinationAccount));

        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(sourceAccount);
        em.persist(destinationAccount);
        for(AccountOperation op : operations) {
            em.persist(op);
        }
        em.getTransaction().commit();
        em.close();

        List<OperationType> results = dao.findTypeOfMostFrequentOperation(sourceAccount.getId());

        assert results != null;
        assert results.size() == 3;
        assert results.contains(OperationType.WITHDRAW);
        assert results.contains(OperationType.TRANSFER);
        assert results.contains(OperationType.DEPOSIT);
    }

    @Test
    public void findTypeOfMostFrequentOperation_shouldReturnEmptyList() {
        List<OperationType> result = dao.findTypeOfMostFrequentOperation(0L);

        assert result != null;
        assert result.size() == 0;
    }
}
