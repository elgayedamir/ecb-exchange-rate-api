# Requirements

## Overview
The goal is to build an "Exchange Rate Service" that exposes a REST API for currencies exchange rates / conversion.

## User Stories
As a user, who accesses this service through a user interface, ...
1. I want to retrieve the ECB reference rate for a currency pair, e.g. USD/EUR or
HUF/EUR.
1. I want to retrieve an exchange rate for other pairs, e.g. HUF/USD.
1. I want to retrieve a list of supported currencies and see how many times they were
requested.
1. I want to convert an amount in a given currency to another, e.g. 15 EUR = ??? GBP
1. I want to retrieve a link to a public website showing an interactive chart for a given
currency pair.

## Constraints
The following constraints should be respected
* Use the reference exchange rates published by the European Central Bank on a daily basis.
[ECB exchange rates reference](https://www.ecb.europa.eu/stats/policy_and_exchange_rates/euro_reference_exchange_rates/html/index.en.html)
* The ECB uses EUR as the base currency. You can calculate non-EUR rates from the
published data set.