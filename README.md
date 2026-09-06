# api-health-monitor
built a system that watches over multiple APIs, checks if they're alive, tracks their response times, and alerts when something goes wrong.


# API Health Monitor

A production-grade backend system that watches over registered API endpoints, 
automatically pings them on a schedule, tracks uptime, manages incidents, 
and presents everything on a live dashboard.

## Features

- Register any HTTP/HTTPS endpoint for monitoring
- Automatic health checks every 60 seconds via background scheduler
- Incident lifecycle management - automatically opens when an endpoint goes DOWN, 
  resolves when it comes back UP
- 24-hour uptime statistics and average response time per monitor
- Pause/resume individual monitors
- Clean dashboard frontend with live auto-refresh

## Tech Stack

- Java 21 
- Spring Data JPA + PostgreSQL (Supabase)
- Spring WebFlux (WebClient for outbound HTTP checks)
- Spring Scheduler for background processing

## Running Locally

Set these environment variables (IntelliJ: Run -> Edit Configurations -> Environment Variables):
```
SPRING_DATASOURCE_URL=jdbc:postgresql://your-host:5432/postgres?sslmode=require
SPRING_DATASOURCE_USERNAME=your-username
SPRING_DATASOURCE_PASSWORD=your-password
```

Then run the app and visit `http://localhost:8080`

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/monitors | Register a new monitor |
| GET | /api/monitors | Get all monitors |
| POST | /api/monitors/{id}/pause | Pause a monitor |
| POST | /api/monitors/{id}/resume | Resume a monitor |
| GET | /api/monitors/{id}/checks | Recent check history |
| GET | /api/monitors/{id}/stats | 24h uptime statistics |
| GET | /api/incidents/open | All open incidents |


