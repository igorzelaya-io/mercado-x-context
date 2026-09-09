# Graph Report - mercado-x-context  (2026-09-08)

## Corpus Check
- 39 files · ~5,410 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 274 nodes · 469 edges · 20 communities (18 shown, 2 thin omitted)
- Extraction: 97% EXTRACTED · 3% INFERRED · 0% AMBIGUOUS · INFERRED: 15 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `5548abd6`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- JwtVerifier
- KafkaPubSubConfig.java
- IdempotencyAspect.java
- TenantValidatorFilter.java
- KafkaIdempotencyAspect.java
- MercadoXJwtAutoConfiguration.java
- KafkaOrgIdPropagationAspect.java
- mvnw
- JwtAuthFilter.java
- WebClientConfig.java
- JwtConfig.java
- DltEventListener.java
- ProducerRecord
- .readPublicKey
- KafkaIdempotent.java
- KafkaOrgIdPropagated.java
- mercado-x-context
- EncryptionAutoConfigurationTest.java
- KafkaCorrelationIdPropagated.java
- CLAUDE.md

## God Nodes (most connected - your core abstractions)
1. `JwtVerifier` - 14 edges
2. `KafkaPubSubConfig` - 13 edges
3. `JwtVerifierTest` - 10 edges
4. `MercadoX Context` - 10 edges
5. `IdempotencyAspect` - 8 edges
6. `KafkaIdempotencyAspect` - 8 edges
7. `MasterKeyProperties` - 8 edges
8. `TenantValidatorFilter` - 8 edges
9. `KafkaCorrelationIdPropagationAspectTest` - 8 edges
10. `EnvVarMasterKeyServiceTest` - 8 edges

## Surprising Connections (you probably didn't know these)
- `KafkaCorrelationIdPropagationAspectTest` --references--> `KafkaCorrelationIdPropagationAspect`  [EXTRACTED]
  src/test/java/hn/shadowcore/mercadox/context/aspect/KafkaCorrelationIdPropagationAspectTest.java → src/main/java/hn/shadowcore/mercadox/context/aspect/KafkaCorrelationIdPropagationAspect.java
- `JwtAuthFilter` --references--> `JwtVerifier`  [EXTRACTED]
  src/main/java/hn/shadowcore/mercadox/context/filter/JwtAuthFilter.java → src/main/java/hn/shadowcore/mercadox/context/security/JwtVerifier.java
- `TenantValidatorFilter` --references--> `AnonymousTenantValidator`  [EXTRACTED]
  src/main/java/hn/shadowcore/mercadox/context/filter/TenantValidatorFilter.java → src/main/java/hn/shadowcore/mercadox/context/validator/AnonymousTenantValidator.java
- `JwtVerifierTest` --references--> `JwtVerifier`  [EXTRACTED]
  src/test/java/hn/mercadoxcontext/utils/JwtVerifierTest.java → src/main/java/hn/shadowcore/mercadox/context/security/JwtVerifier.java

## Import Cycles
- None detected.

## Communities (20 total, 2 thin omitted)

### Community 0 - "JwtVerifier"
Cohesion: 0.09
Nodes (23): BeforeEach, Claims, JwtParser, OncePerRequestFilter, Profile, RSAPrivateKey, Bean, ConditionalOnMissingBean (+15 more)

### Community 1 - "KafkaPubSubConfig.java"
Cohesion: 0.18
Nodes (14): AutoConfigureBefore, ConcurrentKafkaListenerContainerFactory, ConsumerFactory, KafkaProperties, KafkaTemplate, ProducerFactory, AutoConfiguration, Bean (+6 more)

### Community 2 - "IdempotencyAspect.java"
Cohesion: 0.24
Nodes (11): IdempotencyAspect, Around, Aspect, Component, ProceedingJoinPoint, RequiredArgsConstructor, Slf4j, IdempotentOperation (+3 more)

### Community 3 - "TenantValidatorFilter.java"
Cohesion: 0.25
Nodes (9): ConditionalOnBean, FilterChain, HttpServletRequest, HttpServletResponse, Override, RequiredArgsConstructor, Slf4j, TenantValidatorFilter (+1 more)

### Community 4 - "KafkaIdempotencyAspect.java"
Cohesion: 0.23
Nodes (9): RedisIdempotencyChecker, Around, Aspect, Component, ProceedingJoinPoint, RequiredArgsConstructor, Slf4j, KafkaIdempotencyAspect (+1 more)

