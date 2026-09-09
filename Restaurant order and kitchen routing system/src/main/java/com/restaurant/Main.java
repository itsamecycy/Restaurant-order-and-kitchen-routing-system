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
import javafx.stage.Stage;

public class Main extends Application {

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
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Label subtitle = new Label("Select a workspace");
        subtitle.setStyle("-fx-font-size: 15px;");

        Button counterButton = new Button("Counter");
        counterButton.setPrefSize(240, 55);
        counterButton.setOnAction(event -> {
            CounterGUI counterGUI = new CounterGUI(
                    () -> stage.setScene(createMainMenu(stage)));
            stage.setScene(counterGUI.createScene(stage));
        });

        Button kitchenButton = new Button("Kitchen");
        kitchenButton.setPrefSize(240, 55);
        kitchenButton.setOnAction(event -> {
            KitchenGUI kitchenGUI = new KitchenGUI(
                () -> stage.setScene(createMainMenu(stage)));
            stage.setScene(kitchenGUI.createScene());
        });

        Button adminButton = new Button("Admin");
        adminButton.setPrefSize(240, 55);
        adminButton.setOnAction(event -> showUnavailableMessage(
                "Admin", "Admin login will be added when the admin workspace is implemented."));

        VBox menu = new VBox(15, title, subtitle, counterButton,
                kitchenButton, adminButton);
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(30));

        return new Scene(menu, 500, 500);
    }

    private void showUnavailableMessage(String workspace, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(workspace);
        alert.setHeaderText(workspace + " workspace");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {

        launch();
    }
}