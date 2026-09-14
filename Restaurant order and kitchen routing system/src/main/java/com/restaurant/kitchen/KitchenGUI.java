package com.restaurant.kitchen;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class KitchenGUI {

    private static final List<KitchenOrder> pendingOrders = new ArrayList<>();
    private static final List<KitchenOrder> completedOrders = new ArrayList<>();
    private static final String REGULAR_FONT = loadFont(
            "/fonts/StardosStencil-Regular.ttf", "Stardos Stencil");
    private static final String BOLD_FONT = loadFont(
            "/fonts/StardosStencil-Bold.ttf", REGULAR_FONT);

    private final Runnable onBackToMainMenu;
    private final VBox stationOrders = new VBox(15);
    private final Label queueLabel = new Label();
    private final Label stationTitle = new Label();
    private KitchenSection selectedSection = KitchenSection.GRILL;
    private boolean showingOrderLog;

    public KitchenGUI(Runnable onBackToMainMenu) {
        this.onBackToMainMenu = onBackToMainMenu;
    }

    public static synchronized void submitOrder(
            int orderNumber, Map<String, Integer> products) {
        pendingOrders.add(new KitchenOrder(orderNumber, products));
    }

    public Scene createScene() {
        Label title = new Label("RESTAURANT ORDER & KITCHEN ROUTING SYSTEM");
        title.setStyle(font(BOLD_FONT, 21));
        title.setWrapText(true);

        Label subtitle = new Label("KITCHEN SYSTEM");
        subtitle.setStyle(font(REGULAR_FONT, 16));

        VBox titleBlock = new VBox(3, title, subtitle);

        Button backButton = new Button("Back to Main Menu");
        backButton.setOnAction(event -> onBackToMainMenu.run());
        styleButton(backButton, false);

        Button refreshButton = new Button("Refresh Orders");
        refreshButton.setOnAction(event -> refreshOrders());
        styleButton(refreshButton, false);

        Button orderLogButton = new Button("Order Log");
        orderLogButton.setOnAction(event -> {
            showingOrderLog = !showingOrderLog;
            orderLogButton.setText(showingOrderLog ? "Active Orders" : "Order Log");
            refreshOrders();
        });
        styleButton(orderLogButton, false);

        HBox header = new HBox(15, titleBlock, backButton, refreshButton, orderLogButton);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20));
        HBox.setHgrow(titleBlock, javafx.scene.layout.Priority.ALWAYS);

        FlowPane stationTabs = new FlowPane(28, 10);
        stationTabs.setAlignment(Pos.CENTER);
        stationTabs.setPadding(new Insets(12, 20, 12, 20));
        for (KitchenSection section : KitchenSection.values()) {
            Button stationButton = new Button(section.displayName);
            stationButton.setPrefWidth(145);
            stationButton.setOnAction(event -> {
                selectedSection = section;
                stationTitle.setText(selectedSection.displayName + " STATION");
                updateStationTabs(stationTabs);
                refreshOrders();
            });
            stationTabs.getChildren().add(stationButton);
        }

        updateStationTabs(stationTabs);
        stationTitle.setText(selectedSection.displayName + " STATION");

        stationTitle.setStyle(font(BOLD_FONT, 18));
        queueLabel.setStyle(font(REGULAR_FONT, 16));
        stationOrders.setPadding(new Insets(10, 20, 20, 20));

        VBox stationContent = new VBox(12, stationTitle,
                new Label("Assigned Kitchen Queue"), queueLabel,
                new Separator(), stationOrders);
        stationContent.setPadding(new Insets(15, 35, 10, 35));
        stationContent.setStyle("-fx-background-color: white;");
        VBox.setVgrow(stationOrders, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(stationContent);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        BorderPane root = new BorderPane();
        root.setTop(new VBox(header, stationTabs));
        root.setCenter(scrollPane);
        refreshOrders();
        Scene scene = new Scene(root, 1050, 700);
        scene.getRoot().setStyle("-fx-font-family: '" + REGULAR_FONT + "';");
        return scene;
    }

    private void refreshOrders() {
        stationOrders.getChildren().clear();

        synchronized (KitchenGUI.class) {
            if (showingOrderLog) {
                stationTitle.setText(selectedSection.displayName + " ORDER LOG");
                queueLabel.setText("Completed Orders: " + completedOrders.size());
            } else {
                stationTitle.setText(selectedSection.displayName + " STATION");
                queueLabel.setText("Queue: " + pendingOrders.size() + " Active Orders");
            }

            boolean hasOrders = false;
            List<KitchenOrder> ordersToDisplay = showingOrderLog
                    ? completedOrders : pendingOrders;
            for (KitchenOrder order : ordersToDisplay) {
                if (order.hasProductsFor(selectedSection)) {
                    addOrderCard(order, showingOrderLog);
                    hasOrders = true;
                }
            }
            if (!hasOrders) {
                String emptyMessage = showingOrderLog
                        ? "No completed orders for this station"
                        : "No orders assigned to this station";
                Label emptyLabel = new Label(emptyMessage);
                emptyLabel.setStyle(font(REGULAR_FONT, 16));
                stationOrders.getChildren().add(emptyLabel);
                return;
            }
        }
    }

    private void addOrderCard(KitchenOrder order, boolean historyCard) {
        VBox ticket = new VBox(8);
        ticket.setPadding(new Insets(12, 15, 12, 15));
        ticket.setMaxWidth(Double.MAX_VALUE);
        ticket.setStyle("-fx-border-color: #222; -fx-border-width: 1;"
                + " -fx-background-color: white;");

        HBox orderHeading = new HBox();
        Label orderNumber = new Label("Order #" + order.orderNumber);
        orderNumber.setStyle(font(BOLD_FONT, 16));
        Label status = new Label("Status: " + order.status.displayName);
        status.setStyle(font(REGULAR_FONT, 15));
        orderHeading.getChildren().addAll(orderNumber, status);
        status.setMaxWidth(Double.MAX_VALUE);
        status.setAlignment(Pos.CENTER_RIGHT);
        ticket.getChildren().add(orderHeading);

        for (Map.Entry<String, Integer> product : order.products.entrySet()) {
            if (KitchenSection.forProduct(product.getKey()) == selectedSection) {
                Label productLabel = new Label(product.getKey() + " x" + product.getValue());
                productLabel.setStyle(font(REGULAR_FONT, 16));
                ticket.getChildren().add(productLabel);
            }
        }

        if (!historyCard) {
            HBox actions = new HBox(15);
            actions.setAlignment(Pos.CENTER_LEFT);
            Button startButton = new Button("START PREPARING");
            startButton.setDisable(order.status != OrderStatus.PENDING);
            startButton.setOnAction(event -> updateStatus(order, OrderStatus.PREPARING));
            Button readyButton = new Button("MARK READY");
            readyButton.setDisable(order.status != OrderStatus.PREPARING);
            readyButton.setOnAction(event -> updateStatus(order, OrderStatus.READY));
            styleButton(startButton, false);
            styleButton(readyButton, false);
            actions.getChildren().addAll(startButton, readyButton);
            ticket.getChildren().add(actions);
        }
        stationOrders.getChildren().add(ticket);
    }

    private void updateStatus(KitchenOrder order, OrderStatus status) {
        synchronized (KitchenGUI.class) {
            order.status = status;
            if (status == OrderStatus.READY) {
                pendingOrders.remove(order);
                completedOrders.add(order);
            }
        }
        refreshOrders();
    }

    private void updateStationTabs(FlowPane tabs) {
        for (javafx.scene.Node node : tabs.getChildren()) {
            Button button = (Button) node;
            boolean selected = button.getText().equals(selectedSection.displayName);
            styleButton(button, selected);
        }
    }

    private static void styleButton(Button button, boolean selected) {
        button.setStyle("-fx-font-family: '" + BOLD_FONT + "';"
                + " -fx-font-size: 15px; -fx-font-weight: bold;"
                + (selected ? " -fx-background-color: #d0d0d0;" : ""));
    }

    private static String font(String family, int size) {
        return "-fx-font-family: '" + family + "'; -fx-font-size: " + size + "px;";
    }

    private static String loadFont(String resource, String fallback) {
        Font font = Font.loadFont(KitchenGUI.class.getResourceAsStream(resource), 16);
        return font == null ? fallback : font.getName();
    }

    private static final class KitchenOrder {
        private final int orderNumber;
        private final Map<String, Integer> products;
        private OrderStatus status = OrderStatus.PENDING;

        private KitchenOrder(int orderNumber, Map<String, Integer> products) {
            this.orderNumber = orderNumber;
            this.products = new LinkedHashMap<>(products);
        }

        private boolean hasProductsFor(KitchenSection section) {
            return products.keySet().stream()
                    .anyMatch(product -> KitchenSection.forProduct(product) == section);
        }
    }

    private enum OrderStatus {
        PENDING("PENDING"),
        PREPARING("PREPARING"),
        READY("READY");

        private final String displayName;

        OrderStatus(String displayName) {
            this.displayName = displayName;
        }
    }

    private enum KitchenSection {
        GRILL("GRILL"),
        FRY("FRY"),
        DRINKS("BEVERAGE"),
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
