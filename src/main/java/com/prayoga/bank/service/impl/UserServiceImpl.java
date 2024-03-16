package com.prayoga.bank.service.impl;

import com.prayoga.bank.model.response.GetUsersResponse;
import com.prayoga.bank.repository.UserRepository;
import com.prayoga.bank.service.UserService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  @Override
  public List<GetUsersResponse> getUsers() {
    List<GetUsersResponse> responses = new ArrayList<>();
    userRepository.findAll().forEach(user -> {
      responses.add(new GetUsersResponse(user.getName()));
    });
    return responses;
  }
}
