# RideFlow (Still in development)

**RideFlow** is a **distributed, event-driven ride-sharing platform** built with **Spring Boot 3.5.6** and **Java 21**.
It demonstrates modern microservice architecture patterns — combining synchronous REST communication and asynchronous messaging with Kafka — while showcasing clean separation of concerns, resilience patterns, and full cloud-native deployment capabilities.

---

## 🧩 Architecture Overview

RideFlow consists of **five microservices**:

| Service             | Description                                                                                 | Tech Stack                                   |
| ------------------- | ------------------------------------------------------------------------------------------- | -------------------------------------------- |
| **API Gateway**     | Entry point for all client requests. Handles routing, JWT validation, and circuit breaking. | Spring Cloud Gateway, Resilience4j           |
| **User Service**    | Manages rider and driver user profiles linked to Keycloak identities.                       | Spring Boot, PostgreSQL, Flyway              |
| **Ride Service**    | Orchestrates ride lifecycle, implements Saga pattern, interacts with drivers and payments.  | Spring Boot, PostgreSQL, Kafka, Resilience4j |
| **Driver Service**  | Manages driver availability, location, and operational data.                                | Spring Boot, MongoDB, Kafka                  |
| **Payment Service** | Simulates payment authorization and compensation flows in the Saga.                         | Spring Boot, PostgreSQL, Kafka               |

**Integration services:**

* **Keycloak** – Identity and Access Management (OAuth2, OpenID Connect).
* **Kafka (Redpanda)** – Message broker for Saga choreography.
* **PostgreSQL** – Relational storage (user, ride, payment).
* **MongoDB** – NoSQL storage for driver state.

---

## ⚙️ Core Features

* **Microservices architecture** — independently deployable Spring Boot services.
* **Synchronous & asynchronous communication** — REST + Kafka event-driven design.
* **Saga Pattern (choreography)** — distributed transaction across Ride, Driver, and Payment services.
* **Circuit Breaker** — via Resilience4j in gateway and ride-service.
* **OAuth2 / OIDC Authentication** — secured by Keycloak.
* **Polyglot persistence** — PostgreSQL + MongoDB.
* **Containerized deployment** — full setup via Docker Compose, ready for Kubernetes.
* **Infrastructure as Code** — K8s manifests with Kustomize overlays.

---

## 🧱 Technologies

| Category               | Tools / Frameworks                                  |
| ---------------------- | --------------------------------------------------- |
| **Language**           | Java 21                                             |
| **Frameworks**         | Spring Boot 3.5.6, Spring Cloud 2025.0.0 (Kilburn)  |
| **Messaging**          | Apache Kafka (Redpanda)                             |
| **Databases**          | PostgreSQL 16, MongoDB 7                            |
| **Security**           | Keycloak 24.x, OAuth2 Resource Server               |
| **Resilience**         | Resilience4j, Circuit Breaker, Retry                |
| **Infra / Deployment** | Docker, Docker Compose, Kubernetes (Kustomize)      |
| **Observability**      | Spring Boot Actuator, Micrometer (Prometheus-ready) |
| **Build / Codegen**    | Maven, OpenAPI Generator                            |

---

## 🔐 Security Flow

1. Users register and authenticate via **Keycloak**.
2. Clients call APIs with **JWT tokens**.
3. The **API Gateway** validates tokens and routes traffic.
4. Downstream services also validate JWTs for defense in depth.

---

## 🧬 Saga Flow Example

1. `ride-service` emits **RideRequested** → Kafka
2. `driver-service` consumes → assigns driver → emits **DriverAssigned**
3. `payment-service` consumes → authorizes payment → emits **PaymentAuthorized**
4. `ride-service` updates ride status → completed or failed
5. Compensation handled via **PaymentFailed** and driver release

---

## 🧑‍💻 Author & Purpose

RideFlow was designed as a **reference project** and **learning exercise** for:

* Microservice design and orchestration with Spring Boot
* Event-driven architecture and data consistency patterns
* Secure, production-grade distributed systems with Keycloak and Spring Cloud

