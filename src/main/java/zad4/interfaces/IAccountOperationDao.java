package zad4.interfaces;

import zad4.models.AccountOperation;
import zad4.models.OperationType;

import java.sql.Date;
import java.util.List;

public interface IAccountOperationDao extends IGenericDao<AccountOperation, Long> {
    List<AccountOperation> findByCreationDate(Date startDate, Date endDate);
    List<OperationType> findTypeOfMostFrequentOperation(Long accountId);
}
