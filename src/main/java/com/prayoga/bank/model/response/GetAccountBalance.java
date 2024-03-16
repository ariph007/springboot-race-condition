package com.prayoga.bank.model.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetAccountBalance {
  private String name;
  private BigDecimal lastBalance;
  private BigDecimal currentBalance;
}
