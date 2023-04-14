#!/bin/bash

wrk -t4 -c400 -d30s http://localhost:8080/suggestion/1
