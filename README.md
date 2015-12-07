# sungka-game
KCL | 5CCS2SEG | Project Crawl - Android version of the Filipino board game Sunka

## Project Requirements

You must implement an Android application that allows two players to use a single Android tablet as a Sunka board, with the following requirements (listed in order of importance!):

- The app must enable players to start a new game and display a Sunka board in starting configuration.
- Unless requirement 8 is implemented, the app must select a random player to make the first move at the start of the game.
- When a player is permitted to make a turn, the app must enable that player to select a small tray on his/her side and redistribute the shells according to the rules of Sunka automatically.
- The app must assign the next move to the correct player: i.e. the other player in the game, unless the player is entitled to an additional move or the other player is unable to make a move.
- The app must implement the game rule pertaining to capturing an opponent's shells correctly.
- The app must identify when the game is finished and identify the winner in that situation.
- The source code should come with a full complement of automated tests of all the testable features that have been implemented.
- The app should allow the players to execute their first turn simultaneously and recognise when each player has exhausted their moves.  If you implement this requirement, you must not implement requirement 2 and you must assign the second turn to the player who finished the first turn first.
- The app should visualise the redistribution of shells in a way that players can observe.
- The app should include suitable graphics to visualise the board and shells to mimic the actual game.
- The app should collect statistics on games played, such as the number of games won/lost of each player, the time taken to make a move, high scores, player rankings, etc.
- The app may include some animations and sounds, provide these enhance usability and user experience.  Be careful. Make sure that these do not get in the way of playing the game.
- The app may allow two players to play a game from their own Android devices.  This requires two Android devices to communicate with one another.  For this purpose, you could use the relatively basic Java Sockets API to set up interprocess communication as a starting point.  If you implement this feature, you still need to allow two players to play the game using a single screen/device.
- The app may be extended with an AI player.  The relatively basic minimax search algorithm used for turn based games is a good starting point.  Bear in mind, however, that your search tree will grow very rapidly as up to seven moves are possible per turn.  If you implement this feature, you still need to allow two human players to play the game.
- The app must not include a splash screen.
