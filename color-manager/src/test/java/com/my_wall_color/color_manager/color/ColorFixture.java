package com.my_wall_color.color_manager.color;

import com.my_wall_color.color_manager.color.domain.Color;
import com.my_wall_color.color_manager.color.domain.ColorRepository;
import com.my_wall_color.color_manager.user.domain.User;
import com.my_wall_color.color_manager.user.UserFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ColorFixture {
    public Color sulyom = Color.create(null, 136, 147, 152, "Sulyom", null);
    public Color brazilMenta = Color.create(null, 107, 192, 179, "Brazil menta", null);
    public Color havasiEukaliptusz = Color.create(null, 216, 217, 216, "Havasi eukaliptusz", null);
    public Color szarkalab = Color.create(null, 10, 104, 174, "Szarkaláb", null);
    public Color havasiGyopar = Color.create(null, 237, 233, 227, "Havasi gyopár", null);
    public Color kekSzelloRozsa = Color.create(null, 142, 205, 233, "Kék szellő rózsa", null);
    public Color palastfu = Color.create(null, 166, 198, 63, "Palástfű", null);
    public Color nonExistent = Color.create(9999, 1, 2, 3, "non-existent", null);

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

    private void assignColorsToUsers(UserFixture userFixture) {
        repository.assignToUser(sulyom, userFixture.jdoe.getId());
        repository.assignToUser(havasiGyopar, userFixture.jdoe.getId());
        repository.assignToUser(palastfu, userFixture.donna.getId());
        repository.assignToUser(sulyom, userFixture.donna.getId());
        repository.assignToUser(sulyom, userFixture.alex.getId());
    }

    private void injectColors(UserFixture userFixture) {
        sulyom = injectColorWithRecorder(sulyom, userFixture.jdoe);
        brazilMenta = injectColorWithRecorder(brazilMenta, userFixture.jdoe);
        havasiEukaliptusz = injectColorWithRecorder(havasiEukaliptusz, userFixture.jdoe);
        szarkalab = injectColorWithRecorder(szarkalab, userFixture.donna);
        havasiGyopar = injectColorWithRecorder(havasiGyopar, userFixture.donna);
        kekSzelloRozsa = injectColorWithRecorder(kekSzelloRozsa, userFixture.jdoe);
        palastfu = injectColorWithRecorder(palastfu, userFixture.jdoe);
    }

    private Color injectColorWithRecorder(Color color, User recorder) {
        color.setRecordedBy(recorder.getId());
        return repository.save(color);
    }
}
