# api-health-monitor
Monitors HTTP endpoints, records response times and availability, and tracks incidents when services fail.


# API Health Monitor

A Spring Boot service that checks registered HTTP endpoints on a schedule, stores their availability and response times, and opens or resolves incidents when their status changes.

## Features

- Register HTTP/HTTPS endpoints for monitoring
- Run health checks every 60 seconds
- Store response time and availability history
- Open an incident when an endpoint goes down and resolve it when the endpoint recovers
- Calculate 24-hour uptime and average response time
- Pause and resume individual monitors
- View current monitor and incident state from the dashboard

## Tech Stack

- Java 21 
- Spring Data JPA + PostgreSQL (Supabase)
- WebClient for outbound HTTP checks
- Spring Scheduler for background processing

## Running Locally

Set these environment variables (IntelliJ: Run -> Edit Configurations -> Environment Variables):
```
SPRING_DATASOURCE_URL=jdbc:postgresql://your-host:5432/postgres?sslmode=require
SPRING_DATASOURCE_USERNAME=your-username
SPRING_DATASOURCE_PASSWORD=your-password
```
Then run the app and visit `http://localhost:8080`

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/api/monitors` | Register a monitor |
| GET | `/api/monitors` | Get all monitors |
| POST | `/api/monitors/{id}/pause` | Pause a monitor |
| POST | `/api/monitors/{id}/resume` | Resume a monitor |
| GET | `/api/monitors/{id}/checks` | Get recent check history |
| GET | `/api/monitors/{id}/stats` | Get 24-hour uptime statistics |
| GET | `/api/incidents/open` | Get currently open incidents |

## Current limitations

This is a small learning project. Health checks are currently scheduled by a single application instance, so running multiple instances would require coordination to avoid duplicate checks.
