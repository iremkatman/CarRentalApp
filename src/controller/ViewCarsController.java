package controller;

import view.ViewCarsView;
import view.WelcomeView;

public class ViewCarsController {
    private ViewCarsView viewCarsView;
    private WelcomeView welcomeView;

    public ViewCarsController(ViewCarsView viewCarsView, WelcomeView welcomeView) {
        this.viewCarsView = viewCarsView;
        this.welcomeView = welcomeView;

        viewCarsView.getBackButton().addActionListener(e -> goBackToWelcome());
    }

    private void goBackToWelcome() {
        welcomeView.setVisible(true); // Re-show the WelcomeView
        viewCarsView.dispose();      // Dispose the ViewCarsView
    }
}
