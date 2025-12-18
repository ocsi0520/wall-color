package com.my_wall_color.color_manager.color;

import com.my_wall_color.color_manager.user.domain.User;
import com.my_wall_color.color_manager.user.UserFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ColorFixture {
    public Color sulyom = new Color(null, (short) 136, (short) 147, (short) 152, "Sulyom", null);
    public Color brazilMenta = new Color(null, (short) 107, (short) 192, (short) 179, "Brazil menta", null);
    public Color havasiEukaliptusz = new Color(null, (short) 216, (short) 217, (short) 216, "Havasi eukaliptusz", null);
    public Color szarkalab = new Color(null, (short) 10, (short) 104, (short) 174, "Szarkaláb", null);
    public Color havasiGyopar = new Color(null, (short) 237, (short) 233, (short) 227, "Havasi gyopár", null);
    public Color kekSzelloRozsa = new Color(null, (short) 142, (short) 205, (short) 233, "Kék szellő rózsa", null);
    public Color palastfu = new Color(null, (short) 166, (short) 198, (short) 63, "Palástfű", null);
    public Color nonExistent = new Color(9999, (short) 1, (short) 2, (short) 3, "non-existent", null);

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
    }

    private void injectColors(UserFixture userFixture) {
        sulyom = injectColorWithRecorder(sulyom, userFixture.jdoe);
        brazilMenta = injectColorWithRecorder(brazilMenta, userFixture.jdoe);
        kekSzelloRozsa = injectColorWithRecorder(kekSzelloRozsa, userFixture.jdoe);
        szarkalab = injectColorWithRecorder(szarkalab, userFixture.donna);
        havasiGyopar = injectColorWithRecorder(havasiGyopar, userFixture.donna);
        havasiEukaliptusz = injectColorWithRecorder(havasiEukaliptusz, userFixture.jdoe);
        palastfu = injectColorWithRecorder(palastfu, userFixture.jdoe);
    }

    private Color injectColorWithRecorder(Color color, User recorder) {
        color.setRecordedBy(recorder.getId());
        return repository.save(color);
    }
}
