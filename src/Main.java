
public class Main {
  public static void main(String[] args) {
    System.out.println("Hello, World!");
    IpApiClient.fetchIp();
    if (args.length > 0) {
      // if args is weather
      if (args[0].equals("weather")) {
        System.out.println("Fetching weather information...");
        Weather.fetchWeather();
      } else {
        printHelpMessage();
      }
    } else {
      printHelpMessage();
    }
    System.out.println("Bye!");
  }

  private static void printHelpMessage() {
    // print help message
    System.out.println("Available commands: ");
    System.out.println("  weather - Fetch weather information");
  }
}