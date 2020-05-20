# SoPra FS20 Group 01 - TipTop Tournament

## Introduction
The goal of this project was to implement a working tournament management software where tasks which are hard to do manually, would be automated and conveniently done without any "manual labor".

More specifically is was the task of the server to store data about users, tournaments, games and so on.

## Technologies

-   Java
-   JPA
-   Hibernate
-   Springboot
-   PostgreSQL

## High-level components
The server is structured in a way that the requests are handled by the respective controller ([Manager](src/main/java/ch/uzh/ifi/seal/soprafs20/controller/ManagerController.java), [Tournament](src/main/java/ch/uzh/ifi/seal/soprafs20/controller/TournamentController.java), [Participant](src/main/java/ch/uzh/ifi/seal/soprafs20/controller/ParticipantController.java)). Those controllers will then perform the requested action or provide the requested information using the respective service ([Manager](src/main/java/ch/uzh/ifi/seal/soprafs20/service/ManagerService.java), [Tournament](src/main/java/ch/uzh/ifi/seal/soprafs20/service/TournamentService.java), [Participant](src/main/java/ch/uzh/ifi/seal/soprafs20/service/ParticipantService.java)).

Generally a controller would only call its service counterpart to update an entitiy (e.g. the tournament controller would call the tournament service to create a tournament entity). However, we had to compromise that principle since after a game has been updated (using the tournament service), the result of that game would also have to be entered in the participants statistics.

## Launch & Deployment

### Getting started with the application
Start with having a look at the entities with respect to their fields. Once you've got a feel for that, continue with the controllers and have a look what service methods they're calling. By doing that you should get an understanding how the server works.

### Running the project
To start the server locally, just run the [Application](src/main/java/ch/uzh/ifi/seal/soprafs20/Application.java) file and the server should be reachable under *http://localhost:8080/*.

### Running the test
That depends on what IDE you're using. If you happen to use IntelliJ just right-click on the [Test folder](src/test) and choose "Run tests...".

### External dependencies and databases
Gradle should take care of the dependencies and the external PostgreSQL database is only used in the production build.

### Releases
To deploy changes one simply needs to commit and push their changes and the application will automatically be deployed on [heroku](https://sopra-fs20-group-01-server.herokuapp.com/) (if all tests pass).

## Roadmap
-   One Feature we definitely would have liked to implement was the ability for the participant to be in multiple tournaments. At the moment, it is only possible to take part in one tournament at a time

-   We would like to develop a more elegant version of our tournament update algorithm (which is called when a game is finished and the new fixtures need to be calculated).

-   Another idea was to implement a more sophisticated security system to prevent just anyone to make requests.

-   We would have very much liked to work together with the Swiss Table Tennis Association and their data to display interesting statistics. Our idea was to diplay intersting facts based on the tournament happening right now such as "Player XY has the same winning percentage as Roger Federer has had in his career so far". This proved to be out of scope rather quickly.

## Acknowledgment
Developers:
-   Stefano Anzolut
-   Tony Ly
-   Mauro Hirt
-   Vukasin Arandjelovic
-   Fabio Sisi

We would like to extend our thanks to anyone who has supported us through this challenging but experience-filled project. Also, we'd like to especially mention our TA Alex Scheitlin whose advice and guidance was very valuable to us.

## License DRAFT
Copyright (c) 2020 Fabio Sisi

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.