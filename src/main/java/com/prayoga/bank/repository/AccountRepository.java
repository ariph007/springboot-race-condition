package com.prayoga.bank.repository;

import com.prayoga.bank.entity.Account;
import com.prayoga.bank.entity.User;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

  @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
//  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<Account> findByUser(User user);
}
