package zad4.dao;

import zad4.interfaces.IAccountOperationDao;
import zad4.models.Account;
import zad4.models.AccountOperation;
import zad4.models.OperationType;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.swing.text.html.parser.Entity;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountOperationDao extends GenericDao<AccountOperation,Long> implements IAccountOperationDao {

    @Override
    public List<AccountOperation> findByCreationDate(Date startDate, Date endDate) {
        EntityManager em = getEntityManager();
        TypedQuery<AccountOperation> query = em.createNamedQuery("AccountOperation.findByCreationDate", AccountOperation.class);
        query.setParameter(1, startDate);
        query.setParameter(2, endDate);
        List<AccountOperation> results = query.getResultList();

        return results;
    }

    @Override
    public List<OperationType> findTypeOfMostFrequentOperation(Long accountId) {
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("AccountOperation.findTypeOfMostFrequentOperation");
        query.setParameter(1, accountId);

        List<Object[]> results = query.getResultList();

        if(results.size() == 1 && results.get(0) == null) return new ArrayList<OperationType>();

        return (List<OperationType>) query.getResultList();
    }
}
