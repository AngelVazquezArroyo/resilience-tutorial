Exercise 1 - Bulkheads
=======================

Introduction
------------

This exercise addresses the most basic - and most important - resilience pattern: _bulkhead_ (also known as _failure unit_ or _unit of mitigation_). Bulkheads are the most important resilience pattern as almost every other resilience pattern builds on top of bulkheads.

The idea of bulkheads is straightforward: A system must not fail as a whole. Therefore, split the system in several units (the bulkheads) and isolate the units against each other in a way that a failure of one unit is not propagated to other units (avoiding so-called _cascading failures_).

Yet, defining bulkheads is a pure design issue. There are not any frameworks or libraries that support you in creating adequate bulkheads. It is understanding the (business) domain, a lot of reasoning, usually supplemented by pen and paper and trying to come up with a reasonable design that provides the desired properties for the building blocks, i.e., the bulkheads:

* High cohesion, low coupling
* Separation of concerns
* etc.

These properties are desirable because they minimize the required cross-bulkhead communication. Minimized communication between bulkheads is crucial because the fewer communication happens between the bulkheads the less likely are cascading failures that need to be handled.

The problem is that almost everybody knows _what_ the desired design properties are. Yet, hardly anybody really knows _how_ to create a design that provides those properties.

As this is an extremely important topic, we touch it at least briefly in this exercise. We will not be able to cover and solve it in depth (actually, IT as a whole was not able to solve this problem in a satisfying way within the last 40+ years). Still, it is valuable to develop a feeling for the design challenges that are the basis for a robust software design.

Exercise
--------

* Read the [case study](case_study.adoc).
* Design the system landscape for Awesome, Inc. (the company in the case study). Try to split the system landscape in services that can serve a bulkheads.
* The design is done best in a small team (up to 4 persons). Yet, it is also perfectly fine to do it alone if that is the way you create designs best.
* The design should only show the services and their communication paths. Also try to reason about the type of communication to be used between the different services (request/response, messages, events).
* A piece of paper and a pen is sufficient. Alternatively, you can also use a drawing tool of your choice (UML can be used, but is not required).
