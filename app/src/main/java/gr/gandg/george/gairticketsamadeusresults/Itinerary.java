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

    @Override
    public String toString() {
        StringBuffer txt = new StringBuffer("");
        Itinerary it = this;
        txt.append("Αναχώφηση: \n");
        for (int j=0; j<it.outbound.size(); j++) {
            Flight f = it.outbound.get(j);
            txt.append(f.departsAt);
            txt.append(" ");
            txt.append(f.originAirport);
            txt.append("->");
            txt.append(f.arrivesAt);
            txt.append(" ");
            txt.append(f.destinationAirport);
            txt.append(f.marketingAirline + "\n");
        }
        txt.append("Επιστροφή: \n");
        for (int j=0; j<it.inbound.size(); j++) {
            Flight f = it.inbound.get(j);
            txt.append(f.departsAt);
            txt.append(" ");
            txt.append(f.originAirport);
            txt.append("->");
            txt.append(f.arrivesAt);
            txt.append(" ");
            txt.append(f.destinationAirport);
            txt.append(" ");
            txt.append(f.marketingAirline + "\n");

        }
        txt.append("Τιμή: ");
        txt.append(it.totalPrice);
        txt.append("\n");

        return txt.toString();
    }
}
