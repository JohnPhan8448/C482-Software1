package Main;

import Model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * main class that starts the java application.
 * javadoc is located in C:\Users\johnp\IdeaProjects\C482 Inventory Project\src\Javadoc
 * RUNTIME ERROR: ran into error where when trying to modify product, the product parts, would generate all parts from previous modifications.
 * the fix was that i had made the Observablelist a static, so it holding all information from previous modifications.  with static it created the correct
 * parts list corresponding to the Product. Another issue was with pulling index.  i first used the ID that was generated. but realized it would wrong due to index
 * starting at 0.  resolved by getting index before moving to modify part/product.
 *
 * FUTURE ENHANCEMENT: make changes to parts. so that if parts were to be deleted, you would need to check if it was used in any products. and visa versa to avoid
 * having parts that are not used, or products that we no longer carry parts for. Another thing we may want to add is to see which items have the best margins so that
 * the company can focus on what creates the most revenue. which products sell the best/least.
 */
public class InventoryManager extends Application {


    /**
     * starts the java application
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Inventory inv = new Inventory();
        addTestData(inv);


        //Load the FXML File
        Parent root = FXMLLoader.load(getClass().getResource("/ViewController/inventoryM.fxml"));

        //Build the Scene Graph

        Scene scene = new Scene(root);

        //Display our window using the scene graph
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    /**
     * this method creates the test data to test inventory system.
     * @param inv to create a starting inventory system
     */
    void addTestData(Inventory inv) {

        //In house
        Part a1 = new InHouse(1, "Part A1", 2.99,10,5,100,101);
        Part a2 = new InHouse(3, "Part A2", 4.99,11,5,100,103);
        Part b = new InHouse(2,"Part B", 3.99, 9, 5,100,102);
        inv.addPart(a1);
        inv.addPart(a2);
        inv.addPart(b);
        inv.addPart(new InHouse(4,"Part A3", 5.99,15,5,100,104));
        inv.addPart(new InHouse(5,"Part A4", 6.99,5,5,100,105));

        //Outsourced
        Part o = new Outsourced(6,"Part o1",2.99, 10,5,100,"ACME.Co.");
        Part p = new Outsourced(7,"Part p",3.99, 9,5,100,"ACME.Co.");
        Part q = new Outsourced(8,"Part q",2.99, 10,5,100,"FLORIDA.Co.");
        inv.addPart(o);
        inv.addPart(p);
        inv.addPart(q);
        inv.addPart(new Outsourced(9,"Part r",2.99, 10,5,100,"FLORIDA.Co."));
        inv.addPart(new Outsourced(10,"Part o2",2.99, 10,5,100,"NY.Co."));

        //Product
        Product prod1 = new Product(100,"Product 1", 9.99,20,5,100);
        prod1.addAssociatedPart(a1);
        prod1.addAssociatedPart(o);
        inv.addProduct(prod1);
        Product prod2 = new Product(200,"Product 2", 9.99,29,5,100);
        prod2.addAssociatedPart(a2);
        prod2.addAssociatedPart(p);
        inv.addProduct(prod2);
        Product prod3 = new Product(300,"Product 3", 9.99,30,5,100);
        prod3.addAssociatedPart(b);
        prod3.addAssociatedPart(q);
        inv.addProduct(prod3);
        inv.addProduct(new Product(400,"Product 4",29.99,20,5,100));
        inv.addProduct(new Product(500,"Product 5",29.99,9,5,100));


    }
}
