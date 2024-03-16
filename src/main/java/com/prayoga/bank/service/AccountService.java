package com.prayoga.bank.service;

import com.prayoga.bank.model.response.GetAccountBalance;
import com.prayoga.bank.model.response.GetAccountResponse;
import java.util.List;

public interface AccountService {
  List<GetAccountResponse> getAccounts();

  List<GetAccountBalance> transferBalanceRaceCondition();
  List<GetAccountBalance> transferBalanceSequential();
}
