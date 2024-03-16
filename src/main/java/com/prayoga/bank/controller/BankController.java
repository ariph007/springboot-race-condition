package com.prayoga.bank.controller;

import com.prayoga.bank.service.AccountService;
import com.prayoga.bank.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/bank"})
@RequiredArgsConstructor
public class BankController {
  private final UserService userService;
  private final AccountService accountService;

  @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getUsers() {
    return ResponseEntity.ok(userService.getUsers());
  }

  @GetMapping(value = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getAccount() {
    return ResponseEntity.ok(accountService.getAccounts());
  }

  @GetMapping(value = "/transfer-race-condition", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> transferBalanceRaceCondition() {
    return ResponseEntity.ok(accountService.transferBalanceRaceCondition());
  }

  @GetMapping(value = "/transfer-sequential", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> transferBalanceSequential() {
    return ResponseEntity.ok(accountService.transferBalanceSequential());
  }
}
