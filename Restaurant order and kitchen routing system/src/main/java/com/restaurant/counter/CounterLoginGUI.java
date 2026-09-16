package com.restaurant.counter;

import com.restaurant.repository.CounterRepository;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CounterLoginGUI {

    private static final String REGULAR_FONT = loadFont(
            "/fonts/StardosStencil-Regular.ttf", "Stardos Stencil");
    private static final String BOLD_FONT = loadFont(
            "/fonts/StardosStencil-Bold.ttf", REGULAR_FONT);

    private final Runnable onBackToMainMenu;
    private final Runnable onLoginSuccess;
    private final CounterRepository counterRepository = new CounterRepository();

    public CounterLoginGUI(Runnable onBackToMainMenu, Runnable onLoginSuccess) {
        this.onBackToMainMenu = onBackToMainMenu;
        this.onLoginSuccess = onLoginSuccess;
    }

    public Scene createScene(Stage stage) {
        Label title = new Label("COUNTER LOGIN");
        title.setStyle(font(BOLD_FONT, 24));

        Label usernameLabel = new Label("Username");
        usernameLabel.setStyle(font(BOLD_FONT, 15));
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");

        Label passwordLabel = new Label("Password");
        passwordLabel.setStyle(font(BOLD_FONT, 15));
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #b00020;" + font(REGULAR_FONT, 14));

        Button loginButton = new Button("Login");
        styleButton(loginButton);
        loginButton.setDefaultButton(true);
        loginButton.setOnAction(event -> {
            if (counterRepository.authenticate(
                    usernameField.getText(), passwordField.getText())) {
                onLoginSuccess.run();
            } else {
                errorLabel.setText("Invalid username or password.");
                passwordField.clear();
            }
        });

        Button backButton = new Button("Back to Main Menu");
        styleButton(backButton);
        backButton.setOnAction(event -> onBackToMainMenu.run());

        VBox loginForm = new VBox(10, title, usernameLabel, usernameField,
                passwordLabel, passwordField, errorLabel, loginButton, backButton);
        loginForm.setAlignment(Pos.CENTER);
        loginForm.setPadding(new Insets(30));
        loginForm.setMaxWidth(340);

        VBox root = new VBox(loginForm);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-font-family: '" + REGULAR_FONT + "';");
        return new Scene(root, 500, 500);
    }

    private static void styleButton(Button button) {
        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle(font(BOLD_FONT, 15));
    }

    private static String font(String family, int size) {
        return "-fx-font-family: '" + family + "'; -fx-font-size: " + size + "px;";
    }

    private static String loadFont(String resource, String fallback) {
        Font font = Font.loadFont(CounterLoginGUI.class.getResourceAsStream(resource), 16);
        return font == null ? fallback : font.getName();
    }
}