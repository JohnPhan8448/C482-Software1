package Model;

import javafx.beans.property.SimpleIntegerProperty;


/**
 * this class creates inhouse parts
 */
public class InHouse extends Part {
    protected SimpleIntegerProperty machineID;

    /**
     * constructor class for in house parts.
     * @param id to set id
     * @param name to set name
     * @param price to set price
     * @param stock to set stock
     * @param min to set min
     * @param max to set max
     * @param machineID to set machineID
     */
    public InHouse(int id,String name, double price, int stock, int min, int max, int machineID)
    {
        super(id, name, price, stock, min, max);
        this.machineID = new SimpleIntegerProperty(machineID);


    }

    /**
     * this sets machineID.
     * @param machineID to set machineID
     */
    public void setMachineID(int machineID)
    {
        this.machineID.set(machineID);
    }

    /**
     * this returns machineID.
     * @return the machineID
     */
    public int getMachineID()
    {
        return this.machineID.get();
    }
}
