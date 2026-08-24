# Graph Report - .  (2026-08-21)

## Corpus Check
- cluster-only mode — file stats not available

## Summary
- 177 nodes · 299 edges · 17 communities (15 shown, 2 thin omitted)
- Extraction: 97% EXTRACTED · 3% INFERRED · 0% AMBIGUOUS · INFERRED: 8 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `893242f6`
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

## God Nodes (most connected - your core abstractions)
1. `JwtVerifier` - 14 edges
2. `KafkaPubSubConfig` - 12 edges
3. `JwtVerifierTest` - 10 edges
4. `IdempotencyAspect` - 8 edges
5. `KafkaIdempotencyAspect` - 8 edges
6. `TenantValidatorFilter` - 8 edges
7. `JwtAuthFilter` - 6 edges
8. `OrgIdContextHolder` - 6 edges
9. `MercadoXJwtAutoConfiguration` - 5 edges
10. `VerifiedJwt` - 5 edges

## Surprising Connections (you probably didn't know these)
- `JwtAuthFilter` --references--> `JwtVerifier`  [EXTRACTED]
  src/main/java/hn/shadowcore/mercadox/context/filter/JwtAuthFilter.java → src/main/java/hn/shadowcore/mercadox/context/security/JwtVerifier.java
- `TenantValidatorFilter` --references--> `AnonymousTenantValidator`  [EXTRACTED]
  src/main/java/hn/shadowcore/mercadox/context/filter/TenantValidatorFilter.java → src/main/java/hn/shadowcore/mercadox/context/validator/AnonymousTenantValidator.java
- `JwtVerifierTest` --references--> `JwtVerifier`  [EXTRACTED]
  src/test/java/hn/mercadoxcontext/utils/JwtVerifierTest.java → src/main/java/hn/shadowcore/mercadox/context/security/JwtVerifier.java

## Import Cycles
- None detected.

## Communities (17 total, 2 thin omitted)

### Community 0 - "JwtVerifier"
Cohesion: 0.16
Nodes (10): BeforeEach, Claims, JwtParser, RSAPrivateKey, RSAPublicKey, JwtVerifier, VerifiedJwt, JwtVerifierTest (+2 more)

### Community 1 - "KafkaPubSubConfig.java"
Cohesion: 0.19
Nodes (13): ConcurrentKafkaListenerContainerFactory, ConsumerFactory, KafkaProperties, KafkaTemplate, ProducerFactory, Bean, Configuration, DefaultErrorHandler (+5 more)

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
Cohesion: 0.28
Nodes (9): AutoConfiguration, ConfigurationProperties, Resource, JwtVerificationProperties, Bean, ConditionalOnMissingBean, EnableConfigurationProperties, RSAPublicKey (+1 more)

### Community 6 - "KafkaOrgIdPropagationAspect.java"
Cohesion: 0.23
Nodes (6): Around, Aspect, Component, ProceedingJoinPoint, KafkaOrgIdPropagationAspect, OrgIdContextHolder

### Community 7 - "mvnw"
Cohesion: 0.33
Nodes (6): mvnw script, clean(), die(), exec_maven(), set_java_home(), verbose()

### Community 8 - "JwtAuthFilter.java"
Cohesion: 0.33
Nodes (8): OncePerRequestFilter, Profile, FilterChain, HttpServletRequest, HttpServletResponse, Override, RequiredArgsConstructor, JwtAuthFilter

### Community 9 - "WebClientConfig.java"
Cohesion: 0.43
Nodes (5): Builder, ConditionalOnClass, Bean, Configuration, WebClientConfig

### Community 10 - "JwtConfig.java"
Cohesion: 0.48
Nodes (5): Bean, ConditionalOnMissingBean, Configuration, RSAPublicKey, JwtConfig

### Community 11 - "DltEventListener.java"
Cohesion: 0.53
Nodes (4): KafkaListener, DltEventListener, Component, Slf4j

### Community 13 - ".readPublicKey"
Cohesion: 0.47
Nodes (3): Resource, RSAPublicKey, PublicKeyUtils

### Community 14 - "KafkaIdempotent.java"
Cohesion: 0.83
Nodes (3): Retention, Target, KafkaIdempotent

### Community 15 - "KafkaOrgIdPropagated.java"
Cohesion: 0.83
Nodes (3): Retention, Target, KafkaOrgIdPropagated

## Knowledge Gaps
- **1 isolated node(s):** `mercado-x-context`
  These have ≤1 connection - possible missing edges or undocumented components.
- **2 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `JwtVerifier` connect `JwtVerifier` to `JwtAuthFilter.java`, `JwtConfig.java`, `MercadoXJwtAutoConfiguration.java`?**
  _High betweenness centrality (0.148) - this node is a cross-community bridge._
- **Why does `VerifiedJwt` connect `JwtVerifier` to `JwtAuthFilter.java`, `TenantValidatorFilter.java`?**
  _High betweenness centrality (0.075) - this node is a cross-community bridge._
- **Why does `OrgIdContextHolder` connect `KafkaOrgIdPropagationAspect.java` to `ProducerRecord`?**
  _High betweenness centrality (0.045) - this node is a cross-community bridge._
- **What connects `mercado-x-context` to the rest of the system?**
  _1 weakly-connected nodes found - possible documentation gaps or missing edges._