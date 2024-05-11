package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.Date;

public class ADP_Updater {
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        try {
            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(new URI("https://api.sleeper.app/v1/players/nfl"))
                    .GET().build();

            Date d = new Date();
            String url = "https://api.fantasycalc.com/adp?isDynasty=true&numTeams=8&numTeams=10&numTeams=12&ppr=1&numQbs=2&draftType=Startup&startDate=";
            String earlyMonth = String.valueOf(d.getMonth() - 1);
            String month = String.valueOf(d.getMonth());
            if (month.length() == 1) {
                month = "0" + month;
            }
            if (earlyMonth.length() == 1) {
                earlyMonth = "0" + earlyMonth;
            }
            String date = String.valueOf(d.getDate());
            if (date.length() == 1) {
                date = "0" + date;
            }
            String year = String.valueOf(d.getYear() + 1900);
            String earlyYear;
            if (earlyMonth.equalsIgnoreCase("12")) {
                earlyYear = String.valueOf(d.getYear() + 1899);
            } else {
                earlyYear = year;
            }
            url += earlyMonth + "-" + date + "-" + earlyYear;
            url += "&endDate=" + month + "-" + date + "-" + year;

            HttpRequest getRequest2 = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET().build();

            HttpClient client = HttpClient.newHttpClient();
            HttpClient client2 = HttpClient.newHttpClient();

            HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> getResponse2 = client2.send(getRequest2, HttpResponse.BodyHandlers.ofString());

            BufferedWriter writer = new BufferedWriter(new FileWriter("D:/IntelliJ/Mock-Draft-Tool-JSON/src/main/java/org/example/players.json"));
            writer.write(getResponse.body());
            writer.close();

            writer = new BufferedWriter(new FileWriter("D:/IntelliJ/Mock-Draft-Tool-JSON/src/main/java/org/example/ADP.json"));
            writer.write(getResponse2.body());
            writer.close();
        } catch (IOException | URISyntaxException | InterruptedException | RuntimeException e) {
            System.out.println("Error occurred while making HTTP request: " + e.getMessage());
        }
    }
}
