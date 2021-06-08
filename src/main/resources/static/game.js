const API_URL = 'https://ancient-atoll-38572.herokuapp.com/'

let gamesList

let newGameButton
let resumeGameButton
let newGameModal
let resumeGameModal
let container
let menu
let currentGame
let bombRange
let playerInput
let gameIDContainer

//Game Config
let currentPlayer
let gameSize
let bombs
let cellSize = 40
let gameStatus

const logoCoordinates = [{x: 1, y: 2},    {x: 2, y: 2},    {x: 3, y: 2},    {x: 4, y: 2},    {x: 5, y: 2},    {x: 1, y: 3},
    {x: 3, y: 3},    {x: 5, y: 3},    {x: 2, y: 4},    {x: 4, y: 4},    {x: 2, y: 6},    {x: 3, y: 6},    {x: 4, y: 6},    {x: 1, y: 7},
    {x: 5, y: 7},    {x: 2, y: 8},    {x: 3, y: 8},    {x: 4, y: 8},    {x: 2, y: 10},    {x: 3, y: 10},    {x: 4, y: 10},    {x: 1, y: 11},
    {x: 5, y: 11},    {x: 2, y: 12},    {x: 3, y: 12},    {x: 4, y: 12},    {x: 2, y: 14},    {x: 3, y: 14},    {x: 4, y: 14},    {x: 5, y: 14},
    {x: 1, y: 15},    {x: 2, y: 16},    {x: 3, y: 16},    {x: 1, y: 17},    {x: 2, y: 18},    {x: 3, y: 18},    {x: 4, y: 18},    {x: 5, y: 18},
]
function openModal(modal) {
    modal.classList.add("show-modal")
}

function closeModal(modal) {
    modal.classList.remove("show-modal")
}

function fetchGames(user) {

    fetch(API_URL+user).then(function (response) {
        if (response.ok) {
            gamesList = response.json();
        }
    })

}

function createNewGame(size, mines, user) {
    fetch(API_URL + `game`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({"columns":size,"rows":size,"mines":mines,"user":user})
    }).then(function (response) {
        // The API call was successful!
        if (response.ok) {
            return response.json()
        } else {
            return Promise.reject(response);
        }
    }).then(function (data) {
        currentGame = data.id
        gameStatus = data.status
        currentPlayer = data.userName
        drawBoard(data);
    }).catch(function (err) {
        // There was an error
        console.warn('Something went wrong.', err);
    });
}

function resumePreviousGame(id) {
    fetch(API_URL + `game/${id}`).then(function (response) {
        if (response.ok) {
            return response.json()
        } else {
            return Promise.reject(response);
        }
    }).then(function (data) {
        currentGame = data.id
        gameStatus = data.status
        currentPlayer = data.userName
        drawBoard(data);
    }).catch(function (err) {
        // There was an error
        console.warn('Something went wrong.', err);
    });
}

function drawLogoBoard() {
    const newBoard = document.createElement('div')
    newBoard.classList.add('board')

    const logoHeight = 280
    const logoWidth = 840

    newBoard.style.height = logoHeight + 'px'
    newBoard.style.width = logoWidth + 'px'

    for(let x = 0; x < (logoHeight/cellSize); x++) {
        for(let y = 0; y < (logoWidth/cellSize); y++) {
            const cell = document.createElement('div')
            if(!logoCoordinates.find(cell => cell.y === y && cell.x === x)) {
                cell.classList.add('CLOSE')
            }
            else {
                cell.classList.add('OPEN')
                cell.innerHTML = '💣'
            }

            cell.style.height = cellSize + 'px'
            cell.style.width = cellSize + 'px'
            newBoard.appendChild(cell)
        }
    }
    const oldBoard = document.querySelector('.board')
    try {container.removeChild(oldBoard)} catch {}
    container.insertBefore(newBoard,menu)
}

