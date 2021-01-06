package ViewController;

import Model.Inventory;
import Model.Part;
import Model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.*;

/**
 * this class controller is used to add Products.
 * @author John Phan
 */
public class addProductController implements Initializable {


    private static ObservableList<Part> addProductParts = FXCollections.observableArrayList();


    @FXML private TableView<Part> partTable;
    @FXML private TableColumn<Part, Integer> partIDColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Integer> partInvColumn;
    @FXML private TableColumn<Part, Double> partCostColumn;

    @FXML private TableView<Part> partAddTable;
    @FXML private TableColumn<Part, Integer> partAddIDColumn;
    @FXML private TableColumn<Part, String> partAddNameColumn;
    @FXML private TableColumn<Part, Integer> partAddInvColumn;
    @FXML private TableColumn<Part, Double> partAddCostColumn;


    @FXML private TextField partSearchTextField;


    @FXML private TextField nameTextField;
    @FXML private TextField invTextField;
    @FXML private TextField priceTextField;
    @FXML private TextField maxTextField;
    @FXML private TextField minTextField;

    //started autoProductID at 500 due to having 5 test cases going up to 500
    public static int autoProductID = 500;

    @FXML
    private void addProductSaveButtonPushed(ActionEvent event) throws IOException {
        try {
            String productName = nameTextField.getText();
            int productID = autoProductID;
            int productInv = Integer.parseInt(invTextField.getText());
            double productPrice = Double.parseDouble(priceTextField.getText());
            int productMax = Integer.parseInt(maxTextField.getText());
            int productMin = Integer.parseInt(minTextField.getText());

            double partsTotal = 0;

            for (int i = 0; i < addProductParts.size(); i++) {
                partsTotal = partsTotal + addProductParts.get(i).getPrice();
            }

            //looking for errors
            if (productMin > productMax) {
                errorMessage(1);
            } else if (productInv < productMin) {
                errorMessage(2);
            } else if (productInv > productMax) {
                errorMessage(3);
            }
            else if (productPrice <= 0.00){
                errorMessage(9);
            }
            else if (partsTotal > productPrice) {
                errorMessage(8);
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("CONFIRMATION");
                alert.setHeaderText("Add product");
                alert.setContentText("create new product: " + productName + "?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    Product product = new Product(productID, productName, productPrice, productInv, productMin, productMax);
                    Inventory.addProduct(product);
                    for (int i = 0; i < addProductParts.size(); i++) {
                        Part part = addProductParts.get(i);
                        product.addAssociatedPart(part);
                    }

                    while (addProductParts.size() != 0) {
                        addProductParts.remove(addProductParts.size() - 1);
                    }

                    //returns back to main scene after adding part
                    Parent addPartCancelParent = FXMLLoader.load(getClass().getResource("inventoryM.fxml"));
                    Scene addPartCancelScene = new Scene(addPartCancelParent);

                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(addPartCancelScene);
                    window.show();
                }
            }

        } catch(NumberFormatException e)
        {
            errorMessage(4);
        }
    }


    //When this method is called changed scene back to main
    @FXML
    private void addProductCancelButtonPushed(ActionEvent event) throws IOException
    {
        while (addProductParts.size() != 0)
        {
            addProductParts.remove(addProductParts.size()-1);
        }
        Parent addProductCancelParent = FXMLLoader.load(getClass().getResource("inventoryM.fxml"));
        Scene addProductCancelScene = new Scene(addProductCancelParent);

        //This line gets the stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(addProductCancelScene);
        window.show();

    }

    //displays error message based on issue
    private void errorMessage(int i)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if(i == 1){
            alert.setTitle("Error");
            alert.setHeaderText("Add product failed");
            alert.setContentText("min cannot be greater than max");
        }
        else if(i == 2){
            alert.setTitle("Error");
            alert.setHeaderText("Add product failed");
            alert.setContentText("Inventory cannot be less than min");
        }
        else if(i == 3){
            alert.setTitle("Error");
            alert.setHeaderText("Add product failed");
            alert.setContentText("Inventory cannot be more than max");
        }
        else if(i == 4){
            alert.setTitle("Error");
            alert.setHeaderText("Add product failed");
            alert.setContentText("Part cannot be added, invalid values in one or more fields");
        }
        else if(i == 5){
            alert.setTitle("INFO");
            alert.setHeaderText("No part found");
            alert.setContentText("Part not found");
        }
        else if(i == 7){
            alert.setTitle("INFO");
            alert.setHeaderText("No part selected");
            alert.setContentText("Part not selected");
        }
        else if(i == 8){
            alert.setTitle("Error");
            alert.setHeaderText("Pricing Error");
            alert.setContentText("Parts cost more than product");
        }
        else if(i == 9){
            alert.setTitle("Error");
            alert.setHeaderText("Pricing Error");
            alert.setContentText("Product cannot cost 0.00 or less");
        }
        alert.showAndWait();
    }


    @FXML
    private void searchPart (ActionEvent event) {
        ObservableList<Part> foundParts = FXCollections.observableArrayList();

        //checks if ID input
        try {
            int searchPartID = Integer.parseInt(partSearchTextField.getText());
            Part fPart = Inventory.lookupPart(searchPartID);

            if (fPart == null) {
                errorMessage(5);
            } else {
                foundParts.add(fPart);
                partTable.setItems(foundParts);
            }
        }
        //checks if Name input
        catch (NumberFormatException e) {
            String searchPart = partSearchTextField.getText();
            foundParts = Inventory.lookupPart(searchPart);

            if (foundParts.isEmpty()) {
                errorMessage(5);
            } else {
                partTable.setItems(foundParts);
            }
        }
    }

    @FXML
    private void addPartButtonPushed(ActionEvent event) {
        boolean check = false;
        Part part = partTable.getSelectionModel().getSelectedItem();

        if(part == null){
            errorMessage(7);
            check = true;
        }
        if (check == false) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("CONFIRMATION");
            alert.setHeaderText("Add Part");
            alert.setContentText("add part to product?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
                addProductParts.add(part);
        }

    }


    @FXML
    private void removePartButtonPushed(ActionEvent event) {
        Part part = partAddTable.getSelectionModel().getSelectedItem();

        if (part == null){
            errorMessage(7);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("CONFIRMATION");
            alert.setHeaderText("Remove Part");
            alert.setContentText("Remove part from product?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
                addProductParts.remove(part);
        }
    }


        @Override
    public void initialize(URL url, ResourceBundle rb) {
        autoProductID = autoProductID + 100;

        partTable.setItems(Inventory.getAllParts());
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        partAddTable.setItems(addProductParts);
        partAddIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partAddNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partAddInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partAddCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

    }
}
