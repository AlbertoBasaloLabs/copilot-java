# Weather Command Specification

## Purpose

As a user, I want to quickly know the current weather for my location so I can plan my day and activities accordingly.

## User Story

- **As a** user, **I want to** run `askbot weather` **so I can** see the current temperature, precipitation chance, and a simple weather condition for my city, based on my IP address.

## What It Does

- When I run `askbot weather`, the CLI should:
	- Detect my location automatically using my IP address.
	- Query a public weather API for my city.
	- Display the current temperature in °C, the chance of precipitation (as a percentage), and a short description or icon for the weather condition (e.g., sunny, cloudy, rainy).

## Why It Matters

- I want to make quick decisions about clothing, travel, and outdoor plans without searching for my city or using a web browser.
- The command should be fast, simple, and require no extra input from me.

## Output Example

```
🌤️ Weather in Madrid:
	 Temperature: 22°C
	 Precipitation: 15%
	 Condition: Partly cloudy
```

## Acceptance Criteria

- The command works with no arguments: `askbot weather`.
- The output is clear, human-readable, and includes city name, temperature, precipitation, and condition.
- If the weather service is unavailable, I see a helpful error message.
- The command responds in under 3 seconds with a working internet connection.
- No setup or configuration is required from the user.

## Non-Goals

- No hourly or multi-day forecasts.
- No advanced weather details (e.g., wind, humidity).
- No manual location entry.

