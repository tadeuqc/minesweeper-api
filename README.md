# MineSweeperAPI

This is a implementation of the famous game Minesweeper. The mainfocus is to provide a RESTFul API with a complete set 
of features that can be implemented across many clients like Mobile, Web, and even Desktop.

### OnlineVersion

https://ancient-atoll-38572.herokuapp.com/

### Main Features

###POST: /game
This endpoint creates a new game
#####Payload
`{"columns":9,"rows":9,"mines":10,"user":"test"}`

###GET: /game/:id
This endpoint gets a current RUNNING game

###GET: /game/all/:user
Gets all RUNNING games from a specific player

###DELETE: /game/:id
This endpoint deletes a game from the database

###PATCH: /game/:id/:action/:x/:y
This endpoint makes a move on a game, possible actions are: PLAY, FLAG



