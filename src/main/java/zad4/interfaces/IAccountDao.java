package zad4.interfaces;

import zad4.models.Account;

import java.math.BigDecimal;
import java.util.List;

public interface IAccountDao extends IGenericDao<Account, Long> {
    List<Account> findByNameAndAdresss(String name, String address);
    List<Account> findByNameStartsWith(String text);
    List<Account> findByBalance(BigDecimal minValue, BigDecimal maxValue);
    List<Account> findWithMaxBalance();
    List<Account> findWithNoOperationHistory();
    List<Account> findWithMostOperations();
}
