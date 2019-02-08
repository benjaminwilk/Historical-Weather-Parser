import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {

        String zipValue;
        String dateValue;

        System.out.println("Yeet test");
        Main man = new Main();
        zipValue = man.getZip();
        dateValue = man.getDate();
        man.airportParser(zipValue);

    }

    public String getZip(){
        System.out.print("Please enter your ZIP Code: ");
        Scanner readUserZIP = new Scanner(System.in);
        String userZip = readUserZIP.nextLine();
        return userZip;
    }

    public String getDate(){
        System.out.print("Enter the date you are looking for data: ");
        Scanner readUserDate = new Scanner(System.in);
        String userDate = readUserDate.nextLine();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

            Date date = formatter.parse(userDate);
            System.out.println(date);
            System.out.println(formatter.format(date));
        }catch(ParseException pe){
            System.err.println(pe);
            return "" + pe;
        }
        return "You weren't supposed to get to the bottom of the get date function";
    }

    private Document getAirportWebData(String zipValue){
        String airportURL = "https://www.allplaces.us/afz.cgi?s="+ zipValue + "&rad=50";
        Document rawAirportData = downloadWebData(airportURL);
        return rawAirportData;
    }


    public void airportParser(String zipValue){
        Document airportData = getAirportWebData(zipValue);
        //System.out.println(airportData); // Debug line that provides an output of the document text.
        Elements elem = airportData.select("table");
        Elements ids = elem.select("td.article");

        
    }

    private Document downloadWebData(String urlToDownload){
        //System.out.println(urlToDownload); // Debugging statement to make sure that the string that is passed looks correct.
        try {
            Document webWeatherData = Jsoup.connect(urlToDownload).get();
            return webWeatherData;
        }catch(IOException ioe){
            String IOEerror = "" + ioe;
            System.err.println("Uh oh, something went wrong, IOException: " + ioe);
            return Jsoup.parse(IOEerror);
        }
    }

    public void weatherParser(String zipValue, String dateValue){
        String rawData = "";
        String[] headerNames = new String[13];
        String[] weatherUnits;
        String[][] weatherValues = new String[24][500];
        int iteration = 0;

        String wUndergroundURL = "https://api.wunderground.com/history/airport/KDET/" + dateValue + "/DailyHistory.html?zip=" + zipValue;
        Document rawWeatherData = downloadWebData(wUndergroundURL);
        Elements elem = rawWeatherData.select("div#observations_details");
        Elements wt = elem.select("th");
        Elements tm = elem.select("td");
        rawData = "" + tm;

        weatherUnits = rawData.split("\\r?\\n");

        for(int p = 0; p < weatherUnits.length; p++){
            weatherUnits[p] = weatherUnits[p].replaceAll("\\<[^>]*>", "").replaceAll("&nbsp;", " ").trim();
            //System.out.println(weatherUnits[p]);
        }

        for(int r = 0; r < wt.size(); r++){
            headerNames[r] = "" + wt.eq(r);
            headerNames[r] = headerNames[r].replaceAll("\\<[^>]*>","");
            System.out.print(headerNames[r] + " | ");
        }
        System.out.println();

        for(int z = 0; z < 24; z++) {
            for (int r = 0; r < 13; r++) { //
                weatherValues[z][r] = "" + weatherUnits[iteration];
                weatherValues[z][r] = weatherValues[z][r].replaceAll("\\<[^>]*>", "").replaceAll("&nbsp;", " ").replaceAll("&amp;bsp;", "").trim();
                System.out.print(spaceDisplay(weatherValues[z][r], r) + weatherValues[z][r] + spaceDisplay(weatherValues[z][r], r) + "|");
                iteration++;
            }
            System.out.println("\n --");
        }

    }


 /*   public void dataGrabber(){
        String[] headerNames = new String[13];
        String rawData = "";
        String[] weatherUnits;
        String[][] weatherValues = new String[24][500];

        int iteration = 0;


      //  System.out.println("https://api.wunderground.com/history/airport/KDET/" + dateValue + "/DailyHistory.html?zip=" + zipValue);
        try {
         //   Document webWeatherData = Jsoup.connect("https://api.wunderground.com/history/airport/KDET/" + dateValue + "/DailyHistory.html?zip=" + zipValue).get();
            //System.out.println(doc);
           // Elements elem = webWeatherData.select("div#observations_details");
            //Elements wt = elem.select("th");
            //Elements tm = elem.select("td");
            //System.out.println(tm);

      //      rawData = "" + tm;
           // System.out.println(rawData);
            weatherUnits = rawData.split("\\r?\\n");

            for(int p = 0; p < weatherUnits.length; p++){
                weatherUnits[p] = weatherUnits[p].replaceAll("\\<[^>]*>", "").replaceAll("&nbsp;", " ").trim();
                //System.out.println(weatherUnits[p]);
            }

            for(int r = 0; r < wt.size(); r++){
                headerNames[r] = "" + wt.eq(r);
                headerNames[r] = headerNames[r].replaceAll("\\<[^>]*>","");
                System.out.print(headerNames[r] + " | ");
            }
            System.out.println();

            for(int z = 0; z < 24; z++) {
                for (int r = 0; r < 13; r++) { //
                    weatherValues[z][r] = "" + weatherUnits[iteration];
                    weatherValues[z][r] = weatherValues[z][r].replaceAll("\\<[^>]*>", "").replaceAll("&nbsp;", " ").replaceAll("&amp;bsp;", "").trim();
                    System.out.print(spaceDisplay(weatherValues[z][r], r) + weatherValues[z][r] + spaceDisplay(weatherValues[z][r], r) + "|");
                    iteration++;
                }
                System.out.println("\n --");
            }

       /*     for(int r = 0; r < wt.size(); r++){
                weatherTitles[r] = "" + wt.eq(r);
                weatherTitles[r] = weatherTitles[r].replaceAll("\\<[^>]*>","");
                System.out.print(weatherTitles[r] + " | ");
            }
            System.out.println();
            for(int z = 0; z < 24; z++) {
                for (int i = 0; i < weatherTitles.length; i++) {
                    weatherValues[z][i] = "" + wv.eq(i);
                    weatherValues[z][i] = weatherValues[z][i].replaceAll("\\<[^>]*>","");
                    //System.out.print(weatherValues[z][i] + " | ");
                    //System.out.println("Z Value " + z + " I Value " + i  + weatherValues[z][i]);
                }
                System.out.println("\n -- \n");
            }
            /*JSONObject jsonObj = new JSONObject();
            JSONArray jsonArr = new JSONArray();
            Elements ttls = elem.getElementsByClass("ttl");
            JSONObject jo = new JSONObject();
            for (int i = 0, l = ttls.size(); i < l; i++) {
                String key = ttls.get(i).text();
                String value = nfos.get(i).text();
                jo.put(key, value);
            }

        }catch(IOException ioe){
            System.err.println("Uh oh, something went wrong, IOException: " + ioe);
        }

    }*/

    public String spaceDisplay(String text, int state){
        //Time (EST) | Temp. | Windchill | Dew Point | Humidity | Pressure | Visibility | Wind Dir | Wind Speed | Gust Speed | Precip | Events | Conditions |
        int sizeAllocation[] = {12, 7, 11, 12, 11, 10, 12, 10, 12, 12, 9, 8, 12};
        int spaceCount = (sizeAllocation[state] - text.length())/2;
        String spaceAppend = "";
       for(int u = 0; u < spaceCount; u++) {
           spaceAppend += " ";
       }
       return spaceAppend;
    }

}
