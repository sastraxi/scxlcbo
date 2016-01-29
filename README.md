I used this project as an opportunity to explore different libraries, while staying in the Java ecosystem where I'm most comfortable.

Notes on my solution:

* I did not provide a mobile-ready layout for this webapp due to its limited use case; if Michelle is looking to have
  a beer after work on Friday, she is by definition at work when this desire strikes. Therefore, it's enough that
  she is able to interact with the site on her work computer.
* I assume the viewer's browser window is at least 1000 pixels wide.
* The LCBO APU used has some Sake products in the beer category; I've filtered these out.
* By design, the database and API services know nothing about each other.
* I have optimized the app in (slight) favour of a database that is quicker to respond than the API.

I had never used Spark, Freemarker, Unirest, or RethinkDB before. Some thoughts:

* Spark has no surprises so far. It's good at what it does.
* Similarly, Unirest is perfect for a simple API client.
* Freemarker (the templating engine used) needs to be updated for Java 8. In particular, it does not have the support
  for the Java 8 date primitive (OffsetDateTime) that I wanted to use. As a result, I'm converting to the deprecated
  DateTime type before passing datetimes to the view.
  local time zone so that Freemarker
* RethinkDB does not have a very Java-like API at all; its design means we can't have statically-defined types
  and relies on the user to know the return type of each API call. These types aren't stated anywhere in the API,
  except perhaps implicitly.

Ultimately, I think Spark has a lot of advantages over something like Spring for a small app like this (built-in web server; no XML configuration). Unirest seems like the consummate simple HTTP library. I don't think I'd use RethinkDB or Freemarker again.