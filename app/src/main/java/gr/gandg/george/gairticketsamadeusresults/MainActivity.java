package gr.gandg.george.gairticketsamadeusresults;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String amadeusKey;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textView);
        amadeusKey = BuildConfig.AMADEUS_API_KEY;

        (new FlightsParser()).execute();
    }



    private class FlightsParser extends AsyncTask<String, Void, ArrayList<Itinerary>> {

        private static final String LOG_TAG = "FlightsParser";

        @Override
        protected ArrayList<Itinerary> doInBackground(String... params) {



            String origin = "SKG";
            String destination = "LON";
            String departure_date = "2017-03-01";
            String return_date = "2017-03-05";
            int adults = 2, children = 1, infants =1;

            StringBuffer sUrl = new StringBuffer("");
            sUrl.append("http://api.sandbox.amadeus.com/v1.2/flights/low-fare-search?apikey=");
            sUrl.append(amadeusKey);
            sUrl.append("&origin=");
            sUrl.append(origin);
            sUrl.append("&destination=");
            sUrl.append(destination);
            sUrl.append("&departure_date=");
            sUrl.append(departure_date);
            sUrl.append("&return_date=");
            sUrl.append(return_date);
            sUrl.append("&adults");
            sUrl.append(adults);
            sUrl.append("children");
            sUrl.append(children);
            sUrl.append("infants");
            sUrl.append(infants);
            sUrl.append("&nonstop=true");
            sUrl.append("&max_price=5000");
            sUrl.append("currency=EUR");
            sUrl.append("number_of_results=2");

            String theUrl = sUrl.toString();

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String flightsJsonStr = null;



            try {

                Log.i(LOG_TAG, theUrl);
                URL url = new URL(theUrl);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                flightsJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error " + e);
            } catch (Exception  e) {
                Log.e(LOG_TAG, e.getMessage() + e);
                e.printStackTrace();

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream" + e);
                    }
                }
            }

            return parseJson(flightsJsonStr);
        }

        /**
         * Parses the Json Result to an ArrayList of Itinerary objects
        */
        private ArrayList<Itinerary> parseJson(String json) {
            ArrayList<Itinerary>results = new ArrayList<Itinerary>();
            try {
                JSONObject amadeusResponse = new JSONObject(json);
                JSONArray amadeusResults = amadeusResponse.getJSONArray("results");
                for (int i=0; i< amadeusResults.length();i++) {
                    Itinerary itinerary = new Itinerary();

                    JSONObject amadeusResult = amadeusResults.getJSONObject(i);
                    JSONArray amadeusItineraries = amadeusResult.getJSONArray("itineraries");
// --- outbound
                    JSONObject amadeusOutbound = amadeusItineraries.getJSONObject(0).getJSONObject("outbound");
                    JSONArray amadeusOutboundFlights = amadeusOutbound.getJSONArray("flights");

                    // outbound flights
                    for (int j=0; j<amadeusOutboundFlights.length(); j++) {
                        JSONObject amadeusFlight = amadeusOutboundFlights.getJSONObject(j);
                        Flight flight = new Flight();
                        flight.departsAt = amadeusFlight.getString("departs_at");
                        flight.arivesAt = amadeusFlight.getString("arives_at");
                        flight.originAirport = amadeusFlight.getJSONObject("origin").getString("airport");
                        flight.destinationAirport = amadeusFlight.getJSONObject("destination").getString("airport");
                        flight.marketingAirline = amadeusFlight.getString("marketring_airline");
                        flight.operatingAirline = amadeusFlight.getString("operationg_airline");
                        flight.flightNumber = amadeusFlight.getString("flight_number");
                        flight.aircraft = amadeusFlight.getString("aircraft");
                        flight.travelClass = amadeusFlight.getJSONObject("booking_info").getString("travel_class");
                        flight.bookingCode = amadeusFlight.getJSONObject("booking_info").getString("booking_code");
                        flight.seatsRemaining = amadeusFlight.getJSONObject("booking_info").getInt("seats_remaining");
                        itinerary.outbound.add(flight);
                    }


// --- inbound

                    JSONObject amadeusInbound = amadeusItineraries.getJSONObject(0).getJSONObject("inbound");
                    JSONArray amadeusIboundFlightes = amadeusOutbound.getJSONArray("flights");

                    // inbound flights
                    for (int j=0; j<amadeusIboundFlightes.length(); j++) {
                        JSONObject amadeusFlight = amadeusOutboundFlights.getJSONObject(j);
                        Flight flight = new Flight();
                        flight.departsAt = amadeusFlight.getString("departs_at");
                        flight.arivesAt = amadeusFlight.getString("arives_at");
                        flight.originAirport = amadeusFlight.getJSONObject("origin").getString("airport");
                        flight.destinationAirport = amadeusFlight.getJSONObject("destination").getString("airport");
                        flight.marketingAirline = amadeusFlight.getString("marketring_airline");
                        flight.operatingAirline = amadeusFlight.getString("operationg_airline");
                        flight.flightNumber = amadeusFlight.getString("flight_number");
                        flight.aircraft = amadeusFlight.getString("aircraft");
                        flight.travelClass = amadeusFlight.getJSONObject("booking_info").getString("travel_class");
                        flight.bookingCode = amadeusFlight.getJSONObject("booking_info").getString("booking_code");
                        flight.seatsRemaining = amadeusFlight.getJSONObject("booking_info").getInt("seats_remaining");
                        itinerary.inbound.add(flight);
                    }

                    JSONObject amadeusFare = amadeusResult.getJSONObject("fare");
                    itinerary.totalPrice = amadeusFare.getDouble("total_price");
                    itinerary.refundable = amadeusFare.getBoolean("refundable");
                    itinerary.changePenalties = amadeusFare.getBoolean("change_penalties");

                    results.add(itinerary);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return  results;
        }
        @Override
        protected void onPostExecute(ArrayList<Itinerary> result) {
            textView.setText("Completed...");
        }
    }
}
