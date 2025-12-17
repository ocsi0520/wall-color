package com.my_wall_color.color_manager.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserFixture {
    @Autowired
    UserRepository userRepository;

    private boolean isInjectedAlready = false;

    // pw user1
    public User jdoe = new User(null, "jdoe", "$2a$12$/wnuwqCoou1NwfDGzAPTFOsDgbyIblbOyGp.8WRvPMYt/GWSn8XYy", "John Doe");
    // pw user2
    public User donna = new User(null, "donna", "$2a$12$OhrUMXgjSnPXpGkiKkzwSeYkXPDI6IqhpM6/h6blyKE6lDhPPjLNa", "Donna Da Maria");
    // pw user3
    public User alex = new User(null, "alex", "$2a$12$y1ZToAjRAP.yor.J5lY8/uHwB2w1wFJ/CxhbU1V.9cyRezcC2sS2i", "Alex Scrowatz");
    // pw non-existent
    public User nonExistent = new User(null, "non-existent", "$2a$12$BIYHduo6AJy8a9bbn.urmOU9sgub5XMTmEyCTbT.z5/GJ5EzaXOd6", "non-existent");

    private final Map<String, String> passwordTable = Map.of(
            jdoe.getUsername(), "user1",
            donna.getUsername(), "user2",
            alex.getUsername(), "user3",
            nonExistent.getUsername(), "non-existent"
    );

    public String getPasswordFor(User user) {
        return passwordTable.get(user.getUsername());
    }

    public void injectAll() {
        if (isInjectedAlready) return;
        isInjectedAlready = true;

        // might be better to explicitly set ID sequence
        jdoe = userRepository.save(jdoe);
        donna = userRepository.save(donna);
        alex = userRepository.save(alex);
    }
}
