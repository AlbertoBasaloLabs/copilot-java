import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Ip {

  public static void fetchIp() {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://ipapi.co/json/"))
        .header("Accept", "application/json")
        .GET()
        .build();

    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      int status = response.statusCode();
      String body = response.body();

      if (status >= 200 && status < 300) {
        IpApiResponse ipApi = IpApiResponse.fromJson(body);
        System.out.println("Your IP address is " + ipApi.ip());
        String location = ipApi.city() + ", " + ipApi.region() + ", " + ipApi.country_name();
        System.out.println("Location: " + location);
        String coordinates = "Lat " + ipApi.latitude() + ", Long " + ipApi.longitude();
        System.out.println("Coordinates: " + coordinates);
      } else {
        System.out.println("Error HTTP " + status + ": " + body);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      e.printStackTrace();
    }
  }
}
