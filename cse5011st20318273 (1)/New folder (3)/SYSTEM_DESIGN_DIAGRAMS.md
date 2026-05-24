# Task B: System Design and Database Design - EcoStay Retreat

## 1. System Architecture Overview

### High-Level Architecture Diagram
```
┌─────────────────────────────────────────────────────────────────┐
│                    EcoStay Retreat Mobile App                   │
├─────────────────────────────────────────────────────────────────┤
│  Presentation Layer (Android UI)                               │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐│
│  │   Login/    │ │    Rooms    │ │ Activities  │ │   Profile   ││
│  │  Register   │ │  Fragment   │ │  Fragment   │ │  Fragment   ││
│  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘│
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐│
│  │ MainActivity│ │Nature Reserves│ │Notification │ │ Validation │ │
│  │             │ │  Fragment   │ │  Service    │ │   Utils    │ │
│  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘│
├─────────────────────────────────────────────────────────────────┤
│  Business Logic Layer                                          │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐│
│  │   User      │ │    Room     │ │  Activity   │ │   Nature    ││
│  │ Management  │ │  Booking    │ │ Reservation │ │  Reserve    ││
│  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘│
├─────────────────────────────────────────────────────────────────┤
│  Data Access Layer                                             │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐│
│  │  Firebase   │ │  Firestore  │ │   Cloud     │ │   Local     ││
│  │    Auth     │ │  Database   │ │ Messaging   │ │   Cache     ││
│  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘│
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Firebase Backend Services                    │
├─────────────────────────────────────────────────────────────────┤
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐│
│  │  Firebase   │ │  Firestore  │ │   Cloud     │ │  Firebase   ││
│  │Authentication│ │  NoSQL DB   │ │ Functions   │ │  Storage    ││
│  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘│
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐│
│  │   Cloud     │ │  Analytics  │ │ Performance │ │   Hosting   ││
│  │  Messaging  │ │             │ │ Monitoring  │ │             ││
│  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘│
└─────────────────────────────────────────────────────────────────┘
```

## 2. Detailed Component Architecture

### 2.1 Application Flow Diagram
```
User Launch App
        │
        ▼
┌───────────────┐    No     ┌─────────────────┐
│ Check Auth    │──────────▶│  Login/Register │
│ Status        │           │     Screen      │
└───────────────┘           └─────────────────┘
        │ Yes                        │
        ▼                           │ Success
┌───────────────┐◀───────────────────┘
│  Main Screen  │
│ (Bottom Nav)  │
└───────────────┘
        │
        ▼
┌─────────────────────────────────────────────┐
│              Main Features                  │
├─────────────┬─────────────┬─────────────────┤
│    Rooms    │ Activities  │ Nature Reserves │
│  Fragment   │  Fragment   │    Fragment     │
└─────────────┴─────────────┴─────────────────┘
        │
        ▼
┌───────────────┐
│    Profile    │
│   Fragment    │
└───────────────┘
```

### 2.2 Authentication Flow
```
┌─────────────┐    Input     ┌─────────────┐    Validate    ┌─────────────┐
│    User     │─────────────▶│ Validation  │───────────────▶│   Firebase  │
│   Input     │              │   Utils     │                │    Auth     │
└─────────────┘              └─────────────┘                └─────────────┘
                                    │                              │
                                    ▼ Error                       ▼ Success
                             ┌─────────────┐                ┌─────────────┐
                             │    Show     │                │   Create    │
                             │    Error    │                │   User      │
                             └─────────────┘                │  Document   │
                                                           └─────────────┘
                                                                  │
                                                                  ▼
                                                           ┌─────────────┐
                                                           │  Navigate   │
                                                           │  to Main    │
                                                           └─────────────┘
```

## 3. Database Design (Firestore NoSQL)

