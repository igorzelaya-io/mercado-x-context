# MercadoX Context

## Overview

`mercado-x-context` contains shared cross-cutting configuration and utilities.

It provides contextual infrastructure across the ecosystem.

---

## Responsibilities

- OrgId context holder
- Context propagation utilities
- Shared interceptors
- Common utility classes
- Shared configuration abstractions

---

## Core Concept

MercadoX is a **multi-tenant system**.

This module ensures:

- orgId is propagated correctly
- Thread-local context is maintained
- Messaging headers carry tenant information

---

## Why This Exists

To:

- Avoid duplicating utility logic
- Ensure consistent tenant handling
- Centralize cross-cutting concerns
- Maintain clean architecture boundaries