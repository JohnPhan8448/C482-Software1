package Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * this class creates outsourced parts.
 */
public class Outsourced extends Part {

    protected StringProperty companyName;

    /**
     * this is the constructor to create outsourced parts.
     * @param id to set id
     * @param name to set name
     * @param price to set price
     * @param stock to set stock
     * @param min to set min
     * @param max to set max
     * @param companyName to set companyName
     */
    public Outsourced (int id,String name, double price, int stock, int min, int max, String companyName){
        super(id, name, price, stock, min, max);
        this.companyName = new SimpleStringProperty(companyName);
    }

    /**
     * @param name to set company name
     */
    public void setCompanyName(String name){
        this.companyName.set(name);
    }

    /**
     * @return the company name
     */
    public String getCompanyName(){
        return this.companyName.get();
    }
}
