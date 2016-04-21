# Resilience Tutorial

This is the GitHub repository supporting a resilience tutorial that provides a step-by-step introduction to several basic resilience patterns.

NOTE: _Currently, the tutorial is not (yet) intended to be used as self-training without a trainer._

All exercises themselves are located in the corresponding `exercise_<number>_<description>` folders, including the required documentation, sample solutions (./sample_solutions/*) and tests (./tests/*).

## Outline of the workshop

Our mission is to help the Awesome Inc. to increase the availability of their new awesome recommendation service. For more information take a look on the [case study of Awesome Inc.](./case_study/case_study.html).

After some more introductory words, the trainers will guide you through the process. Along the way, various resilience patterns are explained and will be implemented hands-on:

* Bulkheads
* Complete Parameter Checking
* Timeout
* Retry
* Fallback
* Failover
* Circuit Breaker

## Prerequisites

* Java 1.8 or higher
* Apache Maven 3+
* Internet connection
* Your preferred IDE

## Getting The Tutorial Material

```
git clone https://github.com/ufried/resilience-tutorial.git
cd resilience-tutorial
```
