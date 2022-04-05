package com.sap.ase.poker.security;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class PokerUserRepository {
    private List<PokerUser> users = Arrays.asList(
            new PokerUser("al-capone", "Al Capone", "all-in")
    );

    public Optional<PokerUser> findUserByName(String name) {
        return users.stream().filter(user-> user.getName().equals(name)).findFirst();
    }

    public List<PokerUser> findAll() {
        return Collections.unmodifiableList(this.users);
    }
}
