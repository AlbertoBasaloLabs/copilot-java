import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.time.Duration;

public class IpClient {

  private static final HttpClient CLIENT = HttpClient.newBuilder()
      .connectTimeout(Duration.ofSeconds(2))
      .build();

  public static IpApiResponse getLocation() throws IOException, InterruptedException {
    BodyHandler<String> bodyHandlers = HttpResponse.BodyHandlers.ofString();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://ipapi.co/json/"))
        .header("Accept", "application/json")
        .timeout(Duration.ofSeconds(3))
        .GET()
        .build();

    HttpResponse<String> response = CLIENT.send(request, bodyHandlers);
    int status = response.statusCode();
    String body = response.body();

    if (status >= 200 && status < 300) {
      return IpApiResponse.fromJson(body);
    }
    throw new IOException("HTTP " + status + " from ipapi.co");
  }

  public static void fetchIp() {
    try {
      IpApiResponse ipApi = getLocation();
      System.out.println("## Your IP address is " + ipApi.ip());
      String location = ipApi.city() + ", " + ipApi.region() + ", " + ipApi.country_name();
      System.out.println("- Location: " + location);
      String coordinates = "- Coordinates: Lat " + ipApi.latitude() + ", Long " + ipApi.longitude();
      System.out.println(coordinates);
    } catch (IOException e) {
      System.out.println("Could not determine location via IP (network error). Provide --lat and --lon.");
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      System.out.println("Operation interrupted");
    }
  }
}
