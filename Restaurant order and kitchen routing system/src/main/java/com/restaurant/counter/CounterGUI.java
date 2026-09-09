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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CounterGUI {

    private static final AtomicInteger NEXT_ORDER_NUMBER =
            new AtomicInteger(1001);
    private static final DateTimeFormatter RECEIPT_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Map<String, Double> menu = new LinkedHashMap<>();
    private final Map<String, Integer> selectedProducts = new LinkedHashMap<>();
    private final ListView<String> orderList = new ListView<>();
    private final Label totalLabel = new Label("Total: $0.00");
    private final Spinner<Integer> quantitySpinner = new Spinner<>(1, 99, 1);
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
        Label title = new Label("Counter - Create Order");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button backButton = new Button("Back to Main Menu");
        backButton.setOnAction(event -> onBackToMainMenu.run());

        HBox header = new HBox(15, title, backButton);
        header.setAlignment(Pos.CENTER_LEFT);

        GridPane menuGrid = new GridPane();
        menuGrid.setHgap(10);
        menuGrid.setVgap(10);

        int column = 0;
        int row = 0;
        for (Map.Entry<String, Double> entry : menu.entrySet()) {
            Button productButton = new Button(
                    entry.getKey() + "\n$" + String.format("%.2f", entry.getValue()));
            productButton.setPrefSize(150, 60);
            productButton.setOnAction(event -> addProduct(entry.getKey()));
            menuGrid.add(productButton, column, row);

            column++;
            if (column == 2) {
                column = 0;
                row++;
            }
        }

        orderList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        orderList.setPrefHeight(260);
        orderList.setPlaceholder(new Label("No items added yet"));

        quantitySpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, 1));
        quantitySpinner.setPrefWidth(90);

        Button removeButton = new Button("Remove Selected");
        removeButton.setOnAction(event -> removeSelectedProduct());

        Button clearButton = new Button("Clear Order");
        clearButton.setOnAction(event -> clearOrder());

        Button createOrderButton = new Button("Create Order");
        createOrderButton.setDefaultButton(true);
        createOrderButton.setOnAction(event -> createReceipt(stage));

        HBox orderActions = new HBox(
                10,
                new Label("Quantity:"),
                quantitySpinner,
                removeButton,
                clearButton,
                createOrderButton
        );
        orderActions.setAlignment(Pos.CENTER_LEFT);

        VBox menuSection = new VBox(12, new Label("MENU"), menuGrid);
        VBox orderSection = new VBox(12, new Label("CURRENT ORDER"), orderList,
                totalLabel, orderActions);
        menuSection.setPadding(new Insets(10));
        orderSection.setPadding(new Insets(10));

        BorderPane content = new BorderPane();
        content.setTop(header);
        content.setLeft(menuSection);
        content.setCenter(orderSection);
        BorderPane.setMargin(header, new Insets(20, 20, 0, 20));

        BorderPane root = new BorderPane(content);
        root.setPadding(new Insets(10));
        Scene scene = new Scene(root, 900, 520);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.isAltDown() && event.isShiftDown()) {
                onBackToMainMenu.run();
                event.consume();
            }
        });
        return scene;
    }

    private void addProduct(String product) {
        int quantity = quantitySpinner.getValue();
        selectedProducts.put(product,
                selectedProducts.getOrDefault(product, 0) + quantity);
        quantitySpinner.getValueFactory().setValue(1);
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
