## 1. specification

```txt
Generate a complete specification for the weather comand.
Write it from the user/stakeholder perspective (what and why, not how).
Do not over do. Keep it short (less than 100 lines) 
```

## 2. implementation

```txt
implement #file:01-weather.spec.md 
following instructions: 
- #file:bst_clean-code.instructions.md 
- #file:lng_csharp.instructions.md 
- #file:std_object-calisthenics.instructions.md 
```

## 3. fix

```txt
@workspace /fix  > Error: Failed to fetch weather: Weather API error: BadRequest
Bye!
```

## 4. Commit

```txt
Commit changes executing #file:git-commit.prompt.md 
```

## 5. Clean

```txt
Clean #file:IpClient.cs by following best practices from:
- #file:bst_clean-code.instructions.md 
- #file:std_object-calisthenics.instructions.md 
```

```txt
@workspace /fix #file:Program.cs error CS0117: 'IpApiClient' no contiene una definición para 'FetchIp' To make it work with current #file:IpApi.cs changes
```

```txt
Commit the fix executing #file:git-commit.prompt.md 
```