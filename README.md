# GabGuy v1.01
Created by Ben Carlisle

GabGuy is a simple chatbot designed to be entertaining for the user. For information on features, visit the help documentation located under src/files/help.md.

GabGuy was created in Eclipse Java and can be easily imported. Created a jar from the GabGuy Eclipse project is simple by right clicking on the filled createJar.jardesc and clicking create JAR.

Any questions can be sent to my personal email at bencarlisleofficial@gmail.com.

Happy Gabbing!

## Building

```
docker compose build
```

## Running the Gui

```
./setup.sh;
docker compose up gabguy-gui;
```

This will bring up a Java swing GUI

## Running the Cli

```
docker compose run gabguy-cli
```

Commands can be entered and into stdin and read from stdout. Not that `docker compose up` does **not** work, it must be `docker compose run` or `docker run -it`.

# Todo

1. Fix web integration
2. Fix threading
3. Fix State handling
4. Overall code cleanup