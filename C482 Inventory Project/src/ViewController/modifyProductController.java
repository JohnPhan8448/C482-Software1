package ViewController;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import Model.Product;
import Model.Inventory;
import Model.Part;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * this class controller is used to modify Products.
 * @author John Phan
 */
public class modifyProductController implements Initializable {

    Product product;

    private ObservableList<Part> addProductParts = FXCollections.observableArrayList();

    //FXML control declarations
    @FXML private TextField partSearchTextField;

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

    @FXML private TextField idTextField;
    @FXML private TextField nameTextField;
    @FXML private TextField invTextField;
    @FXML private TextField priceTextField;
    @FXML private TextField maxTextField;
    @FXML private TextField minTextField;

    //index for product from inventory manager controller
    protected int modProductIndex = inventoryManagerController.prodIndex;


    /**
     * initializes data  for the modify product scene.
     * initializes data and displays information for product selected, parts list, and parts in products.
     * @param product to fill in information to modify product
     */
    public void initData(Product product)
    {
        this.product = product;
        addProductParts = product.getAllAssociatedParts();

        //part table
        partTable.setItems(Inventory.getAllParts());
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        //current parts in product
        partAddTable.setItems(addProductParts);
        partAddIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partAddNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partAddInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partAddCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        //sets information in text fields.
        idTextField.setText(Integer.toString(product.getId()));
        nameTextField.setText(product.getName());
        invTextField.setText(Integer.toString(product.getStock()));
        priceTextField.setText(Double.toString(product.getPrice()));
        maxTextField.setText(Integer.toString(product.getMax()));
        minTextField.setText(Integer.toString(product.getMin()));
    }

    /**
     * when called updates product information.
     * checks for any errors before updating and returning to main scene.
     * @param event to update product
     * @throws IOException
     */
    @FXML
    private void saveProductButtonPushed(ActionEvent event) throws IOException {
        try {
            String productName = nameTextField.getText();
            int productID = product.getId();
            int productInv = Integer.parseInt(invTextField.getText());
            double productPrice = Double.parseDouble(priceTextField.getText());
            int productMax = Integer.parseInt(maxTextField.getText());
            int productMin = Integer.parseInt(minTextField.getText());
            double partsTotal = 0;

            //adds up costs of all parts
            for (int i = 0; i < addProductParts.size(); i++) {
                partsTotal = partsTotal + addProductParts.get(i).getPrice();
            }

            //looking for errors with min>max, inv<min, inv>max, and parts costs > product price
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
                alert.setHeaderText("Save Changes");
                alert.setContentText("Save changes to " + product.getName() + "?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {

                    //updates product inventory and returns to main scene
                    Product product = new Product(productID, productName, productPrice, productInv, productMin, productMax);
                    Inventory.updateProduct(modProductIndex, product);
                    for (int i = 0; i < addProductParts.size(); i++) {
                        Part part = addProductParts.get(i);
                        product.addAssociatedPart(part);
                    }
                    Parent modifyProductCancelParent = FXMLLoader.load(getClass().getResource("inventoryM.fxml"));
                    Scene modifyProductCancelScene = new Scene(modifyProductCancelParent);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(modifyProductCancelScene);
                    window.show();
                }
            }
        } catch (NumberFormatException e) {
            errorMessage(4);
        }
    }

    /**
     * when called add part to products parts.
     * checks if nothing is selected before adding. has confirmation before adding.
     * @param event to add part to product parts.
     */
    @FXML
    private void addPartButtonPushed(ActionEvent event){

        boolean check = false;
        Part part = partTable.getSelectionModel().getSelectedItem();

        //checks if anything is selected
        if(part == null){
            errorMessage(7);
            check = true;
        }
        if (!check) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("CONFIRMATION");
            alert.setHeaderText("Add Part");
            alert.setContentText("add part to " + product.getName() + "?");

            //add to product parts table
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
                addProductParts.add(part);
        }

    }

    /**
     * when called removes part from products list of parts.
     * checks if nothing is selected before removing. has confirmation before removing.
     * @param event to remove part from product parts
     */
    @FXML
    private void removePartButtonPushed(ActionEvent event){
        Part part = partAddTable.getSelectionModel().getSelectedItem();

        //check if anything is selected
        if (part == null){
            errorMessage(7);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("CONFIRMATION");
            alert.setHeaderText("Remove Part");
            alert.setContentText("Remove part from " + product.getName() + "?");

            //removes part
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
                addProductParts.remove(part);
        }

    }

    /**
     * when called searches for parts, from parts list.
     * checks input: ID or Name and runs search.
     * @param event to search for parts
     */
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

    /**
     * when called cancels modifying a product and returns to main scene.
     * @param event to cancel modifying product and go back to main scene
     * @throws IOException
     */
    @FXML
    private void modifyProductCancelButtonPushed(ActionEvent event) throws IOException
    {

        Parent modifyProductCancelParent = FXMLLoader.load(getClass().getResource("inventoryM.fxml"));
        Scene modifyProductCancelScene = new Scene(modifyProductCancelParent);

        //This line gets the stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(modifyProductCancelScene);
        window.show();

    }


    /**
     * displays input errors.
     * displays error message depending on error found.
     * @param i to display specified error message
     */
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
