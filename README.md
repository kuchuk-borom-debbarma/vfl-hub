# VFL Hub - Visual Flow Logger Framework

VFL Hub is the server component of the Visual Flow Logger (VFL) framework, designed to capture, store, and provide access to execution flow data from applications. It serves as a centralized hub for collecting blocks (execution units) and their associated logs, providing insights into application flow and behavior.

## Architecture Overview

VFL Hub is built using Spring Boot and follows a layered architecture:

- **Controller Layer**: REST API endpoints for data ingestion and retrieval
- **Service Layer**: Business logic for processing blocks and logs
- **Repository Layer**: Data access using JPA repositories
- **Queue System**: Asynchronous processing for performance optimization

## Core Concepts

### Blocks
A **Block** represents an execution unit in your application flow. Each block has:
- Unique identifier and name
- Creation timestamp
- Optional parent-child relationships for hierarchical flows
- Lifecycle timestamps (entered, exited, returned)
- Exit messages for context

### Block Logs
**Block Logs** provide detailed information about what happens within blocks:
- Associated with specific blocks
- Support different log types (INFO, WARN, ERROR, TRACE variants, etc.)
- Can reference other blocks
- Support hierarchical relationships through parent log IDs

## REST API Endpoints

### Block Management

#### Add Blocks
```http
POST /api/v1/blocks
Content-Type: application/json

[
  {
    "id": "block-001",
    "name": "Authentication Flow",
    "parentBlockId": null,
    "createdAt": 1695067050000
  }
]
```

#### Get Root Blocks (Paginated)
```http
GET /api/v1/blocks?cursor=<cursor>&limit=10
```

#### Get Block by ID
```http
GET /api/v1/block/{id}
```

### Block Lifecycle Updates

#### Block Entered
```http
POST /api/v1/block-entered
Content-Type: application/json

{
  "block-001": 1695067055000
}
```

#### Block Exited
```http
POST /api/v1/block-exited
Content-Type: application/json

{
  "block-001": 1695067080000
}
```

#### Block Returned
```http
POST /api/v1/block-returned
Content-Type: application/json

{
  "block-001": 1695067090000
}
```

### Log Management

#### Add Logs
```http
POST /api/v1/logs
Content-Type: application/json

[
  {
    "id": "log-001",
    "blockId": "block-001",
    "message": "Starting authentication process",
    "parentLogId": null,
    "referencedBlockId": null,
    "timestamp": 1695067056000,
    "logType": "INFO"
  }
]
```

#### Get Logs by Block ID (Paginated)
```http
GET /api/v1/logs/{blockId}?cursor=<cursor>&limit=10
```

## Data Models

### Block Entity
```java
public class Block {
    private String id;                    // Unique identifier
    private String name;                  // Human-readable name
    private String parentBlockId;         // Parent block (nullable)
    private long createdAt;              // Creation timestamp
    private Long enteredAt;              // Entry timestamp (nullable)
    private Long exitedAt;               // Exit timestamp (nullable)
    private String exitMessage;          // Exit context (nullable)
    private Long returnedAt;             // Return timestamp (nullable)
    private long persistedTime;          // When saved to database
}
```

### Block Log Entity
```java
public class BlockLog {
    private String id;                    // Unique identifier
    private String blockId;              // Associated block
    private String message;              // Log message (nullable)
    private String parentLogId;          // Parent log (nullable)
    private String referencedBlockId;    // Referenced block (nullable)
    private long timestamp;              // Log timestamp
    private LogType logType;             // Log severity/type
    private long persistedTime;          // When saved to database
}
```

### Log Types
- `INFO`: General information
- `WARN`: Warning messages
- `ERROR`: Error conditions
- `TRACE_PRIMARY`: Primary execution trace
- `TRACE_PARALLEL_JOIN`: Parallel execution join point
- `TRACE_PARALLEL`: Parallel execution
- `TRACE_REMOTE`: Remote operation trace
- `PUBLISH_EVENT`: Event publishing
- `LISTEN_EVENT`: Event listening

## Features

### Cursor-Based Pagination
Both blocks and logs support cursor-based pagination for efficient data retrieval:
- Cursors are Base64-encoded strings containing ID and timestamp
- Ensures consistent ordering and handles concurrent updates
- Default limits: 10 for blocks, 2 for logs (configurable)

### Hierarchical Relationships
- Blocks can have parent-child relationships
- Logs can reference other logs and blocks
- Supports complex flow visualization

### Queue System
VFL Hub includes an in-memory queue system for performance optimization:
- Batches incoming requests to reduce database load
- Configurable flush intervals (default: 60 seconds)
- Automatic capacity-based flushing (default: 10 items)
- Graceful shutdown handling

### Advanced Querying
Uses Blaze Persistence for complex queries:
- Recursive Common Table Expressions (CTEs) for hierarchical data
- Efficient pagination with compound sorting
- Optimized joins for referenced block resolution

## Configuration

### Queue Configuration
```properties
# Queue flush interval in seconds (default: 60)
vfl.queue.flush.interval.seconds=60
```

### Database Configuration
VFL Hub supports any JPA-compatible database. The application uses:
- JPA/Hibernate for ORM
- Blaze Persistence for advanced querying
- Connection pooling for performance

## Error Handling

The API includes comprehensive error handling:
- Custom `VFLException` with HTTP status codes
- Standardized error response format
- Detailed logging for troubleshooting

Example error response:
```json
{
  "error": "VFL_EXCEPTION",
  "message": "Invalid cursor provided for fetching root blocks",
  "timestamp": "2023-09-18T16:17:30.123Z"
}
```

## Usage Patterns

### Basic Flow Tracking
1. Create blocks representing major execution units
2. Update block lifecycle as execution progresses
3. Add logs for detailed tracing within blocks
4. Query data for analysis and visualization

### Hierarchical Flow Analysis
1. Create parent blocks for major operations
2. Create child blocks for sub-operations
3. Use parent-child relationships to build execution trees
4. Analyze flow patterns and performance bottlenecks

### Event-Driven Monitoring
1. Use `PUBLISH_EVENT` and `LISTEN_EVENT` log types
2. Reference related blocks in logs
3. Track event propagation across system boundaries
4. Monitor asynchronous operation completion

## Development Notes

### Future Enhancements (TODOs)
- Implement TimescaleDB hypertables for better time-series performance
- Cloud service deployment
- Enhanced queue service with persistent storage
- Block state transition events (entered, exited, returned)

### Performance Considerations
- Use batch operations for high-volume scenarios
- Consider database indexing on timestamp and ID fields
- Monitor queue performance and adjust flush intervals
- Implement connection pooling for database access

### Testing
The codebase includes:
- Unit tests for controllers using MockMvc
- Mock services for isolated testing
- Spring Boot test configuration

## Getting Started

1. Configure your database connection
2. Run the Spring Boot application
3. Start sending blocks and logs via REST API
4. Query the data for analysis and visualization
5. Integrate with your monitoring and observability stack

VFL Hub provides a robust foundation for application flow monitoring, offering the flexibility to track simple linear flows or complex hierarchical execution patterns with detailed logging and efficient querying capabilities.