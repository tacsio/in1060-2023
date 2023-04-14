#!/bin/bash

wrk -t10 -c1000 -d30s http://localhost:8080/suggestion/9
