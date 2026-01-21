# Install Maven on macOS

## Quick Install (Recommended)

Since Maven is not installed, you have two options:

### Option 1: Install Maven via Homebrew (Recommended)

Run this command in your terminal:

```bash
brew install maven
```

After installation, verify:
```bash
mvn -version
```

Then you can run the application:
```bash
cd /Users/anjanyelle/Desktop/nethajifullstack/nethajibackend
mvn clean install
mvn spring-boot:run
```

---

### Option 2: Use Maven Wrapper (If wrapper is complete)

If the Maven wrapper files are complete, you can use:

```bash
cd /Users/anjanyelle/Desktop/nethajifullstack/nethajibackend
./mvnw clean install
./mvnw spring-boot:run
```

---

## After Installing Maven

Once Maven is installed, run these commands:

```bash
# Navigate to project
cd /Users/anjanyelle/Desktop/nethajifullstack/nethajibackend

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on: **http://localhost:9029**

---

## Verify Installation

After installing Maven, check:
```bash
mvn -version
```

You should see something like:
```
Apache Maven 3.9.x
Maven home: /opt/homebrew/Cellar/maven/...
Java version: 17.0.16
```

---

**Next Step**: Run `brew install maven` in your terminal, then come back and run the application!

