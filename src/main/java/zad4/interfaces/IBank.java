package zad4.interfaces;

import zad4.models.Account;

import java.math.BigDecimal;

public interface IBank {
    /**
     * Tworzy nowe lub zwraca id istniejącego konta.
     *
     * @param name imie i nazwisko własciciela
     * @param address adres własciciela
     * @return id utworzonego lub istniejacego konta.
     */
    Long createAccount(String name, String address);

    /**
     * Znajduje identyfikator konta.
     *
     * @param id id istniejacego konta
     * @return instancję klasy Account lub null, gdy brak konta o podanych parametrach
     */
    Account findAccount(Long id);

    /**
     * Dodaje srodki do konta.
     *
     * @param id id konta
     * @param amount srodki
     * @throws AccountIdException gdy id konta jest nieprawidlowe
     */
    void deposit(Long id, BigDecimal amount);

    /**
     * Zwraca ilosc srodkow na koncie.
     *
     * @param id id konta
     * @return srodki
     * @throws AccountIdException gdy id konta jest nieprawidlowe
     */
    BigDecimal getBalance(Long id);

    /**
     * Pobiera srodki z konta.
     *
     * @param id id konta
     * @param amount srodki
     * @throws AccountIdException gdy id konta jest nieprawidlowe
     * @throws InsufficientFundsException gdy srodki na koncie nie sa
     * wystarczajace do wykonania operacji
     */
    void withdraw(Long id, BigDecimal amount);

    /**
     * Przelewa srodki miedzy kontami.
     *
     * @param idSource id konta
     * @param idDestination id konta
     * @param amount srodki
     * @param title tytuł przelewu
     * @throws AccountIdException gdy id konta jest nieprawidlowe
     * @throws InsufficientFundsException gdy srodki na koncie nie sa
     * wystarczajace do wykonania operacji
     */
    void transfer(Long idSource, Long idDestination, BigDecimal amount, String title);

    class InsufficientFundsException extends RuntimeException {
    };

    class AccountIdException extends RuntimeException {
    };
}