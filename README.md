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
domains/{special}.json - indicates list by ID
domains/partonomy.json
domains/partonomy/ontology.json
domains/partonomy/vocabs/{special}.json - list by ID
domains/partonomy/vocabs/svr.json
domains/partonomy/vocabs/svr/{special}.json - list by ID
domains/partonomy/vocabs/svr/A1234.json
domains/partonomy/vocabs/svr/B2345.json
domains/partonomy/vocabs/pil.json
domains/other.json
```

Download
-----------
This project is available via Maven repo at: https://dl.bintray.com/duck-asteroid/maven/