### 3.1 Database Schema Overview (Updated to Local Room Database)
```
EcoStay Retreat Local Database (SQLite with Room)
├── users/
│   └── User Entity:
│       ├── id: int (Primary Key, Auto-generated)
│       ├── email: string (Unique)
│       ├── password: string (For demo - in production use secure hashing)
│       ├── name: string
│       ├── phone: string
│       ├── preferences: string
│       └── travelDates: string
│
├── rooms/
│   └── Room Entity:
│       ├── id: int (Primary Key, Auto-generated)
│       ├── name: string
│       ├── type: string
│       ├── description: string
│       ├── price: double
│       ├── imageUrl: string
│       ├── ecoFriendly: boolean
│       ├── amenities: string
│       └── available: boolean
│
├── activities/
│   └── Activity Entity:
│       ├── id: int (Primary Key, Auto-generated)
│       ├── name: string
│       ├── type: string
│       ├── description: string
│       ├── price: double
│       ├── imageUrl: string
│       ├── duration: string
│       ├── difficulty: string
│       ├── location: string
│       └── available: boolean
│
├── nature_reserves/
│   └── NatureReserve Entity:
│       ├── id: int (Primary Key, Auto-generated)
│       ├── name: string
│       ├── location: string
│       ├── description: string
│       ├── imageUrl: string
│       ├── greenInitiatives: string
│       ├── sustainabilityPractices: string
│       ├── rating: double
│       └── accessible: boolean
│
└── bookings/
    └── Booking Entity:
        ├── id: int (Primary Key, Auto-generated)
        ├── userId: int (Foreign Key)
        ├── roomId: int (Foreign Key, nullable)
        ├── activityId: int (Foreign Key, nullable)
        ├── bookingType: string ("room" or "activity")
        ├── checkInDate: string
        ├── checkOutDate: string
        ├── bookingDate: string
        ├── totalPrice: double
        ├── status: string
        ├── guestName: string
        └── numberOfGuests: int
```

### 3.2 Entity Relationship Diagram
```
┌─────────────────┐         ┌─────────────────┐
│      Users      │         │      Rooms      │
├─────────────────┤         ├─────────────────┤
│ + userId (PK)   │         │ + roomId (PK)   │
│ + fullName      │    1:N  │ + name          │
│ + email         │◀────────│ + type          │
│ + phone         │         │ + description   │
│ + createdAt     │         │ + pricePerNight │
│ + preferences   │         │ + isAvailable   │
│ + history       │         │ + imageUrl      │
└─────────────────┘         └─────────────────┘
         │                           │
         │ 1:N                       │ 1:N
         ▼                           ▼
┌─────────────────┐         ┌─────────────────┐
│    Bookings     │         │   Activities    │
├─────────────────┤         ├─────────────────┤
│ + bookingId(PK) │         │ + activityId(PK)│
│ + userId (FK)   │         │ + name          │
│ + roomId (FK)   │         │ + category      │
│ + checkIn       │         │ + description   │
│ + checkOut      │         │ + price         │
│ + guestCount    │         │ + duration      │
│ + totalPrice    │         │ + isAvailable   │
│ + status        │         │ + maxParticipants│
└─────────────────┘         └─────────────────┘
                                     │
                                     │ 1:N
                                     ▼
                            ┌─────────────────┐
                            │  Reservations   │
                            ├─────────────────┤
                            │ + reservationId │
                            │ + userId (FK)   │
                            │ + activityId(FK)│
                            │ + resDate       │
                            │ + participants  │
                            │ + totalPrice    │
                            │ + status        │
                            └─────────────────┘
```

## 4. Security Architecture

### 4.1 Security Layers
```
┌─────────────────────────────────────────────────────────────┐
│                    Security Architecture                    │
├─────────────────────────────────────────────────────────────┤
│  Application Layer Security                                 │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐           │
│  │   Input     │ │   Session   │ │    Data     │           │
│  │ Validation  │ │ Management  │ │ Encryption  │           │
│  └─────────────┘ └─────────────┘ └─────────────┘           │
├─────────────────────────────────────────────────────────────┤
│  Network Layer Security                                     │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐           │
│  │    HTTPS    │ │ Certificate │ │   Request   │           │
│  │ Encryption  │ │   Pinning   │ │ Validation  │           │
│  └─────────────┘ └─────────────┘ └─────────────┘           │
├─────────────────────────────────────────────────────────────┤
│  Firebase Security                                          │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐           │
│  │  Security   │ │  Database   │ │    User     │           │
│  │    Rules    │ │    Rules    │ │    Auth     │           │
│  └─────────────┘ └─────────────┘ └─────────────┘           │
└─────────────────────────────────────────────────────────────┘
```

