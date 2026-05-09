package com.gym.service;

import com.gym.entity.types.Gender;

public interface AuthService {
    /** Creates Client + AppUser in one transaction. Throws on duplicate phone. */
    void register(String firstName, String lastName, String phone,
                  String password, Gender gender);
}
