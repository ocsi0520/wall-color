package com.my_wall_color.color_manager.color;

import com.my_wall_color.color_manager.color.domain.Color;
import com.my_wall_color.color_manager.color.domain.ColorRepository;
import com.my_wall_color.color_manager.user.domain.User;
import com.my_wall_color.color_manager.user.UserFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ColorFixture {
    public Color sulyom = Color.create(null, 136, 147, 152, "Sulyom", null);
    public Color brazilMenta = Color.create(null, 107, 192, 179, "Brazil menta", null);
    public Color havasiEukaliptusz = Color.create(null, 216, 217, 216, "Havasi eukaliptusz", null);
    public Color szarkalab = Color.create(null, 10, 104, 174, "Szarkaláb", null);
    public Color havasiGyopar = Color.create(null, 237, 233, 227, "Havasi gyopár", null);
    public Color kekSzelloRozsa = Color.create(null, 142, 205, 233, "Kék szellő rózsa", null);
    public Color palastfu = Color.create(null, 166, 198, 63, "Palástfű", null);
    public Color greenBeige = Color.create(null, 190, 189, 127, "Green Beige", null);
    public Color nonExistent = Color.create(9999, 1, 2, 3, "non-existent", null);

    private Map<User, List<Color>> userListMap = new HashMap<>();

    private void storeAssignment(Color savedColor, User user) {
        if (userListMap.containsKey(user)) {
            userListMap.get(user).add(savedColor);
        } else {
            var newList = new ArrayList<Color>();
            newList.add(savedColor);
            userListMap.put(user, newList);
        }
    }

    public List<Color> getInitialAssignedColorsFor(User user) {
        List<Color> assignedColorsToUser = userListMap.get(user);
        return assignedColorsToUser == null ? List.of() : assignedColorsToUser;
    }

    public List<Color> getAllFixtureColors() {
        return List.of(
                sulyom,
                brazilMenta,
                havasiEukaliptusz,
                szarkalab,
                havasiGyopar,
                kekSzelloRozsa,
                palastfu,
                greenBeige
        );
    }

    @Autowired
    ColorRepository repository;

    // TODO: find better solution
    private boolean isInjectedAlready;

    public void injectAll(UserFixture userFixture) {
        if (isInjectedAlready) return;
        isInjectedAlready = true;

        injectColors(userFixture);
        assignColorsToUsers(userFixture);
    }

    private void assignColorToUser(Color color, User user) {
        repository.assignToUser(color, user.getId());
        storeAssignment(color, user);
    }

    private void assignColorsToUsers(UserFixture userFixture) {
        assignColorToUser(sulyom, userFixture.jdoe);
        assignColorToUser(havasiGyopar, userFixture.jdoe);

        assignColorToUser(palastfu, userFixture.donna);
        assignColorToUser(sulyom, userFixture.donna);
    }

    private void injectColors(UserFixture userFixture) {
        sulyom = injectColorWithRecorder(sulyom, userFixture.jdoe);
        brazilMenta = injectColorWithRecorder(brazilMenta, userFixture.jdoe);
        havasiEukaliptusz = injectColorWithRecorder(havasiEukaliptusz, userFixture.jdoe);
        szarkalab = injectColorWithRecorder(szarkalab, userFixture.donna);
        havasiGyopar = injectColorWithRecorder(havasiGyopar, userFixture.donna);
        kekSzelloRozsa = injectColorWithRecorder(kekSzelloRozsa, userFixture.jdoe);
        palastfu = injectColorWithRecorder(palastfu, userFixture.jdoe);
        greenBeige = injectColorWithRecorder(greenBeige, userFixture.jdoe);
    }

    private Color injectColorWithRecorder(Color color, User recorder) {
        color.setRecordedBy(recorder.getId());
        return repository.save(color);
    }
}
