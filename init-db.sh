#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE etudiants_db;
    CREATE DATABASE grading_db;
EOSQL