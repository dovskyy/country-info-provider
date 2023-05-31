import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class UserInterface {
    private Scanner scanner;
    private Gson gson;

    public UserInterface(Scanner scanner) {
        this.scanner = scanner;
        this.gson = new Gson();
    }

    public void start(){
        while (true){
            System.out.println("-----------------------------------------------------");
            System.out.println("Country info provider. input 'q' to quit the program.");
            System.out.println("Input country name to print its' basic information.");
            String countryName = scanner.nextLine().toLowerCase().trim();

            if (countryName.equalsIgnoreCase("q")){
                break;
            }

            //jeżeli performGetRequest() rzuci IllegalArgumentException, to rozpoczynamy kolejną iterację
            try {
                Country country = performGetRequest(countryName);
                printCountryInfo(country);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid country name. Please try again. \n");
                continue; // Przejdź do kolejnej iteracji pętli while
            }

        }

    }

    private Country performGetRequest(String countryName) {
        HttpClient httpClient = HttpClient.newHttpClient();
        Gson gson = new Gson();

        // metoda GET w API restcountries.com zwraca tablicę obiektów Country
        Country[] countryArray;
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://restcountries.com/v3.1/name/" + countryName))
                .GET()
                .build();

        HttpResponse<String> response = null;

        // jeżeli response body zawiera sekwencję "404" oznacza to, że państwo nie zostało znalezione. Rzuca wyjątek, obsługiwany w metodzie start
        try {
            response = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            if (response.body().contains("404")) {
                throw new IllegalArgumentException("Country not found, try again.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }



        //assign Countries from the API response to our Country array
        countryArray = gson.fromJson(response.body(), Country[].class);
        return countryArray[0];
    }

    private void printCountryInfo(Country country) {
        System.out.println("Country official name: " + country.getName().getOfficial());
        System.out.println("Country population: " + country.getPopulation());
        System.out.println("Country capital: " + country.getCapital()[0]);
        System.out.println("Region: " + country.getRegion());
        System.out.println("Subregion: " + country.getSubregion());
        System.out.println("Is United Nations member: " + country.getUnMember());
    }
}
