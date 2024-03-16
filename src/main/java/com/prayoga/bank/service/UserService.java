package com.prayoga.bank.service;

import com.prayoga.bank.entity.User;
import com.prayoga.bank.model.response.GetUsersResponse;
import java.util.List;

public interface UserService {
   List<GetUsersResponse> getUsers();
}
