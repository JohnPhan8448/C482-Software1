package ViewController;

import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.net.URL;
import Model.Product;
import Model.Inventory;
import Model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * this Class controls main inventory screen
 * @author John Phan
 */
public class inventoryManagerController implements Initializable {


    //to find selected index for part and product
    public static int modIndex;
    public static int prodIndex;

    //FXML control declarations
    @FXML private TextField partSearchTextField;
    @FXML private TextField productSearchTextField;

    @FXML private TableView<Part> partTable;
    @FXML private TableColumn<Part, Integer> partIDColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Integer> partInvColumn;
    @FXML private TableColumn<Part, Double> partCostColumn;

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> productIDColumn;
    @FXML private TableColumn<Product, String> productNameColumn;
    @FXML private TableColumn<Product, Integer> productInvColumn;
    @FXML private TableColumn<Product, Double> productCostColumn;


    /**
     * when called change to add part scene.
     * @param event to change scenes to addPart.fxml
     * @throws IOException
     */
    @FXML
    private void addPartButtonPushed(ActionEvent event) throws IOException
    {
        Parent addPartParent = FXMLLoader.load(getClass().getResource("addPart.fxml"));
        Scene addPartScene = new Scene(addPartParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(addPartScene);
        window.show();
    }

    /**
     * when called change scene to modify Part scene.
     * passes information from selected part to modify part scene.
     * @param event to change scenes to modifyPart.fxml
     * @throws IOException
     */
    @FXML
    private void modifyPartButtonPushed(ActionEvent event) throws IOException
    {
        modIndex = Inventory.getAllParts().indexOf(partTable.getSelectionModel().getSelectedItem());
        Part part = partTable.getSelectionModel().getSelectedItem();

        //check to see if a part is selected. if not will return error
        if (part == null) {
            errorMessage(2);
        }
        else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("modifyPart.fxml"));
            Parent modifyPartParent = loader.load();
            Scene modifyPartScene = new Scene(modifyPartParent);

            //gets modifyPartController and initialize data
            modifyPartController controller = loader.getController();
            controller.initData(part);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(modifyPartScene);
            stage.show();
        }
    }

    /**
     * when called change scene to add Product scene.
     * @param event to change scene to addProduct.fxml
     * @throws IOException
     */
    //When this method is called changed scene to addProduct.fxml
    @FXML
    private void addProductButtonPushed(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("addProduct.fxml"));
        Parent addProductParent = loader.load();
        Scene addProductScene = new Scene(addProductParent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(addProductScene);
        stage.show();
    }

    /**
     * when called change scene to modify product scene.
     * passes information from selected product to modify product scene.
     * @param event to change scene to modifyProduct.fxml
     * @throws IOException
     */
    @FXML
    private void modifyProductButtonPushed(ActionEvent event) throws IOException
    {
        prodIndex = Inventory.getAllProducts().indexOf(productTable.getSelectionModel().getSelectedItem());
        Product product = productTable.getSelectionModel().getSelectedItem();

        //checks to see if product has been selected
        if (product == null){
            errorMessage(2);
        }
        else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("modifyProduct.fxml"));
            Parent modifyProductParent = loader.load();
            Scene modifyProductScene = new Scene(modifyProductParent);

            modifyProductController controller = loader.getController();
            controller.initData(product);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(modifyProductScene);
            stage.show();
        }
    }

    /**
     * when called deletes selected product.
     * checks if empty or there are associated parts before deletion.
     * @param event to delete selected product
     */
    @FXML
    private void deleteProductButton(ActionEvent event){
        Product product = productTable.getSelectionModel().getSelectedItem();

        //check if product selected and if there are any associated parts before confirmation of deletion.
        if(product == null){
            errorMessage(3);
        }
        else{
            if (!product.getAllAssociatedParts().isEmpty()){
                errorMessage(4);
            }else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Product Deletion");
                alert.setHeaderText("Delete");
                alert.setContentText("Do you want to delete product: " + product.getName() + "?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    Inventory.deleteProduct(product);
                }
            }
        }
    }

    /**
     * when called deletes selected part.
     * checks if empty before deletion.
     * @param event to delete selected part
     */
    @FXML
    private void deletePartButton(ActionEvent event) {

        Part part = partTable.getSelectionModel().getSelectedItem();

        //check to see if part selected, if not will return error
        if (part == null) {
            errorMessage(2);
        }

        //checks for confirmation, before deletion.
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Part Deletion");
            alert.setHeaderText("Delete");
            alert.setContentText("Do you want to delete part: " + part.getName() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Inventory.deletePart(part);
            }
        }
    }

    /**
     * when called searches for selected part.
     * checks to see if input is ID or Name before executing search.
     * @param event to search for selected part
     */
    @FXML
    private void searchPart (ActionEvent event)
    {
        ObservableList<Part> foundParts = FXCollections.observableArrayList();

        //checks if ID input
        try {
            int searchPartID = Integer.parseInt(partSearchTextField.getText());
            Part fPart = Inventory.lookupPart(searchPartID);

            if (fPart == null){
                errorMessage(1);
            } else {
                foundParts.add(fPart);
                partTable.setItems(foundParts);
            }
        }
        //catch if Name input
        catch (NumberFormatException e){
            String searchPart = partSearchTextField.getText();
            foundParts = Inventory.lookupPart(searchPart);

            if (foundParts.isEmpty()) {
                errorMessage(1);
            } else {
                partTable.setItems(foundParts);
            }
        }
    }

    /**
     * when called searches for selected product.
     * checks to see if input is ID or Name before executing search.
     * @param event to search for selected product
     */
    @FXML
    private void searchProduct(ActionEvent event){
            ObservableList<Product> foundProduct = FXCollections.observableArrayList();

            //checks if ID input
            try {
                int searchProductID = Integer.parseInt(productSearchTextField.getText());
                Product fProduct = Inventory.lookupProduct(searchProductID);

                if (fProduct == null){
                    errorMessage(3);
                } else {
                    foundProduct.add(fProduct);
                    productTable.setItems(foundProduct);
                }
            }
            //catch if Name input
            catch (NumberFormatException e){
                String searchProduct = productSearchTextField.getText();
                foundProduct = Inventory.lookupProduct(searchProduct);

                if (foundProduct.isEmpty()) {
                    errorMessage(1);
                } else {
                    productTable.setItems(foundProduct);
                }
            }

    }

    /**
     * when called exits program.
     * @param event to exit program
     */
    //exits program when exit button pushed
    @FXML
    private void exitProgramButton(ActionEvent event) throws IOException
    {
        Platform.exit();
    }

    /**
     * displays input errors.
     * displays error message depending on error found.
     * @param i to display specified error message
     */
    private void errorMessage(int i){

        //search no part found
        if(i == 1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INFO");
            alert.setHeaderText("No Part found");
            alert.setContentText("Part not found");
            alert.showAndWait();      }
        //no part selected
        else if (i == 2) {
            Alert alertNothing = new Alert(Alert.AlertType.ERROR);
            alertNothing.setTitle("ERROR");
            alertNothing.setHeaderText("Nothing selected");
            alertNothing.setContentText("A part was not selected");
            alertNothing.showAndWait();
        }
        else if (i == 3)
        {
            Alert alertNothing = new Alert(Alert.AlertType.ERROR);
            alertNothing.setTitle("ERROR");
            alertNothing.setHeaderText("Nothing selected");
            alertNothing.setContentText("A product was not selected");
            alertNothing.showAndWait();
        }
        else if (i == 4)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Cannot Delete");
            alert.setContentText("Parts are associated with product");
            alert.showAndWait();
        }
    }

    /**
     * initializes controller class.
     * displays test data from InventoryManager.java
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        partTable.setItems(Inventory.getAllParts());
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTable.setItems(Inventory.getAllProducts());
        productIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

    }


}
