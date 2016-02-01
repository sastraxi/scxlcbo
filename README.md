[![Build Status](https://travis-ci.org/sastraxi/scxlcbo.svg?branch=master)](https://travis-ci.org/sastraxi/scxlcbo)

I assume anyone wanting to build/run this project has a working JDK 8 development environment.

* clone this repostiory
* import pom.xml as Maven Project into IntelliJ/Eclipse
* optionally change DB configuration (by default, points to a RethinkDB instance on AWS)
* run with entry point com.sastraxi.scxlcbo.Main
* access http://localhost:4567/

I used this project as an opportunity to explore different libraries, while staying in the Java ecosystem where I'm most comfortable.

Notes on my solution:

* I did not provide a mobile-ready layout for this webapp due to its limited use case; if Michelle is looking to have
  a beer after work on Friday, she is by definition at work when this desire strikes. Therefore, it's enough that
  she is able to interact with the site on her work computer.
* I assume the viewer's browser window is at least 940 pixels wide.
* The LCBO API used has some Sake products in the beer category; I've tried to filter these out.
* By design, the database and API services know nothing about each other.
* I have optimized the app in (slight) favour of a database that is quicker to respond than the API.

I had never used Spark, Freemarker, Unirest, or RethinkDB before. Some thoughts:

* Spark has no surprises so far. It's good at what it does.
* Similarly, Unirest is perfect for a simple API client.
* Freemarker (the templating engine used) needs to be updated for Java 8. In particular, it does not have the support
  for the Java 8 date primitives (Zoned/OffsetDateTime) that I wanted to use. As a result, I have to pass in a
  date/time formatter and bypass the template engine's date/time support.
* RethinkDB does not have a very Java-like API at all; its design means we can't have statically-defined types
  and relies on the user to know the return type of each API call. These types aren't stated anywhere in the API,
  except perhaps implicitly.

Ultimately, I think Spark has a lot of advantages over something like Spring for a small app like this (built-in web server; no XML configuration). Unirest seems like the consummate simple HTTP library. I don't think I'd use RethinkDB or Freemarker again.

If I were to spend more time on this project, I would:

* further configure travis CI to build+deploy the app onto an EC2 instance
* add a star rating system to the selection history, allowing the user to build a taste profile
* add some basic level of authentication and provide a UI to wipe stats (currently the endpoint exists but is commented-out)
* add some transitions between the pages