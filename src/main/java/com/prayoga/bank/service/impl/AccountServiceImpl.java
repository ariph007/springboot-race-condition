package com.prayoga.bank.service.impl;

import com.prayoga.bank.entity.Account;
import com.prayoga.bank.entity.User;
import com.prayoga.bank.model.response.GetAccountBalance;
import com.prayoga.bank.model.response.GetAccountResponse;
import com.prayoga.bank.repository.AccountRepository;
import com.prayoga.bank.repository.UserRepository;
import com.prayoga.bank.service.AccountService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
  private final AccountRepository accountRepository;
  private final UserRepository userRepository;

  @Override
  public List<GetAccountResponse> getAccounts() {
    List<GetAccountResponse> responseList = new ArrayList<>();
      accountRepository.findAll().forEach(account->{
          responseList.add(new GetAccountResponse(account.getUser().getName(), account.getBalance()));
      });
    return responseList;
  }

  @Override
  public List<GetAccountBalance> transferBalanceRaceCondition() {
    List<GetAccountBalance> balanceBeforeTransaction = getLastBalance();
    int totalTransferred = 100;
    final ExecutorService executor = Executors.newFixedThreadPool(totalTransferred);// Submit tasks to the thread pool
    for (int i = 0; i < totalTransferred; i++) {
      int threadId = i;
      executor.submit(() -> {
        System.out.println("Thread " + threadId + " is running.");
        return transaction();
      });
    }
    // Shutdown the executor when tasks are completed
    executor.shutdown();
    List<GetAccountBalance> balanceAfterTransaction = getLastBalance();
    balanceBeforeTransaction.get(0).setLastBalance(balanceAfterTransaction.get(0).getCurrentBalance());
    balanceBeforeTransaction.get(1).setLastBalance(balanceAfterTransaction.get(1).getCurrentBalance());
    return balanceBeforeTransaction;
  }

  @Override
  public List<GetAccountBalance> transferBalanceSequential() {
    return transaction();
  }

  private List<GetAccountBalance> transaction() {
    List<GetAccountBalance> result = new ArrayList<>();
    User eko = userRepository.findById("2c61c8d3-5676-44ac-bc85-b72469eb08a6").orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
    );
    Account ekoAccount = accountRepository.findByUser(eko).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found")
    );

    User ucok = userRepository.findById("fb203dc3-b641-4dcd-9725-5355b6f0e277").orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
    );
    Account ucokAccount = accountRepository.findByUser(ucok).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found")
    );

    GetAccountBalance ekoBalance = new GetAccountBalance();
    GetAccountBalance ucokBalance = new GetAccountBalance();

    ekoBalance.setName(eko.getName());
    ekoBalance.setLastBalance(ekoAccount.getBalance());

    ucokBalance.setName(ucok.getName());
    ucokBalance.setLastBalance(ucokAccount.getBalance());

    //* Transfer balance eko to ucok for $1
    BigDecimal amount = BigDecimal.valueOf(1);
    if (ekoAccount.getBalance().compareTo(BigDecimal.valueOf(1)) >= 1) {
      ekoAccount.setBalance(ekoAccount.getBalance().subtract(amount));
      Account ekoSuccessTransfer = accountRepository.saveAndFlush(ekoAccount);
      ekoBalance.setCurrentBalance(ekoSuccessTransfer.getBalance());

      ucokAccount.setBalance(ucokAccount.getBalance().add(amount));
      Account ucokSuccessReceive = accountRepository.saveAndFlush(ucokAccount);
      ucokBalance.setCurrentBalance(ucokSuccessReceive.getBalance());
    } else {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance");
    }
    result.add(ekoBalance);
    result.add(ucokBalance);
    return result;
  }

  private List<GetAccountBalance> getLastBalance(){
    List<GetAccountBalance> result = new ArrayList<>();
    User eko = userRepository.findById("2c61c8d3-5676-44ac-bc85-b72469eb08a6").orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
    );
    Account ekoAccount = accountRepository.findByUser(eko).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found")
    );

    User ucok = userRepository.findById("fb203dc3-b641-4dcd-9725-5355b6f0e277").orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
    );
    Account ucokAccount = accountRepository.findByUser(ucok).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found")
    );

    GetAccountBalance ekoBalance = new GetAccountBalance(eko.getName(),ekoAccount.getBalance(), ekoAccount.getBalance());
    GetAccountBalance ucokBalance = new GetAccountBalance(ucok.getName(),ucokAccount.getBalance(), ucokAccount.getBalance() );

    result.add(ekoBalance);
    result.add(ucokBalance);
    return result;
  }
}
