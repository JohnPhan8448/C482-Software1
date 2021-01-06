package Model;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

/**
 * this class is used for creating products
 * @author John Phan
 *
 */
public class Product {

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * this is used to create product
     * @param id to set id
     * @param name to set name
     * @param price to set price
     * @param stock to set stock
     * @param min to set min
     * @param max to set max
     */
    public Product (int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }
    /**
     * @param id the id to set
     * */
    public void setId(int id){
        this.id = id;
    }

    /**
     * @return  the id
     * */
    public int getId(){
        return id;
    }

    /**
     * @param name to set name
     * */
    public void setName(String name){
        this.name = name;
    }

    /**
     * @return the name
     * */
    public String getName(){
        return name;
    }

    /**
     * @param price the price to set
     * */
    public void setPrice(double price){
        this.price = price;
    }

    /**
     * @return the price
     * */
    public double getPrice(){
        return price;
    }

    /**
     * @param stock the stock to set
     * */
    public void setStock(int stock){
        this.stock = stock;
    }

    /**
     * @return the stock
     * */
    public int getStock(){
        return stock;
    }

    /**
     *
     * @param min the min to set
     */
    public void setMin(int min){
        this.min = min;
    }

    /**
     *
     * @return the min
     */
    public int getMin(){
        return min;
    }

    /**
     *
     * @param max the max to set
     */
    public void setMax(int max){
        this.max = max;
    }

    /**
     *
     * @return the max
     */
    public int getMax(){
        return max;
    }

    /**
     *
     * @param part to add associated part
     */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    /**
     *
     * @param selectedAssociatedPart to selected part to delete
     * @return delete confirmation
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        if(associatedParts.contains(selectedAssociatedPart)){
            associatedParts.remove(selectedAssociatedPart);
            return true;
        }
        return false;
    }

    /**
     *
     * @return associatedParts list
     */
    public ObservableList getAllAssociatedParts(){
        return this.associatedParts;
    }
}
