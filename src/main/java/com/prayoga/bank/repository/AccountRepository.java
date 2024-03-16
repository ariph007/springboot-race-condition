package com.prayoga.bank.repository;

import com.prayoga.bank.entity.Account;
import com.prayoga.bank.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

  Optional<Account> findByUser(User user);
}
