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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

  private final AccountRepository accountRepository;
  private final UserRepository userRepository;

  @Override
  public List<GetAccountResponse> getAccounts() {
    List<GetAccountResponse> responseList = new ArrayList<>();
    accountRepository.findAll().forEach(account -> {
      responseList.add(new GetAccountResponse(account.getUser().getName(), account.getBalance()));
    });
    return responseList;
  }

  @Override
  @Transactional()
  public List<GetAccountBalance> transferBalance() {
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
    System.out.println("Transfer Balance");
    return result;
  }
}
