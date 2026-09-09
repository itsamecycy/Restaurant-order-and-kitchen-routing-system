package com.restaurant.kitchen;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class KitchenGUI {

    private static final List<KitchenOrder> pendingOrders = new ArrayList<>();

    private final Runnable onBackToMainMenu;
    private final Map<KitchenSection, VBox> sectionLists =
            new EnumMap<>(KitchenSection.class);

    public KitchenGUI(Runnable onBackToMainMenu) {
        this.onBackToMainMenu = onBackToMainMenu;
    }

    public static synchronized void submitOrder(
            int orderNumber, Map<String, Integer> products) {
        pendingOrders.add(new KitchenOrder(orderNumber, products));
    }

    public Scene createScene() {
        Label title = new Label("Kitchen Display");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button backButton = new Button("Back to Main Menu");
        backButton.setOnAction(event -> onBackToMainMenu.run());

        Button refreshButton = new Button("Refresh Orders");
        refreshButton.setOnAction(event -> refreshOrders());

        HBox header = new HBox(15, title, backButton, refreshButton);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20));

        HBox sections = new HBox(12);
        sections.setPadding(new Insets(0, 20, 20, 20));
        sections.setAlignment(Pos.TOP_LEFT);

        for (KitchenSection section : KitchenSection.values()) {
            VBox list = new VBox(10);
            list.setPadding(new Insets(10));
            list.setStyle("-fx-border-color: #b8b8b8; -fx-border-radius: 4;");
            sectionLists.put(section, list);

            VBox sectionColumn = new VBox(8, new Label(section.displayName), list);
            VBox.setVgrow(list, Priority.ALWAYS);
            sectionColumn.setPrefWidth(220);
            sections.getChildren().add(sectionColumn);
        }

        ScrollPane scrollPane = new ScrollPane(sections);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(scrollPane);
        refreshOrders();
        return new Scene(root, 1150, 650);
    }

    private void refreshOrders() {
        sectionLists.values().forEach(list -> list.getChildren().clear());

        synchronized (KitchenGUI.class) {
            if (pendingOrders.isEmpty()) {
                sectionLists.values().forEach(list ->
                        list.getChildren().add(new Label("No pending orders")));
                return;
            }

            for (KitchenOrder order : pendingOrders) {
                addOrderToSections(order);
            }
        }
    }

    private void addOrderToSections(KitchenOrder order) {
        Map<KitchenSection, List<String>> routedProducts =
                new EnumMap<>(KitchenSection.class);
        for (Map.Entry<String, Integer> product : order.products.entrySet()) {
            KitchenSection section = KitchenSection.forProduct(product.getKey());
            routedProducts.computeIfAbsent(section, ignored -> new ArrayList<>())
                    .add(product.getKey() + " x" + product.getValue());
        }

        boolean completionButtonAdded = false;
        for (KitchenSection section : KitchenSection.values()) {
            List<String> products = routedProducts.get(section);
            if (products == null) {
                continue;
            }

            VBox ticket = new VBox(6);
            ticket.setPadding(new Insets(8));
            ticket.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #888;");
            ticket.getChildren().add(new Label("Order #" + order.orderNumber));
            products.forEach(product -> ticket.getChildren().add(new Label(product)));

            if (!completionButtonAdded) {
                Button completeButton = new Button("Complete Order");
                completeButton.setOnAction(event -> completeOrder(order));
                ticket.getChildren().add(completeButton);
                completionButtonAdded = true;
            }
            sectionLists.get(section).getChildren().add(ticket);
        }
    }

    private void completeOrder(KitchenOrder order) {
        synchronized (KitchenGUI.class) {
            pendingOrders.remove(order);
        }
        refreshOrders();
    }

    private static final class KitchenOrder {
        private final int orderNumber;
        private final Map<String, Integer> products;

        private KitchenOrder(int orderNumber, Map<String, Integer> products) {
            this.orderNumber = orderNumber;
            this.products = new LinkedHashMap<>(products);
        }
    }

    private enum KitchenSection {
        GRILL("GRILL"),
        FRY("FRY"),
        DRINKS("DRINKS"),
        DESSERT("DESSERT"),
        OTHER("OTHER");

        private final String displayName;

        KitchenSection(String displayName) {
            this.displayName = displayName;
        }

        private static KitchenSection forProduct(String product) {
            return switch (product) {
                case "Burger", "Steak", "Chicken Wings" -> GRILL;
                case "French Fries" -> FRY;
                case "Iced Tea", "Coffee" -> DRINKS;
                case "Ice Cream" -> DESSERT;
                default -> OTHER;
            };
        }
    }
}