function drawBoard(response) {
    const newBoard = document.createElement('div')
    newBoard.classList.add('board')

    newBoard.style.height = (response.rows * cellSize) + 'px'
    newBoard.style.width = (response.columns * cellSize) + 'px'

    for(let x = 0; x < response.rows; x++) {
        for(let y = 0; y < response.columns; y++) {
            const cellStatus = response.cells.find( cell => cell.x === x && cell.y === y)
            const cell = document.createElement('div')
            cell.setAttribute('x', x.toString())
            cell.setAttribute('y', y.toString())
            cell.style.height = cellSize + 'px'
            cell.style.width = cellSize + 'px'
            cell.classList.add(cellStatus.status)
            if(cellStatus.adjacentMine > 0) {
                cell.innerHTML = cellStatus.adjacentMine
                if (cellStatus.adjacentMine == 1) cell.classList.add('one')
                if (cellStatus.adjacentMine == 2) cell.classList.add('two')
                if (cellStatus.adjacentMine == 3) cell.classList.add('three')
                if (cellStatus.adjacentMine == 4) cell.classList.add('four')
            }
            if(cellStatus.status === 'HAS_MINE') {
                cell.innerHTML = '💣'
            }
            cell.addEventListener('click', function(e) {
                play(cell, 'PLAY')
            })
            newBoard.appendChild(cell)
        }
    }
    const oldBoard = document.querySelector('.board')
    try {container.removeChild(oldBoard)} catch {}
    container.insertBefore(newBoard,menu)
    gameIDContainer.innerHTML = `Current Game ID: ${currentGame} - Player Name: ${currentPlayer}`
}

function play(cell, action) {
    if(gameStatus === 'LOOSE' || gameStatus === 'WON') {
        return;
    }
    const x = cell.getAttribute('x')
    const y = cell.getAttribute('y')

    fetch(API_URL + `game/${currentGame}/${action}/${x}/${y}`, {
        method: 'PATCH'
    }).then(function (response) {
        if (response.ok) {
            return response.json()
        } else {
            return Promise.reject(response);
        }
    }).then(function (data) {
        console.log(data)
        gameStatus = data.status
        if (gameStatus !== 'RUNNING') {
            alert(`You ${gameStatus.toLowerCase()}!`)
        }
        drawBoard(data);
    }).catch(function (err) {
        // There was an error
        console.warn('Something went wrong.', err);
    });
}

document.addEventListener('DOMContentLoaded', () => {
    container = document.querySelector('.container')
    menu = document.querySelector('.menu')
    newGameButton = document.querySelector('#newGame')
    resumeGameButton = document.querySelector('#resumeGame')
    newGameModal = document.querySelector('#newGameModal')
    bombRange = document.querySelector('#bomb-range')
    playerInput = document.querySelector('#playerName')
    gameIDContainer = document.querySelector('.gameID')
    resumeGameModal = document.querySelector('#resumeGameModal')
    const newgameOkButton = document.querySelector('#new-ok')
    const resumeOkButton = document.querySelector('#resume-ok')

    //Handle Modals
    window.addEventListener("click", e => {
        if (e.target === newGameModal) closeModal(newGameModal)
    });

    //Handle NewGameModal Controls
    const sizeOptions = document.querySelectorAll('input[name="size"]');
    sizeOptions.forEach(radio => {
        radio.addEventListener('click',  () => {
            gameSize = radio.value;
            bombRange.setAttribute('max',gameSize * 3)
            bombRange.value = 1
            bombs = 1
            bombLabel.innerHTML = 'Bombs: ' + bombs
        });
    });

    const bombLabel = document.querySelector('#bomb-lbl')
    bombRange.addEventListener('change', e => {
        bombRange.setAttribute('value', e.target.value)
        bombs = e.target.value
        bombLabel.innerHTML = 'Bombs: ' + bombs
    })

    newGameButton.addEventListener('click', () => {
        openModal(newGameModal)
    });

    resumeGameButton.addEventListener('click', () => {
        openModal(resumeGameModal)
    });

    newgameOkButton.addEventListener('click', () => {

        createNewGame(gameSize,bombs,playerInput.value)
        closeModal(newGameModal)
    });

    resumeOkButton.addEventListener('click', () => {
        const gameIdInput = document.querySelector('#gameId')
        resumePreviousGame(gameIdInput.value)
        closeModal(resumeGameModal)
    });
    [...document.querySelectorAll('.cancel')].forEach( item => {
        item.addEventListener('click', () => {
            closeModal(item.closest('.show-modal'))
        })
    })

    //Draw Logo
    drawLogoBoard()
})
