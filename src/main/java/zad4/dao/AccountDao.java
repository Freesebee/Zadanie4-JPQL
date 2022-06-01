package zad4.dao;

import zad4.interfaces.IAccountDao;
import zad4.models.Account;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.List;

public class AccountDao extends GenericDao<Account,Long> implements IAccountDao {
    @Override
    public List<Account> findByNameAndAdresss(String name, String address) {
        EntityManager em = getEntityManager();

        TypedQuery<Account> query = em.createNamedQuery("findByNameAndAdresss", Account.class);
        query.setParameter(1, address);
        query.setParameter(2, name);

        List<Account> accounts = query.getResultList();
        em.close();

        return accounts.isEmpty() ? null : accounts;
    }

    @Override
    public List<Account> findByNameStartsWith(String text) {
        EntityManager em = getEntityManager();
        TypedQuery<Account> query = em.createNamedQuery("findByNameStartsWith", Account.class);
        query.setParameter(1, text + "%");

        List<Account> accounts = query.getResultList();
        em.close();

        return accounts.isEmpty() ? null : accounts;
    }

    @Override
    public List<Account> findByBalance(BigDecimal minValue, BigDecimal maxValue) {
        EntityManager em = getEntityManager();
        TypedQuery<Account> query = em.createNamedQuery("findByBalance", Account.class);
        query.setParameter(1, minValue);
        query.setParameter(2, maxValue);

        List<Account> accounts = query.getResultList();
        em.close();

        return accounts.isEmpty() ? null : accounts;
    }

    @Override
    public List<Account> findWithMaxBalance() {
        EntityManager em = getEntityManager();
        TypedQuery<Account> query = em.createNamedQuery("findWithMaxBalance", Account.class);

        List<Account> accounts = query.getResultList();
        em.close();

        return accounts.isEmpty() ? null : accounts;
    }

    @Override
    public List<Account> findWithNoOperationHistory() {
        EntityManager em = getEntityManager();
        TypedQuery<Account> query = em.createNamedQuery("findWithNoOperationHistory", Account.class);

        List<Account> accounts = query.getResultList();
        em.close();

        return accounts.isEmpty() ? null : accounts;
    }

    @Override
    public List<Account> findWithMostOperations() {
        EntityManager em = getEntityManager();
        TypedQuery<Account> query = em.createNamedQuery("findWithMostOperations", Account.class);

        List<Account> accounts = query.getResultList();
        em.close();

        return accounts.isEmpty() ? null : accounts;
    }
}
