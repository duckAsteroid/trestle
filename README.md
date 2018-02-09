trestle
===========
A mocking framework for REST APIs

![Build status](https://travis-ci.org/duckAsteroid/trestle.svg?branch=master)

Getting Started
---------------

Simulates/mocks a REST API using a series of resources on the filesystem.

A simple server that provides JUnit tests with a REST API to call

Spins up a server to handle the gets etc.

Uses a flat directory of JSON files to provide "resources".

Use special (redacted from served content) JSON to control AUTH and METHODs (in each file or in each dir)

When directory includes special "LIST" signifier then API provides enumeration of files in either ID mode (*.json) or in content mode.

IDs are expected to be those of docs.

```text
petstore/pets/{special}.json - indicates list by ID
petstore/pets.json
petstore/pets/dave.json
```

Download
-----------
This project is available via Maven repo at: https://dl.bintray.com/duck-asteroid/maven/

