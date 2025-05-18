# Java Concurrency Demos

This repository presents a set of small Java code examples that explore fundamental concurrency issues such as
atomicity, instruction reordering, and visibility.

The examples are intentionally minimalistic and aim to highlight the subtle pitfalls developers may encounter when
working with threads and shared state in Java.

## Overview

The demos are grouped into three main categories:

### 1. Atomicity

- **Problem:** A shared counter is updated concurrently without proper synchronisation, leading to race conditions.
- **Illustrative solution:** Introduces `AtomicInteger` to ensure atomic operations.

### 2. Reordering

- **Problem:** Instruction reordering by the compiler or CPU may lead to unexpected results when threads observe
  operations out of order.
- **Illustrative solution:** The use of `volatile` prevents reordering and ensures visibility across threads.

### 3. Visibility

- **Problem:** One thread modifies a flag to signal another thread, but due to lack of proper memory synchronisation,
  the change may never be observed.
- **Illustrative solution:** Declaring the flag as `volatile` ensures that changes are visible to other threads.

## Educational Purpose

These examples are inspired by:

- *Java Concurrency in Practice* by Brian Goetz et al., 2006
- The Java Language Specification, Java SE 21 Edition, particularly chapter 17 "Threads and Locks"

They are intended **solely for educational and illustrative purposes**. The code is deliberately unsophisticated to
expose core behaviours without distraction.

The proposed solutions are merely illustrative. Other valid approaches to addressing these concurrency challenges
exist and should be considered depending on the context.

## Disclaimer

Use at your own risk. These examples are designed to provoke incorrect behaviour under certain conditions — precisely to
demonstrate what can go wrong.

## AI Tools Used

This project utilises AI tools, specifically ChatGPT by OpenAI, to assist with documentation. All AI-generated content
has been reviewed and validated by human contributors to ensure accuracy and quality.
