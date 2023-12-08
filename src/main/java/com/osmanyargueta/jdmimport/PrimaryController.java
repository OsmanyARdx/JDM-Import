package com.osmanyargueta.jdmimport;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;

public class PrimaryController {

    @FXML
    Label dealershipLabel;

    @FXML
    ListView carListView;
    @FXML
    Label yearLabel;
    @FXML
    Label makeLabel;
    @FXML
    Label modelLabel;
    @FXML
    Label colorLabel;
    @FXML
    Label odoLabel;
    @FXML
    Label priceLabel;
    @FXML
    ImageView imageView;
    @FXML
    Button buyButton;
    @FXML
    TextField nameTextField;

    @FXML
    ImageView imageView2;

    String databaseURL = "";
    Connection conn = null;
    String sql = "";
    PreparedStatement preparedStatement = null;
    File choosen = null;
    File temp = null;

    ObservableList<JDMCar> carList;
    List<JDMCar> listOfCars = new LinkedList();
    ExecutorService exec = null;
    JDMCar selectedCar = null;

    /**
     * Method for initializing the app
     */
    @FXML
    public void initialize() {
        System.out.println("INITIALIZING...");

        carList = carListView.getItems();
        carList.clear();

        buyButton.disableProperty().set(true);
        exec = Executors.newFixedThreadPool(3);
    }

    /**
     * Method used to open a fileChooser and get a dealer database file
     */
    @FXML
    public void openFile() {
        try {
            System.out.println(Thread.currentThread().getName());
            FileChooser openFile = new FileChooser();
            temp = new File(new File(".").getCanonicalPath());
            openFile.setTitle("Open");
            openFile.setInitialDirectory(temp);
            openFile.getExtensionFilters().add(new ExtensionFilter("accdb (*.accdb)", "*.accdb"));
            choosen = openFile.showOpenDialog(null);

            Thread t1 = new Thread(() -> loadFile());
            exec.submit(t1);

        } catch (IOException ex) {
            System.out.println("Temp file not created.");
        }

    }

    /**
     * Method that will load the selected file, from the main thread, within a
     * different thread
     */
    public void loadFile() {
        if (choosen != null) {
            try {
                System.out.println(Thread.currentThread().getName());
                Platform.runLater(() -> dealershipLabel.setText("Dealership: " + choosen.getName().replaceAll(".accdb", "").toUpperCase()));
                System.out.println("jdbc:ucanaccess://.//" + choosen.getName());
                databaseURL = "jdbc:ucanaccess://.//" + choosen.getName();
                conn = DriverManager.getConnection(databaseURL);
            } catch (SQLException ex) {
                System.out.println("Connection failed!");
            }
            Platform.runLater(() -> carList.clear());
            try {
                String tableName = "carTable";
                Statement stmt = conn.createStatement();
                ResultSet result = stmt.executeQuery("select * from " + tableName);
                while (result.next()) {
                    if (result.getString("owner") == null) {
                        JDMCar current = new JDMCar(result.getInt("year"), result.getString("make"), result.getString("model"), result.getString("color"), result.getLong("odometer"), result.getDouble("price"), result.getString("owner"), result.getString("url"));
                        Platform.runLater(() -> carList.add(current));
                        Platform.runLater(() -> listOfCars.add(current));
                    }
                }
            } catch (SQLException e) {
                System.out.println("could not read from table");
            }
        }
    }

    /**
     * Method to show the info of the car that is selected in the listView
     */
    @FXML
    public void showCarInfo() {
        if (!carListView.getSelectionModel().isEmpty()) {
            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    selectedCar = (JDMCar) carListView.getSelectionModel().getSelectedItem();
                    Platform.runLater(() -> yearLabel.setText(String.valueOf(selectedCar.getYear())));
                    Platform.runLater(() -> makeLabel.setText(selectedCar.getMake()));
                    Platform.runLater(() -> modelLabel.setText(selectedCar.getModel()));
                    Platform.runLater(() -> colorLabel.setText(selectedCar.getColor()));
                    Platform.runLater(() -> odoLabel.setText(String.valueOf(selectedCar.getOdometer())));
                    Platform.runLater(() -> priceLabel.setText("$ " + String.valueOf(selectedCar.getPrice())));
                    Platform.runLater(() -> imageView.setImage(selectedCar.getCarImage()));
                    nameTextField.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                            buyButton.disableProperty().set(false);
                        }
                    });
                }
            });
            t2.start();
        }
    }

    /**
     * Method to show alerts for confirmation before buying a car
     */
    @FXML
    public void showAlert() {
        if (!modelLabel.getText().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Purchase Confirmation");
            alert.setContentText("Are you sure you want to purchase this " + modelLabel.getText() + "?");
            Optional<ButtonType> confirmation = alert.showAndWait();
            if (confirmation.get().equals(ButtonType.OK)) {
                buy();
                Alert receiptAlert = new Alert(Alert.AlertType.CONFIRMATION);
                receiptAlert.setContentText("Congratulations!!! - Your car will arrive in 2 weeks.\nCreate reciept file?");
                Optional<ButtonType> confirmation2 = receiptAlert.showAndWait();
                if (confirmation2.get().equals(ButtonType.OK)) {
                    createReceipt();
                }
            } else {
                Alert cancelAlert = new Alert(Alert.AlertType.CONFIRMATION);
                cancelAlert.setTitle("Canceled");
                cancelAlert.setContentText("Purchase has been canceled");
                cancelAlert.show();
            }
        }
    }

    /**
     * Method used when you buy a car, will update the owner string in the
     * database of the car selected and refresh the car info gui
     */
    public void buy() {
        System.out.println("Bought Car!");

        try {
            PreparedStatement stmt = conn.prepareStatement("UPDATE carTable SET owner = ? WHERE model = ?");
            stmt.setString(1, nameTextField.getText());
            stmt.setString(2, selectedCar.getModel());
            stmt.execute();
            carListView.getItems().remove(selectedCar);

            imageView.setImage(new Image("https://w7.pngwing.com/pngs/296/544/png-transparent-multicolored-congratulations-dunottar-school-youtube-competition-s-congratulations-icon-miscellaneous-text-logo-thumbnail.png"));

            ScaleTransition scaleRect = new ScaleTransition(Duration.seconds(1.5), imageView);
            scaleRect.setByX(1);
            scaleRect.setCycleCount(2);
            scaleRect.autoReverseProperty().set(true);
            scaleRect.play();
            scaleRect.setOnFinished(event -> {
                showCarInfo();
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to create a receipt file in a .txt file
     */
    public void createReceipt() {
        System.out.println("Reciept here");
    }

    /**
     * Method to close the application and all threads
     */
    @FXML
    public void closeApp() {
        Platform.exit();
        exec.shutdown();
    }
}
