# Lowest Unique Bid (LUB) System - Spring Boot Application

## Original Question

You need to design and implement a Spring Boot application that provides the core logic of the lowest unique bid system for an online bidding platform.

### Requirements:
- The application must allow adding a new bid and provide the ability to query the current lowest unique bid at any point.
- A unique bid is a bid amount placed by exactly one user. If multiple users place the same bid, it is no longer considered unique.
- The system must return the lowest unique bid so far, or -1 if none exist.

### Example:
- John bids 10 → no one else bid 10 → LUB = 10
- Mary bids 8 → no one else bid 8 → LUB = 8
- Peter bids 8 → 8 is no longer unique → LUB = 10

The system must consistently update the LUB as new bids come in.

### Expectations:
- Free to use Java collections.
- Should know internal details of collections, including time-space complexity.
- Implementation should follow a production-like Spring Boot structure with proper separation of concerns.

### Brownie point:
The API to query the lowest unique bid must be treated as an internal API (not exposed publicly).

## Implementation

A Spring Boot application that implements the Lowest Unique Bid system for an online bidding platform. This system allows users to place bids and efficiently tracks the lowest unique bid in real-time.

## Features

- Place new bids with user ID and amount
- Track the lowest unique bid in real-time
- Thread-safe implementation for concurrent access
- RESTful API with Swagger documentation
- Internal API protection with API key
- Input validation and error handling

## Requirements

- Java 21
- Maven 3.6+
- Spring Boot 3.2.0

## API Endpoints

### Public API

- **POST** `/api/bids` - Place a new bid
  - Request Body:
    ```json
    {
      "userId": "john123",
      "amount": 100
    }
    ```
  - Response:
    ```json
    {
      "message": "Bid placed successfully",
      "currentLowestUniqueBid": 100
    }
    ```

### Internal API (Protected)

- **GET** `/internal/lowest-unique-bid` - Get current lowest unique bid
  - Headers:
    ```
    X-INTERNAL-KEY: super-secret-internal-token
    ```
  - Response:
    ```json
    {
      "message": "Success",
      "currentLowestUniqueBid": 100
    }
    ```

## Data Structures & Complexity

### `ConcurrentHashMap<Integer, Set<String>> amountToUsers`
- Maps bid amount → set of users who placed that amount
- Uses `CopyOnWriteArraySet` for thread-safe operations
- Ensures idempotency for duplicate bids from the same user

### `AtomicReference<Set<Integer>> uniqueAmounts`
- Tracks amounts that are currently unique (only one bidder)
- Thread-safe implementation using `ConcurrentHashMap.newKeySet()`
- Efficiently retrieves the lowest unique bid

### Time Complexity
- **Adding a bid**: O(1) amortized for hash operations
- **Finding lowest unique bid**: O(1) using `min()` on the set of unique amounts
- **Memory**: Proportional to the number of distinct bid amounts and user-bid pairs

## Thread Safety
- Thread-safe implementation using concurrent collections
- Synchronized blocks for compound operations
- Safe for concurrent access from multiple threads

## Getting Started

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd LowestUniqueBidSystem
   ```

2. **Build the application**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access Swagger UI**
   Open your browser and navigate to:
   ```
   http://localhost:8080/swagger-ui/index.html
   ```

## Configuration

Configuration can be modified in `src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8080

# Internal API Security
app.internal-api.key=super-secret-internal-token
```

## Extensions & Improvements

1. **Persistence Layer**
   - Store bids in a database for persistence
   - Add data retention policies

2. **Enhanced Security**
   - Implement JWT authentication
   - Add rate limiting
   - Enable HTTPS

3. **Monitoring & Observability**
   - Add Prometheus metrics
   - Implement distributed tracing
   - Add health checks

4. **Scalability**
   - Add caching layer (e.g., Redis)
   - Implement distributed locking for high concurrency

## Example Usage

```bash
# Place a bid
curl -X POST "http://localhost:8080/api/bids" \
     -H "Content-Type: application/json" \
     -d '{"userId":"john", "amount": 10}'

# Get lowest unique bid (internal API)
curl -H "X-INTERNAL-KEY: super-secret-internal-token" \
     "http://localhost:8080/api/internal/lowest-unique-bid"
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
