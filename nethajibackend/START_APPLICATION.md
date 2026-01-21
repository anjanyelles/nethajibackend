# How to Start the Application

## Quick Start

```bash
cd /Users/anjanyelle/Desktop/nethajifullstack/nethajibackend
mvn spring-boot:run
```

## Wait for Startup

Look for this message in the terminal:
```
Started NethajiApplication in X.XXX seconds
```

## Verify It's Running

Once you see "Started NethajiApplication", test in a new terminal:

```bash
curl http://localhost:9029/api/nethaji-service/health
```

Should return: `"OK"`

## Then Test in Postman/Safari

- **Health**: `http://localhost:9029/api/nethaji-service/health`
- **Departments**: `http://localhost:9029/api/nethaji-service/acadamic/getalldepartments`
- **Programs**: `http://localhost:9029/api/nethaji-service/acadamic/getallprograms`

## Load Sample Data (After App Starts)

Once the application is running, load sample data via API:

```bash
curl -X POST http://localhost:9029/api/nethaji-service/admin/seed-data
```

Or use Postman:
- **Method**: `POST`
- **URL**: `http://localhost:9029/api/nethaji-service/admin/seed-data`

---

## Troubleshooting

### "Connection Refused" Error
- **Solution**: Application is not running
- **Fix**: Start the application with `mvn spring-boot:run`

### Port Already in Use
```bash
lsof -i :9029
kill -9 <PID>
```

### Database Connection Error
- Make sure PostgreSQL is running: `brew services list | grep postgres`
- Start PostgreSQL: `brew services start postgresql@14`

---

**Important**: Keep the terminal window open while the application is running!

