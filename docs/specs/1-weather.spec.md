
# Weather command spec

## 1. Problem Specification (from the user POV)

- Goal: Quickly get the current weather for the user's location (inferred from IP) or for explicit coordinates.
- Primary user story: "As a user, I want to know today's current temperature, the chance of rain, and a short weather code/description for my city so I can decide what to wear."
- Inputs:
	- Optional: latitude and longitude (positional) or `--lat` and `--lon` flags.
	- Default: no args — use the app's geolocation (RF-1 / ip-api) to determine coordinates.
- Output (user-visible): a short, human-friendly block showing:
	- Location (city, country, coordinates)
	- Temperature (°C)
	- Chance of rain / precipitation probability (%) for the current hour or nearest hourly value
	- Weather code or simple condition label (derived from Open-Meteo weather code)
- Error modes:
	- Network/API unavailable → friendly message suggesting retry.
	- Missing/invalid coordinates → usage hint.
	- Slow responses → timeout with a clear message.

## 2. Solution Overview (from the dev POV)

- Flow:
	1. Resolve coordinates: if lat/lon provided use them; otherwise call the geolocation service (ip-api or equivalent) to obtain city/country/lat/lon.
	2. Query Open-Meteo for weather: request current and hourly precipitation probability using the endpoint:
		 - /v1/forecast?latitude={lat}&longitude={lon}&current_weather=true&hourly=precipitation_probability&timezone=auto
	3. Parse JSON and pick the current timestamp index for hourly precipitation to report the nearest precipitation probability.
	4. Format and print: show location, temperature, precipitation probability (rounded), and weather code → map to a short label when helpful.

- Data contracts (minimal):
	- Request: latitude (decimal), longitude (decimal)
	- Open-Meteo response fields used:
		- current_weather.temperature (number)
		- current_weather.weathercode (int)
		- hourly.time (array of iso8601), hourly.precipitation_probability (array of numbers)

- Error handling and timeouts:
	- Set a short HTTP timeout (e.g., 2–3s) to meet responsiveness targets.
	- On non-200 or parse errors return a clear, single-line reason and an exit code >0.
	- If precipitation data is missing, show "N/A" for chance of rain but still display temperature and code.

- CLI surface:
	- askbot weather                # uses IP-based location
	- askbot weather --lat 43.36 --lon -8.40
	- askbot weather <lat> <lon>

## 3. Acceptance Criteria (from the QA)

1. Happy path
	 - Given reachable APIs and no args, `askbot weather` prints:
		 - Location: City, Country (CODE)
		 - Coordinates: lat, lon
		 - Temperature: <value> °C
		 - Precipitation: <value>% (or "N/A")
		 - Condition: <weather code> (short label)
	 - Data corresponds to the values returned by Open-Meteo for the resolved coordinates.

2. Input variations
	 - When called with valid `--lat` and `--lon` or positional coordinates, the command uses them instead of IP lookup.

3. Error and timeout handling
	 - If the geolocation API is unreachable, the command prints a friendly message and instructs how to provide coordinates manually.
	 - If the weather API times out or returns an error, the command prints a short error and exits non-zero.
	 - If precipitation probability is not available, the output shows "Precipitation: N/A" but still prints temperature and code.

4. Performance
	 - Under normal network conditions the command responds within 3 seconds. (Tests may mock HTTP to assert speed.)

5. Tests (minimal)
	 - Unit: JSON parsing for Open-Meteo response (temperature, weathercode, hourly precipitation index).
	 - Integration (mocked HTTP): happy path, geolocation failure, weather API error, missing precipitation field.

6. Mapping to PRD
	 - Covers RF-2: shows current temperature, chance of rain and weather code via Open-Meteo; uses IP-based location by default.

---
*Spec authored from PRD RF-2; keep implementation small and dependency-free.*
