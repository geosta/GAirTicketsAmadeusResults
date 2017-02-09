package gr.gandg.george.gairticketsamadeusresults;

import java.util.ArrayList;

/**
 * Created by george on 9/2/2017.
 */

public class Itinerary {

    public ArrayList<Flight> outbound = new ArrayList<Flight>();
    public ArrayList<Flight> inbound = new ArrayList<Flight>();

    public double totalPrice;
    public double pricePerAdult;
    public double pricePerChild;
    public double pricePerInfant;
    public boolean refundable;
    public boolean changePenalties;

}