### 4.2 Firestore Security Rules
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Users can only access their own data
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Rooms are readable by authenticated users
    match /rooms/{roomId} {
      allow read: if request.auth != null;
      allow write: if false; // Only admin can modify
    }
    
    // Activities are readable by authenticated users
    match /activities/{activityId} {
      allow read: if request.auth != null;
      allow write: if false; // Only admin can modify
    }
    
    // Bookings are private to users
    match /bookings/{bookingId} {
      allow read, write: if request.auth != null && 
        request.auth.uid == resource.data.userId;
    }
    
    // Reservations are private to users
    match /reservations/{reservationId} {
      allow read, write: if request.auth != null && 
        request.auth.uid == resource.data.userId;
    }
  }
}
```

## 5. Performance and Scalability Design

### 5.1 Performance Optimization Strategy
```
┌─────────────────────────────────────────────────────────────┐
│                Performance Architecture                     │
├─────────────────────────────────────────────────────────────┤
│  Client-Side Optimization                                   │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐           │
│  │   Image     │ │    Data     │ │   Memory    │           │
│  │ Compression │ │   Caching   │ │ Management  │           │
│  └─────────────┘ └─────────────┘ └─────────────┘           │
├─────────────────────────────────────────────────────────────┤
│  Network Optimization                                       │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐           │
│  │  Pagination │ │    Lazy     │ │  Offline    │           │
│  │             │ │   Loading   │ │   Support   │           │
│  └─────────────┘ └─────────────┘ └─────────────┘           │
├─────────────────────────────────────────────────────────────┤
│  Backend Optimization                                       │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐           │
│  │  Database   │ │    CDN      │ │   Auto      │           │
│  │  Indexing   │ │  Delivery   │ │  Scaling    │           │
│  └─────────────┘ └─────────────┘ └─────────────┘           │
└─────────────────────────────────────────────────────────────┘
```

## 6. Design Decision Justifications

### 6.1 Architecture Decisions

#### Firebase Selection
**Decision**: Use Firebase as Backend-as-a-Service
**Justification**: 
- Reduces development time and complexity
- Provides real-time synchronization
- Includes authentication, database, and hosting
- Excellent Android integration
- Suitable for educational projects

#### NoSQL Database Choice
**Decision**: Use Firestore NoSQL database
**Justification**:
- Flexible schema for evolving requirements
- Real-time updates for booking availability
- Offline synchronization capabilities
- Scalable for growing user base
- JSON-like document structure matches mobile data

#### MVVM Architecture Pattern
**Decision**: Implement Model-View-ViewModel pattern
**Justification**:
- Separates UI logic from business logic
- Improves testability and maintainability
- Supports data binding and lifecycle awareness
- Recommended by Google for Android development

### 6.2 Key Technical Challenges Addressed

#### Challenge 1: Real-time Data Synchronization
**Problem**: Multiple users booking same rooms simultaneously
**Solution**: Firestore real-time listeners with optimistic locking
**Implementation**: Transaction-based booking with conflict resolution

#### Challenge 2: Offline Functionality
**Problem**: Users need access in areas with poor connectivity
**Solution**: Firestore offline persistence with local caching
**Implementation**: Automatic sync when connection restored

#### Challenge 3: Image Loading Performance
**Problem**: Large images affecting app performance
**Solution**: Glide library with caching and compression
**Implementation**: Progressive loading with placeholder images

#### Challenge 4: User Input Validation
**Problem**: Ensuring data integrity and security
**Solution**: Comprehensive validation utility class
**Implementation**: Client-side and server-side validation

## 7. Scalability Considerations

### 7.1 Horizontal Scaling Strategy
- **Database Sharding**: Partition data by geographic regions
- **CDN Integration**: Distribute static content globally
- **Load Balancing**: Distribute API requests across servers
- **Caching Strategy**: Multi-level caching implementation

### 7.2 Monitoring and Analytics
- **Performance Monitoring**: Firebase Performance Monitoring
- **Crash Reporting**: Firebase Crashlytics
- **User Analytics**: Firebase Analytics
- **Custom Metrics**: Business-specific KPIs tracking

## Conclusion

This system design provides a robust, scalable foundation for the EcoStay Retreat mobile application. The architecture emphasizes security, performance, and maintainability while leveraging modern cloud technologies. The design decisions are justified by technical requirements, educational objectives, and industry best practices.

The modular architecture allows for future enhancements and scalability, while the comprehensive database design ensures data integrity and efficient querying. Security measures protect user data and prevent unauthorized access, meeting modern mobile application standards.
