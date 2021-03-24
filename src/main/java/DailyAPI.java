import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class DailyAPI extends AbstractAPI{

    /**
     * Creates connection to API, stores Daily JSON, parses Daily JSON, and returns daily data organized in an ArrayList of Entry Objects.
     * Limited to 5 calls per minute and 500 calls per day.
     * @param _stock String with desired stock symbol. (Example: "IBM")
     * @return ArrayList of Entry objects.
     */
    public static ArrayList<Entry> getDailyInfo(String _stock){
        try {
            // Create the API url.
            URL url = getStockURL(_stock, null, "daily");

            // Save the main JSON file.
            JSONObject JSONFile = getStockJSON(url);

            //Translate the interval used in URL to correct format needed to parse the JSON file.
            String JSONInterval = translateInterval("daily");

            //If there is an error receiving the JSONObject OR the time series interval then the function will exit.
            if(JSONFile == null || JSONInterval == null){
                return null;
            }

            //Create JSON object that will hold only the information for the given time interval.
            JSONObject timeSeries = JSONFile.getJSONObject(JSONInterval);

            //Parse the given interval JSON and save the data in an ArrayList of Entry objects.

            return parseStockJSON(timeSeries);
        }
        catch (Exception ex) {
            System.out.println("Error: " + ex);
            return null;
        }

        /*for(int i = 0; i < intraday.size(); i++)
            System.out.println(intraday.get(i).toString());*/
    }
}
