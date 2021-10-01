# Lights Out

A simple microservice based implementation of the Lights Out game. The backend uses Javalin with a Jetty service. The frontend client is a desktop app using JavaFX. This project is designed to provide a working example for my students in CS 3321 - Introduction to Software Engineering here at Idaho State University.

## Table of Contents

- [Installation](#instalation)
- [Usage](#usage)
  * [Server](#server)
  * [Client](#client)
- [Contributing](#contributing)
- [Credits](#credits)
- [License](#license)

## Installation

This project is composed of three modules: i) the `library` module contains some basic shared code for the `client` and the `server`. So no installation will be needed as it will be provided to both. ii) The server can be installed by simply running the following command:

```bash
$ gradle :server:assemble
```

This will produce the files `server/build/distributions/lo-server.tar` and `server/build/distributions/lo-server.zip`. You can then simply copy and unpack these wherever you wish the server to be installed.

iii) The client is very similar to the server. To produce the installable program, you simply execute the following command:


```bash
$ gradle :client:assemble
```

This will produce the files `client/build/distributions/lo-client.tar` and `client/build/distributions/lo-client.zip`. You can then simply copy and unpack these wherever you wish the client to be installed.

For both the client and server, you can also add the appropriate paths (plus "/bin") to your `PATH` environment variable so that the appropriate commands may be directly executed from the command line.

## Usage

### Server

The server can be started in one of three ways (depending on your installation)

1. If you did not follow the installation instructions, but you have cloned this repo, then you can either:

   - Run the following gradle command (if you have gradle installed):

     ```bash
     $ gradle :server:run
     ```

   - If you do not have gradle installed, use the following command:

     ```bash
     $ gradlew :server:run
     ```

2. If you did install the program, but did not alter your `PATH` environment variable then the following command will be needed:

   ```bash
   $ cd <directory you installed it to>
   $ ./bin/lo-server
   ```

3. Finally, if you did the installation and updated your `PATH` variable, then all that is needed is to execute the following command:

   ```bash
   $ lo-server
   ```
   
Once the server is up and running, you should see something like the following:


Once the server is running, it can be stopped by simply killing it using Ctrl-C or the like. But, keep the server running and turn your attention to the Client.

### Client

The client can be started in one of three ways (depending on your installation)

1. If you did not follow the installation instructions, but you have cloned this repo, then you can either:

   - Run the following gradle command (if you have gradle installed):

     ```bash
     $ gradle :client:run
     ```

   - If you do not have gradle installed, use the following command:

     ```bash
     $ gradlew :client:run
     ```

2. If you did install the program, but did not alter your `PATH` environment variable then the following command will be needed:

   ```bash
   $ cd <directory you installed it to>
   $ ./bin/lo-client
   ```

3. Finally, if you did the installation and updated your `PATH` variable, then all that is needed is to execute the following command:

   ```bash
   $ lo-client
   ```
   
Once running, the client should open a window similar to the following:


Here you simply need to put in the address (host name) for the server (if running on the same machine as the client, localhost will do), and then enter the port number (7000). Once you have entered that information, go ahead and press the "Connect" button (note: if you made a mistake go ahead and press the reset button to clear the form and try again). When successful, you should see the game board displayed. This will look something like the following screenshot:

On the gameboard, you have four options:

1. You can play the game by clicking on the tiles in the board.
2. You can disconnect from the server and try another server by pressing the "disconnect" button.
3. You can reset the board to another randomly generated game by pressing the "reset" button
4. You can exit the game by pressing the "exit" button.

## Contributing

Currently, I am not seeking any new contributions to the program.

## Credits

- [Isaac D. Griffith](https://github.com/grifisaa) - Author, Developer, and Professor.

## License

Currently this project is copyright 2021 Isaac D. Griffith under the MIT License. (see the LICENSE file)
