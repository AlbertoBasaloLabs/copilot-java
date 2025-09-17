
public class Main {
  public static void main(String[] args) {
    System.out.println("# Ask Bot here, welcome!");
    IpClient.fetchIp();
    if (args.length > 0) {
      if (args[0].equals("weather")) {
        System.out.println("## Fetching weather information...");
        String[] rest = new String[Math.max(0, args.length - 1)];
        for (int i = 1; i < args.length; i++)
          rest[i - 1] = args[i];
        Weather.fetchWeather(rest);
      } else {
        printHelpMessage();
      }
    } else {
      printHelpMessage();
    }
    System.out.println("Bye!");
  }

  private static void printHelpMessage() {
    System.out.println("## Available commands: ");
    System.out.println("  - `weather` : Fetch weather information");
  }
}