{
  "mapType": "SquareMap" or "HexMap" are valid. *Add others as we go*
  "totalCols": Total number of cols in the map grid (including null ones)
  "totalRows": Total number of rows in the map grid (including null ones)
  "maxPlayers": Maximum number of players able to play the map **SHOULD MAYBE BE DESCIDED IN GAME CODE ONLY**
  "randomPlayers": If true, player field in zones are disregarded **SHOULD MAYBE BE MOVED TO GAME CODE**
  "offset": Only relevant for HexMaps, sets the start index for 2D array of axial hex coords
  "zones": [
    "player1": [ This text could say whatever, but it needs to be here. If randomPlayers is true, the objects are combined
        [       Coordinate values. Each array corresponds to a row, and each number to a col.
            9   Every zone that should not be null, must be specified with a col number.
        ],
        [
            9,
            10
        ],
    "player2": [
        [],      IMPORTANT: Empty rows must still be included.
        [
            8
        ],
        [
            7,
            8
        ],
  ]
}

HexMap coordinate values are interpreted as axial
Ref: https://www.redblobgames.com/grids/hexagons/#map-storage and 2D-arrays
Follow that guide for writing coordinates
