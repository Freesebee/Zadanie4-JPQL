package zad4.models;

import zad4.dao.AccountDao;
import zad4.dao.AccountOperationDao;
import zad4.dao.TransferOperationDao;
import zad4.interfaces.IAccountDao;
import zad4.interfaces.IAccountOperationDao;
import zad4.interfaces.IBank;
import zad4.interfaces.ITransferOperationDto;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Optional;

public class Bank implements IBank {

    IAccountDao accountDao;
    IAccountOperationDao accountOperationDao;
    ITransferOperationDto transferOperationDto;

    public Bank() {
        accountDao = new AccountDao();
        accountOperationDao = new AccountOperationDao();
        transferOperationDto = new TransferOperationDao();
    }

    @Override
    public Long createAccount(String name, String address) {
        Account newAccount = new Account(name, address);
        newAccount.setCreationDate(new Date(System.currentTimeMillis()));
        newAccount.setModificationDate(new Date(System.currentTimeMillis()));

        accountDao.save(newAccount);

        return newAccount.getId();
    }

    @Override
    public Account findAccount(Long id) {
        Optional<Account> match = accountDao.findById(id);

        return match.isEmpty() ? null : match.get();
    }


    @Override
    public void deposit(Long id, BigDecimal amount) {
        Optional<Account> match = accountDao.findById(id);

        if (match.isEmpty()) {
            throw new AccountIdException();
        }

        BigDecimal newBalance = match.get().getAmount().add(amount);
        match.get().setAmount(newBalance);

        accountDao.update(match.get());

        accountOperationDao.save(new AccountOperation(match.get(), amount, OperationType.DEPOSIT));
    }


    @Override
    public BigDecimal getBalance(Long id) {
        Optional<Account> match = accountDao.findById(id);

        if (match.isEmpty()) {
            throw new AccountIdException();
        }
        return match.get().getAmount();
    }


    @Override
    public void withdraw(Long id, BigDecimal amount) {
        Optional<Account> match = accountDao.findById(id);

        if (match.isEmpty()) {
            throw new AccountIdException();
        }

        if (match.get().getAmount().compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }

        BigDecimal newBalance = match.get().getAmount().subtract(amount);
        match.get().setAmount(newBalance);

        accountDao.update(match.get());

        accountOperationDao.save(new AccountOperation(match.get(), amount, OperationType.WITHDRAW));
    }

    @Override
    public void transfer(Long idSource, Long idDestination, BigDecimal amount, String title) {

        Optional<Account> source = accountDao.findById(idSource);
        Optional<Account> destination = accountDao.findById(idDestination);

        if (source.isEmpty() || destination.isEmpty()) {
            throw new AccountIdException();
        }

        if (source.get().getAmount().compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }

        BigDecimal newSourceBalance = source.get().getAmount().subtract(amount);
        BigDecimal newDestinationBalance = destination.get().getAmount().add(amount);

        source.get().setAmount(newSourceBalance);
        destination.get().setAmount(newDestinationBalance);

        accountDao.update(source.get());
        accountDao.update(destination.get());

        transferOperationDto.save(new TransferOperation(source.get(), amount, title, destination.get()));
    }

}
