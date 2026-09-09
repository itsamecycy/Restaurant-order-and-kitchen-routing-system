package com.restaurant.counter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.restaurant.kitchen.KitchenGUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CounterGUI {

    private static final String REGULAR_FONT = loadFont(
        "/fonts/StardosStencil-Regular.ttf", "Stardos Stencil");
    private static final String BOLD_FONT = loadFont(
        "/fonts/StardosStencil-Bold.ttf", REGULAR_FONT);
    private static final AtomicInteger NEXT_ORDER_NUMBER =
            new AtomicInteger(1001);
    private static final DateTimeFormatter RECEIPT_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Map<String, Double> menu = new LinkedHashMap<>();
    private final Map<String, Integer> selectedProducts = new LinkedHashMap<>();
    private final ListView<String> orderList = new ListView<>();
    private final Label totalLabel = new Label("Total: $0.00");
    private final Runnable onBackToMainMenu;

    public CounterGUI() {
        this(() -> {
        });
    }

    public CounterGUI(Runnable onBackToMainMenu) {
        this.onBackToMainMenu = onBackToMainMenu;
        menu.put("Burger", 10.99);
        menu.put("Steak", 19.99);
        menu.put("French Fries", 3.99);
        menu.put("Chicken Wings", 9.99);
        menu.put("Iced Tea", 2.99);
        menu.put("Coffee", 1.99);
        menu.put("Ice Cream", 4.99);
    }

    public Scene createScene(Stage stage) {
        Label title = new Label("RESTAURANT ORDER & KITCHEN ROUTING SYSTEM");
        title.setStyle(font(BOLD_FONT, 21));

        Label subtitle = new Label("COUNTER SYSTEM");
        subtitle.setStyle(font(REGULAR_FONT, 16));

        Label staff = new Label("Order Staff");
        staff.setStyle(font(REGULAR_FONT, 16));

        Button backButton = new Button("Back to Main Menu");
        backButton.setOnAction(event -> onBackToMainMenu.run());
        styleButton(backButton);

        VBox titleBlock = new VBox(3, title, subtitle);
        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);
        HBox header = new HBox(15, titleBlock, headerSpacer, staff, backButton);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20, 35, 15, 35));

        Label menuTitle = new Label("Menu");
        menuTitle.setStyle(font(BOLD_FONT, 18));
        VBox menuItems = new VBox(14);
        for (Map.Entry<String, Double> entry : menu.entrySet()) {
            menuItems.getChildren().add(createMenuItem(entry.getKey(), entry.getValue()));
        }
        ScrollPane menuScroll = new ScrollPane(menuItems);
        menuScroll.setFitToWidth(true);
        menuScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        menuScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        VBox.setVgrow(menuScroll, Priority.ALWAYS);

        VBox menuSection = new VBox(14, menuTitle, menuScroll);
        menuSection.setPadding(new Insets(10, 25, 20, 30));
        menuSection.setMinWidth(230);
        menuSection.setMaxWidth(Double.MAX_VALUE);
        menuSection.setFillWidth(true);

        orderList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        orderList.setMinHeight(140);
        orderList.setMaxHeight(Double.MAX_VALUE);
        orderList.setPlaceholder(new Label("No items added yet"));

        Button removeButton = new Button("Remove Item");
        removeButton.setOnAction(event -> removeSelectedProduct());
        styleButton(removeButton);

        Button clearButton = new Button("Clear Order");
        clearButton.setOnAction(event -> clearOrder());
        styleButton(clearButton);

        Button createOrderButton = new Button("Confirm Order");
        createOrderButton.setDefaultButton(true);
        createOrderButton.setOnAction(event -> createReceipt(stage));
        styleButton(createOrderButton);

        VBox orderActions = new VBox(12, removeButton, clearButton, createOrderButton);
        VBox orderSection = new VBox(14, new Label("Current Order"), orderList,
            totalLabel, orderActions);
        ((Label) orderSection.getChildren().get(0)).setStyle(font(BOLD_FONT, 18));
        totalLabel.setStyle(font(BOLD_FONT, 16));
        orderSection.setPadding(new Insets(10, 30, 20, 25));
        VBox.setVgrow(orderList, Priority.ALWAYS);

        BorderPane content = new BorderPane();
        content.setTop(header);
        content.setLeft(menuSection);
        content.setCenter(orderSection);

        BorderPane root = new BorderPane(content);
        root.setStyle("-fx-font-family: '" + REGULAR_FONT + "';");
        Scene scene = new Scene(root, 950, 760);
        menuSection.prefWidthProperty().bind(root.widthProperty().multiply(0.46));
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.isAltDown() && event.isShiftDown()) {
                onBackToMainMenu.run();
                event.consume();
            }
        });
        return scene;
    }

    private HBox createMenuItem(String product, double price) {
        Label productLabel = new Label(product);
        productLabel.setStyle(font(REGULAR_FONT, 17));
        Label priceLabel = new Label(String.format("$%.2f", price));
        priceLabel.setStyle(font(REGULAR_FONT, 17));
        Button addButton = new Button("ADD");
        addButton.setOnAction(event -> addProduct(product));
        styleButton(addButton);

        VBox productDetails = new VBox(6, productLabel, addButton);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox item = new HBox(10, productDetails, spacer, priceLabel);
        item.setMaxWidth(Double.MAX_VALUE);
        item.setPadding(new Insets(10, 15, 9, 15));
        item.setStyle("-fx-border-color: #777; -fx-border-width: 1;"
                + " -fx-background-color: white;");
        return item;
    }

    private void addProduct(String product) {
        selectedProducts.put(product, selectedProducts.getOrDefault(product, 0) + 1);
        refreshOrder();
    }

    private void removeSelectedProduct() {
        String selectedProduct = orderList.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            return;
        }

        String product = selectedProduct.substring(0, selectedProduct.indexOf(" x"));
        selectedProducts.remove(product);
        refreshOrder();
    }

    private void clearOrder() {
        selectedProducts.clear();
        refreshOrder();
    }

    private void refreshOrder() {
        orderList.getItems().clear();
        double total = 0;

        for (Map.Entry<String, Integer> entry : selectedProducts.entrySet()) {
            double itemTotal = menu.get(entry.getKey()) * entry.getValue();
            total += itemTotal;
            orderList.getItems().add(String.format("%s x%d - $%.2f",
                    entry.getKey(), entry.getValue(), itemTotal));
        }

        totalLabel.setText(String.format("Total: $%.2f", total));
    }

    private static void styleButton(Button button) {
        button.setStyle("-fx-font-family: '" + BOLD_FONT + "';"
                + " -fx-font-size: 14px;");
    }

    private static String font(String family, int size) {
        return "-fx-font-family: '" + family + "'; -fx-font-size: " + size + "px;";
    }

    private static String loadFont(String resource, String fallback) {
        Font font = Font.loadFont(CounterGUI.class.getResourceAsStream(resource), 16);
        return font == null ? fallback : font.getName();
    }

    private void createReceipt(Stage owner) {
        if (selectedProducts.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(owner);
            alert.setTitle("No Items");
            alert.setHeaderText(null);
            alert.setContentText("Add at least one item before creating an order.");
            alert.showAndWait();
            return;
        }

        int orderNumber = NEXT_ORDER_NUMBER.getAndIncrement();
        KitchenGUI.submitOrder(orderNumber, selectedProducts);
        String receipt = buildReceipt(orderNumber);
        TextArea receiptText = new TextArea(receipt);
        receiptText.setEditable(false);
        receiptText.setPrefRowCount(16);
        receiptText.setPrefColumnCount(42);

        Alert receiptDialog = new Alert(Alert.AlertType.INFORMATION);
        receiptDialog.initOwner(owner);
        receiptDialog.setTitle("Order Receipt #" + orderNumber);
        receiptDialog.setHeaderText("Order #" + orderNumber
            + " created successfully");
        receiptDialog.getDialogPane().setContent(receiptText);
        receiptDialog.showAndWait();
        clearOrder();
    }

    private String buildReceipt(int orderNumber) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("RESTAURANT ORDER RECEIPT\n");
        receipt.append("ORDER NUMBER: #").append(orderNumber).append("\n");
        receipt.append("Date: ").append(LocalDateTime.now().format(RECEIPT_TIME_FORMAT))
                .append("\n");
        receipt.append("------------------------------------------\n");

        double total = 0;
        for (Map.Entry<String, Integer> entry : selectedProducts.entrySet()) {
            double itemTotal = menu.get(entry.getKey()) * entry.getValue();
            total += itemTotal;
            receipt.append(String.format("%-20s x%-3d $%8.2f\n",
                    entry.getKey(), entry.getValue(), itemTotal));
        }

        receipt.append("------------------------------------------\n");
        receipt.append(String.format("TOTAL:                              $%8.2f\n", total));
        receipt.append("Thank you for your order!\n");
        return receipt.toString();
    }
}
