import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class Weather {

  public static void fetchWeather(String... args) {
    Double lat = null;
    Double lon = null;

    // parse flags or positional
    for (int i = 0; i < args.length; i++) {
      String a = args[i];
      if (a.equals("--lat") && i + 1 < args.length) {
        lat = tryParseDouble(args[++i]);
      } else if (a.equals("--lon") && i + 1 < args.length) {
        lon = tryParseDouble(args[++i]);
      } else if (!a.startsWith("--") && lat == null) {
        lat = tryParseDouble(a);
      } else if (!a.startsWith("--") && lon == null) {
        lon = tryParseDouble(a);
      }
    }

    if (lat == null || lon == null) {
      try {
        var loc = IpClient.getLocation();
        lat = loc.latitude();
        lon = loc.longitude();
        System.out.println("Location: " + loc.city() + ", " + loc.country_name());
        System.out.println("Coordinates: " + lat + ", " + lon);
      } catch (IOException | InterruptedException e) {
        System.out.println("Could not determine location via IP. Provide --lat and --lon.");
        return;
      }
    }

    String url = String.format(
        "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&current_weather=true&hourly=precipitation_probability&timezone=auto",
        lat, lon);

    HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2)).build();
    HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).timeout(Duration.ofSeconds(3)).GET().build();

    try {
      HttpResponse<String> resp = client.send(req, BodyHandlers.ofString());
      int status = resp.statusCode();
      if (status < 200 || status >= 300) {
        System.out.println("Weather API error: HTTP " + status);
        return;
      }
      String body = resp.body();
      Map<String, Object> root = new JsonParser(body).parseObject();

      // current_weather
      Map<String, Object> current = (Map<String, Object>) root.get("current_weather");
      Double temperature = null;
      Integer weathercode = null;
      if (current != null) {
        Object t = current.get("temperature");
        if (t instanceof Number)
          temperature = ((Number) t).doubleValue();
        Object wc = current.get("weathercode");
        if (wc instanceof Number)
          weathercode = ((Number) wc).intValue();
      }

      // hourly precipitation_probability and times
      Map<String, Object> hourly = (Map<String, Object>) root.get("hourly");
      String precipStr = "N/A";
      if (hourly != null) {
        List<Object> times = (List<Object>) hourly.get("time");
        List<Object> pp = (List<Object>) hourly.get("precipitation_probability");
        if (times != null && pp != null && !times.isEmpty() && !pp.isEmpty()) {
          // Find nearest index to current time: prefer first element for simplicity
          // For accuracy we could parse timestamps; keep simple and pick index 0 or match
          // current_weather.time
          int idx = 0;
          if (current != null && current.get("time") instanceof String) {
            String curTime = (String) current.get("time");
            for (int i = 0; i < times.size(); i++) {
              Object o = times.get(i);
              if (o instanceof String && ((String) o).equals(curTime)) {
                idx = i;
                break;
              }
            }
          }
          Object val = pp.size() > idx ? pp.get(idx) : null;
          if (val instanceof Number) {
            precipStr = String.valueOf(((Number) val).intValue()) + "%";
          }
        }
      }

      // Print results
      if (temperature != null)
        System.out.println("Temperature: " + String.format("%.1f", temperature) + " °C");
      else
        System.out.println("Temperature: N/A");

      System.out.println("Precipitation: " + precipStr);

      if (weathercode != null)
        System.out.println("Condition code: " + weathercode + " (" + weatherCodeLabel(weathercode) + ")");
      else
        System.out.println("Condition: N/A");

    } catch (IOException e) {
      System.out.println("Network error fetching weather: " + e.getMessage());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      System.out.println("Operation interrupted");
    } catch (ClassCastException | IllegalArgumentException e) {
      System.out.println("Error parsing weather response");
    }
  }

  private static Double tryParseDouble(String s) {
    try {
      return Double.parseDouble(s);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  private static String weatherCodeLabel(int code) {
    // Minimal mapping per Open-Meteo weather codes
    switch (code) {
      case 0:
        return "Clear sky";
      case 1:
      case 2:
      case 3:
        return "Mainly clear / partly cloudy";
      case 45:
      case 48:
        return "Fog";
      case 51:
      case 53:
      case 55:
        return "Drizzle";
      case 61:
      case 63:
      case 65:
        return "Rain";
      case 71:
      case 73:
      case 75:
        return "Snow";
      case 80:
      case 81:
      case 82:
        return "Rain showers";
      case 95:
      case 96:
      case 99:
        return "Thunderstorm";
      default:
        return "Unknown";
    }
  }
}
