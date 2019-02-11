import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Yeet test");
        new WeatherData();
    }
}

class WeatherData {
    private String zipValue;
    private String dateValue;
    private String airportCode;

    public WeatherData() {
        this.zipValue = getZip();
        this.dateValue = dateFormatter(getDate());
       // System.out.println(this.dateValue);
        this.airportCode = airportParser();
       // System.out.println(this.airportCode);
        System.out.println(weatherInfoMessage());
        weatherParser();
    }

    private String getZip() {
        System.out.print("Please enter your ZIP Code: ");
        Scanner readUserZIP = new Scanner(System.in);
        return readUserZIP.nextLine();
    }

    private String getDate() {
        System.out.print("Enter the date you are looking for data(MM/DD/YYYY): ");
        Scanner readUserDate = new Scanner(System.in);
        return readUserDate.nextLine();
    }

    private String giveAirportURL() {
        return "https://www.allplaces.us/afz.cgi?s=" + this.zipValue + "&rad=50";
       // Document rawAirportData = downloadWebData(airportURL);
        //return airportURL;
    }

    private String dateFormatter(String userDefinedDate){
        Date date = new Date();
        if(userDefinedDate.length() == 10){
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            try {
                date = formatter.parse(userDefinedDate);
            }catch(ParseException pe){
                return "01/01/1991";
            }
        }
        SimpleDateFormat urlFormatter = new SimpleDateFormat("yyyy/MM/dd");
        return "" + urlFormatter.format(date);
    }

    private String airportParser() {
        Document airportData = downloadWebData(giveAirportURL());
        //System.out.println(airportData); // Debug line that provides an output of the document text.
        Elements elem = airportData.select("tbody");
        Elements ids = elem.select("td.article");
        Elements font = ids.select("font");
        String rawAirportData = "" + font;

        ArrayList <String> parsedAirport = new ArrayList<String>(Arrays.asList(rawAirportData.split("\\r?\\n")));

        String pat = "^([Kk])[A-Za-z]{3}$";

        for (int p = 0; p < parsedAirport.size(); p++) {
            parsedAirport.set(p, parsedAirport.get(p).replaceAll("<[^>]*>", "").trim());
            //System.out.println("|" + parsedAirport[p] + "|");
            if (parsedAirport.get(p).matches(pat)) {
                return parsedAirport.get(p);
            }
        }
        return "KLGA";
    }

    private String giveWeatherURL(){
        //System.out.println("Weather Underground URL : https://api.wunderground.com/history/airport/" + this.airportCode + "/" + this.dateValue + "/DailyHistory.html?zip=" + this.zipValue);
        return "https://api.wunderground.com/history/airport/" + this.airportCode + "/" + this.dateValue + "/DailyHistory.html?zip=" + this.zipValue;
    }

    private Document downloadWebData(String urlToDownload) {
        //System.out.println(urlToDownload); // Debugging statement to make sure that the string that is passed looks correct.
        try {
            return Jsoup.connect(urlToDownload).get();
            //return webWeatherData;
        } catch (IOException ioe) {
            String IOEerror = "" + ioe;
            System.err.println("Uh oh, something went wrong, IOException: " + ioe);
            return Jsoup.parse(IOEerror);
        }
    }

    private String weatherInfoMessage(){
        return "Weather Data for " + this.zipValue + " -- Date: " + this.dateValue + " -- Airport: " + this.airportCode;
    }

    private void weatherParser() {
        ArrayList <String> headerNames = new ArrayList<String>();
        int iteration = 0;

        Document rawWeatherData = downloadWebData(giveWeatherURL());
        Elements elem = rawWeatherData.select("div#observations_details");
        //System.out.println(elem);
        Elements wt = elem.select("th");
        Elements tm = elem.select("td");
        Elements columns = elem.select("tr.no-metars");
        Elements headValues = elem.select("table.obs-table > thead > tr > th");

        String[][] weatherValues = new String[columns.size()][headValues.size()];


        String rawData = "" + tm;
        ArrayList <String> weatherUnits = new ArrayList<String>(Arrays.asList(rawData.split("\\r?\\n")));

        for (int p = 0; p < weatherUnits.size(); p++) {
            weatherUnits.set(p, weatherUnits.get(p).replaceAll("\\<[^>]*>", "").replaceAll("&nbsp;", " ").trim());
            //System.out.println(weatherUnits[p]);
        }

        for (int r = 0; r < wt.size(); r++) {
            headerNames.add("" + wt.eq(r));
            headerNames.set(r, headerNames.get(r).replaceAll("\\<[^>]*>", ""));
            System.out.print(headerNames.get(r) + " | ");
        }
        System.out.println();

        for (int z = 0; z < columns.size(); z++) {
            for (int r = 0; r < headValues.size(); r++) { //
                weatherValues[z][r] = "" + weatherUnits.get(iteration);
                weatherValues[z][r] = weatherValues[z][r].replaceAll("\\<[^>]*>", "").replaceAll("&nbsp;", " ").replaceAll("&amp;bsp;", "").trim();
                System.out.print(spaceDisplay(weatherValues[z][r], r) + weatherValues[z][r] + spaceDisplay(weatherValues[z][r], r) + "|");
                iteration++;
            }
            System.out.println("\n --");
        }

    }

    private StringBuilder spaceDisplay(String text, int state) {
        //Time (EST) | Temp. | Windchill | Dew Point | Humidity | Pressure | Visibility | Wind Dir | Wind Speed | Gust Speed | Precip | Events | Conditions |
        ArrayList <Integer> sizeAllocation = new ArrayList<>(Arrays.asList(12, 7, 11, 12, 11, 10, 12, 10, 12, 12, 9, 8, 12));
        int spaceCount = (sizeAllocation.get(state) - text.length()) / 2;
        StringBuilder spaceAppend = new StringBuilder();
        for (int u = 0; u < spaceCount; u++) {
            spaceAppend.append(" ");
        }
        return spaceAppend;
    }

}