package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class is used to create Inventory.
 * Holds both parts and products ObservableLists
 * @author John Phan
 */
public class Inventory
{

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     *
     * @param part to add part
     */
    public static void addPart(Part part)
    {
        allParts.add(part);
    }

    /**
     *
     * @param product to add product
     */
    public static void addProduct(Product product) { allProducts.add(product); }

    /**
     *
     * @return the Parts List
     */
    public static ObservableList<Part> getAllParts()
    {
        return allParts;
    }

    /**
     *
     * @return the Product List
     */
    public static ObservableList<Product> getAllProducts(){ return allProducts;}

    /**
     * when called searches for part using ID.
     * @param partID to search with part id
     * @return the part
     */
    public static Part lookupPart (int partID)
    {
        for (int i = 0; i < allParts.size(); i++){
            if(allParts.get(i).getId() == partID){
                return allParts.get(i);
            }
        }
        return null;
    }

    /**
     * when called searches for product using ID.
     * @param productID to search with product id;
     * @return the product
     */
    public static Product lookupProduct(int productID)
    {
        for(int i = 0; i < allProducts.size(); i++){
            if(allProducts.get(i).getId() == productID){
                return allProducts.get(i);
            }
        }
        return null;
    }

    /**
     * when called searches for part using name.
     * @param partName to search for part with name
     * @return the part
     */
    public static ObservableList<Part> lookupPart (String partName)
    {
        ObservableList<Part> found = FXCollections.observableArrayList();
        for ( int i = 0; i < allParts.size(); i++){
            if(allParts.get(i).getName().toLowerCase().contains(partName.toLowerCase())){
                found.add(allParts.get(i));
            }
        }
        return found;
    }


    /**
     * when called searches for product using name.
     * @param productName to search for product with name
     * @return the product
     */
    public static ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> found = FXCollections.observableArrayList();
        for(int i = 0; i < allProducts.size(); i++){
            if(allProducts.get(i).getName().toLowerCase().contains(productName.toLowerCase())){
                found.add(allProducts.get(i));
            }
        }
        return found;
    }

    /**
     * when called updates part.
     * @param index to find index of updated part
     * @param selectedPart to update selected part
     */
    public static void updatePart(int index, Part selectedPart)
    {
        allParts.set(index, selectedPart);
    }

    /**
     * when called updates selected product.
     * @param index to find index of updated product
     * @param selectedProduct to update selected product
     */
    public static void updateProduct(int index, Product selectedProduct){
        allProducts.set(index, selectedProduct);
    }

    /**
     * when called delete selected part.
     * @param selectedPart to delete selected part
     * @return the deleted part
     */
    public static boolean deletePart(Part selectedPart)
    {
        return allParts.remove(selectedPart);
    }

    /**
     * when called delete selected product
     * @param selectedProduct to delete selected product
     * @return the deleted product
     */
    public static boolean deleteProduct(Product selectedProduct){
        return allProducts.remove(selectedProduct);
    }


}
