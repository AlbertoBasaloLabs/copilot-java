---
ID: ASK-BOT_PRD
Curso: VSCode-Copilot-Java
Arquetipo: Java-CLI
Autor: Alberto Basalo
---

# PRD — AskBot CLI

## Resumen ejecutivo
CLI educativo que consulta APIs públicas para dar información básica de la IP del usuario y servicios asociados: localización, clima, moneda, hora y sol.

## Objetivos y métricas
- CLI usable en taller.
- Demostrar consumo de APIs REST.
- Lograr que todos los comandos funcionen en local.

## Usuarios
- Estudiante de programación.
- Instructor.

## Historias de usuario
- Como usuario, quiero saber mi localización actual a partir de mi IP.
- Como usuario, quiero conocer el pronóstico de hoy en mi ciudad.
- Como usuario, quiero ver la moneda oficial de mi país y cotizaciones básicas.
- Como usuario, quiero conocer mi hora local y diferencia con UTC.
- Como usuario, quiero saber las horas de salida y puesta del sol.

## Alcance
Included: CLI con 5 comandos.
Excluded: interfaz gráfica, persistencia, autenticación.

## Requerimientos funcionales
1. RF-1: `/loc` muestra ciudad, país, lat/long de [`ipapi.co/json`](https://ipapi.co/json/)
2. RF-2: `/weather` muestra temperatura, prob. lluvia, código meteorológico de [`open-meteo.com`](https://open-meteo.com/)
3. RF-3: `/money` muestra moneda oficial + cotización en EUR/USD/GBP/CHF de [`open.er-api.com/v6`](https://open.er-api.com/v6)
4. RF-4: `/time` muestra hora local, huso, verano/invierno, diff con UTC de [`timeapi.io`](https://timeapi.io/)
5. RF-5: `/sun` muestra salida y puesta de sol de [`open-meteo.com`](https://open-meteo.com/)

## Requerimientos no funcionales
- Simplicidad ante todo.
- Respuesta <1s con conexión activa.
- Uso de APIs públicas gratuitas:
  - ipapi.co
  - open-meteo.com
  - open.er-api.com/v6
  - timeapi.io

## Dependencias
- Java
- APIs externas

## Criterios de aceptación
- Cada comando devuelve datos legibles.
- CLI ejecutable con `java -jar cli.jar /command`.

## Riesgos
- APIs caídas → fallback genérico.


