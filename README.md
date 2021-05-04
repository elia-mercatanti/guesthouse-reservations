# Guesthouse Reservations
[![Java CI with Gradle in Windows](https://github.com/elia-mercatanti/guesthouse-reservations/actions/workflows/gradle-windows.yml/badge.svg)](https://github.com/elia-mercatanti/guesthouse-reservations/actions/workflows/gradle-windows.yml)
[![Java CI with Gradle in Linux On Pull Request](https://github.com/elia-mercatanti/guesthouse-reservations/actions/workflows/gradle-linux-pr.yml/badge.svg)](https://github.com/elia-mercatanti/guesthouse-reservations/actions/workflows/gradle-linux-pr.yml)
[![Coverage Status](https://coveralls.io/repos/github/elia-mercatanti/guesthouse-reservations/badge.svg?branch=main)](https://coveralls.io/github/elia-mercatanti/guesthouse-reservations?branch=main)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=elia-mercatanti_guesthouse-reservations&metric=alert_status)](https://sonarcloud.io/dashboard?id=elia-mercatanti_guesthouse-reservations)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=elia-mercatanti_guesthouse-reservations&metric=bugs)](https://sonarcloud.io/dashboard?id=elia-mercatanti_guesthouse-reservations)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=elia-mercatanti_guesthouse-reservations&metric=code_smells)](https://sonarcloud.io/dashboard?id=elia-mercatanti_guesthouse-reservations)

Project for Advanced Techniques and Tools for Software Development (ATTSD) Course.

Simple app to manage reservations of a guesthouse.

## How to Build the Project

1. Clone the Repository.
```sh
git clone https://github.com/elia-mercatanti/guesthouse-reservations
```

2. Set Project Main Directory.
```sh
cd guesthouse-reservations
```

3. Build the Project - Compile code, Unit, Integration and End To End tests and Fatjar
```sh
./gradlew build
```
or With Also JaCoCo and Pitest Report
```sh
./gradlew build jacocoTestReport pitest
```

# Author
- Elia Mercatanti
