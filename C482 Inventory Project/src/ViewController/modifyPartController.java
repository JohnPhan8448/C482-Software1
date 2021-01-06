package ViewController;

import Model.*;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * this class controller is used to modify Parts.
 * @author John Phan
 */
public class modifyPartController implements Initializable{

    Part part;

    //FXML control declarations
    @FXML private TextField idTextField;
    @FXML private TextField nameTextField;
    @FXML private TextField invTextField;
    @FXML private TextField priceTextField;
    @FXML private TextField maxTextField;
    @FXML private TextField minTextField;
    @FXML private TextField machineIDTextField;

    @FXML private RadioButton inHouseRb;
    @FXML private RadioButton outsourcedRb;
    @FXML private Label modifyPartLabel;
    @FXML private ToggleGroup modifyPartTg;

    //used to display ID and to get index of part
    protected int autoID;
    protected int modPartIndex = inventoryManagerController.modIndex;


    /**
     *initializes data for modify part scene
     * displays information of the part to be modified
     * @param part to get information on part to be modified
     */
    public void initData(Part part)
    {
        this.part = part;

        //sets up passed information base on if in house or outsourced
        if (part instanceof InHouse) {
            inHouseRb.setSelected(true);
            modifyPartLabel.setText("Machine ID");
            idTextField.setText(Integer.toString(part.getId()));
            nameTextField.setText(part.getName());
            invTextField.setText(Integer.toString(part.getStock()));
            priceTextField.setText(Double.toString(part.getPrice()));
            maxTextField.setText(Integer.toString(part.getMax()));
            minTextField.setText(Integer.toString(part.getMin()));
            machineIDTextField.setText(Integer.toString(((InHouse) part).getMachineID()));
        }
        if (part instanceof Outsourced){
            outsourcedRb.setSelected(true);
            modifyPartLabel.setText("Company Name");
            idTextField.setText(Integer.toString(part.getId()));
            nameTextField.setText(part.getName());
            invTextField.setText(Integer.toString(part.getStock()));
            priceTextField.setText(Double.toString(part.getPrice()));
            maxTextField.setText(Integer.toString(part.getMax()));
            minTextField.setText(Integer.toString(part.getMin()));
            machineIDTextField.setText(((Outsourced) part).getCompanyName());
        }
    }

    /**
     * this method is to save updated part.
     * @param event to save part
     */
    @FXML
    private void savePartButtonPushed(ActionEvent event) throws IOException{

        Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirmation.setTitle("Update parts");
        alertConfirmation.setHeaderText("Update parts");
        alertConfirmation.setContentText("are you sure you want to update?");

        Optional<ButtonType> result = alertConfirmation.showAndWait();
        if (result.get() == ButtonType.OK) {

            try {
                autoID = Integer.parseInt(idTextField.getText());
                String name = nameTextField.getText();
                double price = Double.parseDouble(priceTextField.getText());
                int inv = Integer.parseInt(invTextField.getText());
                int min = Integer.parseInt(minTextField.getText());
                int max = Integer.parseInt(maxTextField.getText());

                //looking for errors with min>max, inv<min, inv>max
                if (min > max) {
                    errorMessage(1);
                } else if (inv < min) {
                    errorMessage(2);
                } else if (inv > max) {
                    errorMessage(3);
                }
                else if (price <= 0.00)
                {
                    errorMessage(5);
                }
                else {
                    //updates depending on in house or outsourced
                    if (this.modifyPartTg.getSelectedToggle().equals(this.inHouseRb)) {
                        int machineID = Integer.parseInt(machineIDTextField.getText());
                        InHouse part = new InHouse(autoID, name, price, inv, min, max, machineID);
                        Inventory.updatePart(modPartIndex, part);
                    }
                    else {
                        String companyName = machineIDTextField.getText();
                        Outsourced part = new Outsourced(autoID, name, price, inv, min, max,companyName);
                        Inventory.updatePart(modPartIndex, part);
                    }
                    Parent modifyPartCancelParent = FXMLLoader.load(getClass().getResource("inventoryM.fxml"));
                    Scene addPartCancelScene = new Scene(modifyPartCancelParent);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(addPartCancelScene);
                    window.show();
                }

            }
            //displays error if number/string input is wrong.
            catch (NumberFormatException e) {
                errorMessage(4);
            }
        }
    }


    /**
     * when called cancels modifying part and returns to main scene.
     * @param event to cancel modifying part and go back to main scene
     */
    @FXML
    private void modifyPartCancelButtonPushed(ActionEvent event) throws IOException
    {
        Parent modifyPartCancelParent = FXMLLoader.load(getClass().getResource("inventoryM.fxml"));
        Scene addPartCancelScene = new Scene(modifyPartCancelParent);

        //This line gets the stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(addPartCancelScene);
        window.show();

    }

    /**
     * when called this method checks which radio button is selected.
     * Checks which button is selected and sets TextLabel.
     */
    @FXML
    private void radioButtonChanged()
    {
        if (this.modifyPartTg.getSelectedToggle().equals(this.inHouseRb))
            modifyPartLabel.setText("Machine ID");
        if(this.modifyPartTg.getSelectedToggle().equals(this.outsourcedRb))
            modifyPartLabel.setText("Company Name");
    }

    /**
     * displays input errors.
     * displays error message depending on error found.
     * @param i to display specified error message
     */
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
     * initializes modifyPartController class.
     * radio buttons set up as a toggle group.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        //add part radio button setup
        modifyPartTg = new ToggleGroup();
        this.inHouseRb.setToggleGroup(modifyPartTg);
        this.outsourcedRb.setToggleGroup(modifyPartTg);
        inHouseRb.setSelected(true);
        modifyPartLabel.setText("Machine ID");
    }
}
