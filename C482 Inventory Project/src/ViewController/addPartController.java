package ViewController;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import javafx.fxml.FXML;
import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;

/**
 * this class controller is used to add Parts.
 * @author John Phan
 */
public class addPartController implements Initializable {

    //FXML control declarations
    @FXML private RadioButton inHouseRb;
    @FXML private RadioButton outsourcedRb;
    @FXML private Label addPartLabel;
    @FXML private ToggleGroup addPartTg;
    @FXML private TextField nameTextField;
    @FXML private TextField invTextField;
    @FXML private TextField priceTextField;
    @FXML private TextField maxTextField;
    @FXML private TextField minTextField;
    @FXML private TextField machineIDTextField;


    //used to keep track of ID. static so that it persists with each part added
    //started autoID at 10 due to having 10 test cases
    public static int autoID = 10;

    /**
     * this method creates new parts.
     * @param event to create new part
     * @throws IOException when integer placed in string
     */
    @FXML
    private void addPartSaveButtonPushed (ActionEvent event) throws IOException
    {

        try {
            String partName = nameTextField.getText();
            int partInv = Integer.parseInt(invTextField.getText());
            double partPrice = Double.parseDouble(priceTextField.getText());
            int partMax = Integer.parseInt(maxTextField.getText());
            int partMin = Integer.parseInt(minTextField.getText());

            //looking for errors with min>max, inv<min, inv>max
            if (partMin > partMax)
            {
                errorMessage(1);
            }
            else if (partInv < partMin){
                errorMessage(2);
            }
            else if (partInv > partMax) {
                errorMessage(3);
            }
            else if(partPrice <= 0.00){
                errorMessage(5);
            }
            else {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("CONFIRMATION");
                alert.setHeaderText("Add part");
                alert.setContentText("create new part?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    //Checks if in house or outsourced selected then creates part.
                    if (this.addPartTg.getSelectedToggle().equals(this.inHouseRb)) {
                        int machineID = Integer.parseInt(machineIDTextField.getText());
                        InHouse part = new InHouse(autoID, partName, partPrice, partInv, partMin, partMax, machineID);
                        Inventory.addPart(part);


                        //returns back to main scene after adding part
                        Parent addPartCancelParent = FXMLLoader.load(getClass().getResource("inventoryM.fxml"));
                        Scene addPartCancelScene = new Scene(addPartCancelParent);

                        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        window.setScene(addPartCancelScene);
                        window.show();
                    } else {
                        Outsourced part = new Outsourced(autoID, partName, partPrice, partInv, partMin, partMax, machineIDTextField.getText());
                        Inventory.addPart(part);


                        //returns back to main scene after adding part
                        Parent addPartCancelParent = FXMLLoader.load(getClass().getResource("inventoryM.fxml"));
                        Scene addPartCancelScene = new Scene(addPartCancelParent);

                        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        window.setScene(addPartCancelScene);
                        window.show();
                    }
                }

            }
        }
        //if errors with non number values placed in price,inv,min,max
        catch (NumberFormatException e){
            errorMessage(4);
        }
    }

    /**
     * when called this method checks which radio button is selected.
     * Checks which button is selected and sets TextLabel.
     */
    //checks radio buttons for display text  machine id or company name
    @FXML
    private void radioButtonChanged()
    {
        if (this.addPartTg.getSelectedToggle().equals(this.inHouseRb))
            addPartLabel.setText("Machine ID");
        if(this.addPartTg.getSelectedToggle().equals(this.outsourcedRb))
            addPartLabel.setText("Company Name");
    }

    /**
     * displays input errors.
     * displays error message depending on error found.
     * @param i to display specified error message
     */
    @FXML
    private void errorMessage(int i)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("Add part failed");
        if(i == 1){
            alert.setContentText("min cannot be greater than max");
        }
        else if(i == 2){
            alert.setContentText("Inventory cannot be less than min");
        }
        else if(i == 3){
            alert.setContentText("Inventory cannot be more than max");
        }
        else if(i == 4){
            alert.setContentText("Part cannot be added, invalid values in one or more fields");
        }
        else if(i == 5){
            alert.setContentText("Price cannot be 0.00 or less");
        }
        alert.showAndWait();
    }

    /**
     * when called cancels adding part and returns to main scene.
     * @param event to cancel adding part and go back to main scene
     */
    @FXML
    private void addPartCancelButtonPushed(ActionEvent event) throws IOException
    {
        Parent addPartCancelParent = FXMLLoader.load(getClass().getResource("InventoryM.fxml"));
        Scene addPartCancelScene = new Scene(addPartCancelParent);

        //This line gets the stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(addPartCancelScene);
        window.show();
    }

    /**
     * initializes addPartController class.
     * radio buttons set up as a toggle group.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        autoID = autoID + 1;

        //add part radio button setup
        addPartTg = new ToggleGroup();
        this.inHouseRb.setToggleGroup(addPartTg);
        this.outsourcedRb.setToggleGroup(addPartTg);
        inHouseRb.setSelected(true);
        addPartLabel.setText("Machine ID");

    }


}
