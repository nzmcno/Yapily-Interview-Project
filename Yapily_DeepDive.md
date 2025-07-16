# Yapily – Company Technical Overview (Open Banking Infrastructure)

> **Disclaimer**: This overview is based on research and publicly available information. The technical details and specifications presented here are not official company data and should be used for informational purposes only.

Yapily is a London-based Open Banking infrastructure provider founded in 2017. It offers a standardized, white-label API platform that enables fintech companies and regulated institutions to securely access bank account data and initiate payments across the UK and EU. Yapily is known for its "quiet infrastructure" philosophy — operating entirely in the background without any user-facing components.

---

## 🔧 What Yapily Does

- **Account Information Services (AIS)**: Fetch user-permitted data like account balances, IBAN, transactions.
- **Payment Initiation Services (PIS)**: Allow users to initiate real-time payments directly from their bank accounts.
- **VRP (Variable Recurring Payments)**: Facilitate recurring payments with flexible amounts.
- **Data Enrichment**: Categorize and enhance transaction data for insights and compliance.
- **Compliance APIs**: Enable AML checks, income verification, and regulatory reporting.

---

## ⚙️ Core Technology Stack

| Layer                | Technologies                                 |
| -------------------- | -------------------------------------------- |
| Language / Framework | Java 17+, Spring Boot, Spring Batch          |
| API Handling         | WebClient (non-blocking), OpenAPI            |
| Database             | PostgreSQL (prod), H2 (test), Flyway         |
| Testing              | JUnit 5, Mockito, WireMock, Testcontainers   |
| Resilience           | Spring Retry, Timeouts, Custom Health Checks |
| Monitoring           | Actuator, Prometheus, Grafana, Loki          |
| Build                | Maven, Spring Boot Maven Plugin (Buildpacks) |
| CI/CD                | GitHub Actions, Nexus/GCR/GHCR               |
| Deployment           | Docker, Kubernetes, Helm                     |

---

## 🔐 Security & Compliance

- Fully compliant with **PSD2**, **SCA**, **GDPR**, and FCA regulations.
- OAuth2.0 based authorization for user consent flow.
- Supports secure key and secret management (Vault/GCP).

---

## 🚀 CI/CD & Deployment Flow

1. Build with: `mvn spring-boot:build-image`
2. Push to registry: `docker push ghcr.io/yapily/<service>:tag`
3. Deploy via Helm: `helm upgrade --install ...`
4. Enable SLA monitoring, health probes, and autoscaling on K8s

---

## 🧪 Testing Culture

- Unit tests with JUnit + Mockito
- HTTP mocks with WireMock
- Integration tests with Testcontainers (PostgreSQL)
- CI pipeline with GitHub Actions + artifact reporting
- Custom health checks implemented for external dependencies

---

## 💰 Monetization Model

| Type                  | Description                               |
| --------------------- | ----------------------------------------- |
| API Call Pricing      | Pay-per-use based on volume               |
| Subscription Tiers    | Starter → Growth → Enterprise plans       |
| Payment Commissions   | % fee per initiated transaction           |
| White-label Licensing | Fixed annual enterprise usage fees        |
| Add-ons               | Categorization, risk scoring, VRP modules |

---

## 🔁 Main Competitors

| Company     | Differentiator                           |
| ----------- | ---------------------------------------- |
| TrueLayer   | Strong SDKs, better frontend UX          |
| Tink (Visa) | Focused on data analytics                |
| Token.io    | Real-time payment infrastructure         |
| Salt Edge   | Global reach, many banks (5000+)         |
| Plaid       | US-dominant, end-to-end financial access |

---

## 🌍 Coverage & Expansion

- Supports over **2,000+ banks** in the UK & EU.
- Offices in London and Vilnius.
- Fully licensed by **FCA (UK)** and **Bank of Lithuania**.

---

## 👥 Customer Segments

- Fintechs & Digital Wallets
- Accounting & ERP Platforms
- Lending & Risk Analytics
- iGaming & Insurance (AML/KYC)
- E-commerce & Real-time Payments

---

## 🧠 Strategic Position

> Yapily provides modular, backend-first open banking infrastructure that powers financial innovation without interfering in branding or UX. With high SLAs, strong observability, and deep developer tooling, it enables regulated companies to scale across Europe with full PSD2 compliance and maximum control.

---
