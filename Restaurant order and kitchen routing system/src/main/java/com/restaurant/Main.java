package com.restaurant;

import com.restaurant.counter.CounterGUI;
import com.restaurant.kitchen.KitchenGUI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    private static final String REGULAR_FONT = loadFont(
        "/fonts/StardosStencil-Regular.ttf", "Stardos Stencil");
    private static final String BOLD_FONT = loadFont(
        "/fonts/StardosStencil-Bold.ttf", REGULAR_FONT);

    @Override
    public void start(Stage stage) {
        stage.setTitle("Restaurant System");
        var icon = getClass().getResourceAsStream("/images/icon.png");
        if (icon != null) {
            stage.getIcons().add(new Image(icon));
        }
        stage.setScene(createMainMenu(stage));
        stage.show();
    }

    private Scene createMainMenu(Stage stage) {
        Label title = new Label("Restaurant System");
        title.setStyle(font(BOLD_FONT, 28));
        title.setWrapText(true);

        Label subtitle = new Label("Select a workspace");
        subtitle.setStyle(font(REGULAR_FONT, 15));

        Button counterButton = new Button("Counter");
        counterButton.setPrefSize(240, 55);
        styleButton(counterButton);
        counterButton.setOnAction(event -> {
            CounterGUI counterGUI = new CounterGUI(
                    () -> stage.setScene(createMainMenu(stage)));
            stage.setScene(counterGUI.createScene(stage));
        });

        Button kitchenButton = new Button("Kitchen");
        kitchenButton.setPrefSize(240, 55);
        styleButton(kitchenButton);
        kitchenButton.setOnAction(event -> {
            KitchenGUI kitchenGUI = new KitchenGUI(
                () -> stage.setScene(createMainMenu(stage)));
            stage.setScene(kitchenGUI.createScene());
        });

        Button adminButton = new Button("Admin");
        adminButton.setPrefSize(240, 55);
        styleButton(adminButton);
        adminButton.setOnAction(event -> showUnavailableMessage(
                "Admin", "Admin login will be added when the admin workspace is implemented."));

        VBox menu = new VBox(15, title, subtitle, counterButton,
                kitchenButton, adminButton);
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(30));
        menu.setStyle("-fx-font-family: '" + REGULAR_FONT + "';");

        Scene scene = new Scene(menu, 500, 500);
        counterButton.prefWidthProperty().bind(menu.widthProperty().multiply(0.65));
        kitchenButton.prefWidthProperty().bind(menu.widthProperty().multiply(0.65));
        adminButton.prefWidthProperty().bind(menu.widthProperty().multiply(0.65));
        return scene;
    }

    private void showUnavailableMessage(String workspace, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(workspace);
        alert.setHeaderText(workspace + " workspace");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static void styleButton(Button button) {
        button.setStyle(font(BOLD_FONT, 16));
    }

    private static String font(String family, int size) {
        return "-fx-font-family: '" + family + "'; -fx-font-size: " + size + "px;";
    }

    private static String loadFont(String resource, String fallback) {
        Font font = Font.loadFont(Main.class.getResourceAsStream(resource), 16);
        return font == null ? fallback : font.getName();
    }

    public static void main(String[] args) {

        launch();
    }
}