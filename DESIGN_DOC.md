# Design Doc for Sports Analysis App

## Overview

### What exactly does this app do?

Competitive sports are won as much off the field as on it. The decisions made before and during a match, who starts, how the lineup is structured, how to counter the opposition's strengths and can be just as decisive as anything that happens in play. But making those decisions well requires processing a large amount of information across players, situations, and conditions. Doing that manually is slow, inconsistent, and easy to get wrong under pressure.
That's the problem this app is built to solve.
At its core, the app helps coaches and team management make smarter, faster strategic decisions by bringing together player performance data, physical condition, and mental state into one place and making sense of it in a way that's actually useful during the crunch of match preparation.
Take a simple example: you know a specific opposing player tends to dominate in a particular situation or style of play. Instead of going through records and relying on gut feel, the app tells you exactly which of your players has the best counter-record in that scenario, and recommends adjusting your lineup or strategy accordingly. That same logic scales across dozens of match situations and sport types and it's just not humanly practical to compute all of it in time without a tool like this.
What makes the analysis meaningful, though, is that it doesn't stop at statistics. A player's past numbers only tell part of the story. Their current fitness, recent physical workload, and mental form matter just as much and those are exactly the kinds of variables that tend to get overlooked when decisions are being made quickly. The app brings all of that together so nothing important falls through the cracks.
The goal isn't to replace the coach's instinct. It's to give that instinct better information to work with across any sport, any team, any match situation.

## Technologies used

For Backend Java and Spring Boot are used.

As for why we have used Java

Pros

1. It gives good structure to our code by default so we don't have to create it from scratch.
2. Support multithreading.
3. And overall has a good community support.
4. Also is a great language to build to make to develop an intuition about object oriented and SOLID methodologies.

Cons

1. A lot of boilerplate code has to be written.
2. Can become a little complicated to understand.

Databases Used

1. Postgres SQL
   1. Specifically for structured data like user, orgs, player and player contract records.
2. Mongo DB
   1. Specifically selected due to it document structure to use it as an append only log for stats.

## Database Schema

In this app the base focus is oon the analytics of individual player and in the combination of team
