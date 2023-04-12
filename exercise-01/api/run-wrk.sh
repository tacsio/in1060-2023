#!/bin/bash

wrk -t4 -c50 -d30s http://localhost:8080/suggestion/9