### Community 5 - "MercadoXJwtAutoConfiguration.java"
Cohesion: 0.18
Nodes (12): ConfigurationProperties, Resource, JwtVerificationProperties, AutoConfiguration, Bean, ConditionalOnMissingBean, EnableConfigurationProperties, RSAPublicKey (+4 more)

### Community 6 - "KafkaOrgIdPropagationAspect.java"
Cohesion: 0.12
Nodes (12): Around, Aspect, Component, ProceedingJoinPoint, KafkaOrgIdPropagationAspect, ProducerRecord, KafkaProducerRecordFactory, OrgIdContextHolder (+4 more)

### Community 7 - "mvnw"
Cohesion: 0.33
Nodes (6): mvnw script, clean(), die(), exec_maven(), set_java_home(), verbose()

### Community 8 - "JwtAuthFilter.java"
Cohesion: 0.12
Nodes (14): ExtendWith, Around, Aspect, Component, ProceedingJoinPoint, KafkaCorrelationIdPropagationAspect, CorrelationIdContext, AfterEach (+6 more)

### Community 9 - "WebClientConfig.java"
Cohesion: 0.43
Nodes (5): Builder, ConditionalOnClass, Bean, Configuration, WebClientConfig

### Community 10 - "JwtConfig.java"
Cohesion: 0.20
Nodes (8): MasterKeyService, SecretKey, EnvVarMasterKeyService, Override, ConfigurationProperties, MasterKeyProperties, EnvVarMasterKeyServiceTest, Test

### Community 11 - "DltEventListener.java"
Cohesion: 0.53
Nodes (4): KafkaListener, DltEventListener, Component, Slf4j

### Community 12 - "ProducerRecord"
Cohesion: 0.18
Nodes (10): Configuration Reference, Idempotency, Internal Dependencies, JWT Verification, Kafka Pub/Sub Configuration, MercadoX Context, Overview, Responsibilities (+2 more)

### Community 13 - ".readPublicKey"
Cohesion: 0.39
Nodes (7): ConditionalOnProperty, EncryptionAutoConfiguration, AutoConfiguration, Bean, ConditionalOnMissingBean, EnableConfigurationProperties, MasterKeyService

### Community 14 - "KafkaIdempotent.java"
Cohesion: 0.83
Nodes (3): Retention, Target, KafkaIdempotent

### Community 15 - "KafkaOrgIdPropagated.java"
Cohesion: 0.83
Nodes (3): Retention, Target, KafkaOrgIdPropagated

### Community 17 - "EncryptionAutoConfigurationTest.java"
Cohesion: 0.53
Nodes (3): ApplicationContextRunner, EncryptionAutoConfigurationTest, Test

### Community 18 - "KafkaCorrelationIdPropagated.java"
Cohesion: 0.83
Nodes (3): Retention, Target, KafkaCorrelationIdPropagated

## Knowledge Gaps
- **11 isolated node(s):** `mercado-x-context`, `graphify`, `Overview`, `Responsibilities`, `Tenant Context Propagation` (+6 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **2 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `JwtVerifier` connect `JwtVerifier` to `MercadoXJwtAutoConfiguration.java`?**
  _High betweenness centrality (0.100) - this node is a cross-community bridge._
- **Why does `TenantValidatorFilter` connect `TenantValidatorFilter.java` to `JwtVerifier`?**
  _High betweenness centrality (0.072) - this node is a cross-community bridge._
- **Why does `VerifiedJwt` connect `JwtVerifier` to `TenantValidatorFilter.java`?**
  _High betweenness centrality (0.047) - this node is a cross-community bridge._
- **What connects `mercado-x-context`, `graphify`, `Overview` to the rest of the system?**
  _11 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `JwtVerifier` be split into smaller, more focused modules?**
  _Cohesion score 0.09390243902439024 - nodes in this community are weakly interconnected._
- **Should `KafkaOrgIdPropagationAspect.java` be split into smaller, more focused modules?**
  _Cohesion score 0.11954022988505747 - nodes in this community are weakly interconnected._
- **Should `JwtAuthFilter.java` be split into smaller, more focused modules?**
  _Cohesion score 0.11895161290322581 - nodes in this community are weakly interconnected._