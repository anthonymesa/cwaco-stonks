import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class provides connection info and re-usable functions for connecting to the
 * AlphaVantage API for stock and crypto data.
 */

public abstract class AlphaVantageConnection {

    protected static final String baseURLAlpha = "https://www.alphavantage.co/query?";
    protected static final String apiKeyAlpha = "MO9QU9JAPBBPX5T7";

    protected enum CallType {INTRADAY_CALL, DAILY_CALL, WEEKLY_CALL, MONTHLY_CALL}
    protected enum CallInterval {NO_INTERVAL, MINUTE_1, MINUTE_5, MINUTE_10, MINUTE_15, MINUTE_30, MINUTE_60}

    /**
     * Parses raw JSON information from API call and stores it in a JSONobject that is returned.
     * @param _APIlink URL object that is formatted for AlphaVantage API calls.
     * @return JSONobject containing intraday data.
     */
    protected static JSONObject getJSON(URL _APIlink) throws IOException, JSONException {
        // Create a HTTP Connection.
        HttpURLConnection con = (HttpURLConnection) _APIlink.openConnection();

        con.setRequestMethod("GET");

        // Examine the response code.
        int status = con.getResponseCode();
        if (status != 200) {
            System.out.print("Error: Could not load item: " + status);
            return null;
        } else {
            // Parsing input stream into a text string.
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            // Close the connections.
            in.close();
            con.disconnect();

            // Print out our complete JSON string. (For Testing)
            //System.out.println("Output: " + content.toString());

            return new JSONObject(content.toString());
        }
    }

    /**
     * This method will take the int given by the caller and return the appropriate enum variable to continue processing the call.
     * @param _callType Int from 1 to 4
     * @return CallType enum
     */
    protected static AVAPIStocksTranslator.CallType translateTypeEnum(int _callType){
        switch(_callType){
            case 0:
                return AVAPIStocksTranslator.CallType.INTRADAY_CALL;

            case 1:
                return AVAPIStocksTranslator.CallType.DAILY_CALL;

            case 2:
                return AVAPIStocksTranslator.CallType.WEEKLY_CALL;

            case 3:
                return AVAPIStocksTranslator.CallType.MONTHLY_CALL;

            default:
                System.out.println("Invalid call type.");
                return null;
        }

    }

    /**
     * This method will take the int given by the caller and return the appropriate enum variable to continue processing the call.
     * @param _callInterval Int from 10 to 16
     * @return CallInterval enum
     */
    protected static AVAPIStocksTranslator.CallInterval translateIntervalEnum(int _callInterval){
        switch(_callInterval){
            case 0:
                return AVAPIStocksTranslator.CallInterval.NO_INTERVAL;

            case 1:
                return AVAPIStocksTranslator.CallInterval.MINUTE_1;

            case 2:
                return AVAPIStocksTranslator.CallInterval.MINUTE_5;

            case 3:
                return AVAPIStocksTranslator.CallInterval.MINUTE_10;

            case 4:
                return AVAPIStocksTranslator.CallInterval.MINUTE_15;

            case 5:
                return AVAPIStocksTranslator.CallInterval.MINUTE_30;

            case 6:
                return AVAPIStocksTranslator.CallInterval.MINUTE_60;

            default:
                System.out.println("Invalid Interval.");
                return null;
        }

    }

    /**
     * This method will transform the int interval Enum from the AlphpaAPIDataGet into a string to be used in the API URL.
     * @param _interval String with desired time interval, for Intraday only (Options: "1min", "5min", "10min", "15min", "30min", "60min")
     *      *                  Use NO_INTERVAL for any call that is not intraday and this method will not be used in that case.
     * @return A formatted String ready for use in the API URL.
     */
    protected static String translateIntradayInterval(AVAPIStocksTranslator.CallInterval _interval){
        //If using Intraday call then the interval must be checked so that the correct interval is used in the URL.
        switch(_interval) {
            case MINUTE_1:
                return "1min";

            case MINUTE_5:
                return "5min";

            case MINUTE_10:
                return "10min";

            case MINUTE_15:
                return "15min";

            case MINUTE_30:
                return "30min";

            case MINUTE_60:
                return "60min";

            default:
                System.out.println("Not a valid Interval.");
                return null;
        }
    }
}
