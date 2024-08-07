import {send} from "./send.js";

// this is just the list of games that could be used
const gamesMap = new Map();
let joinButtonListener = null;
renderTable();

document.addEventListener('DOMContentLoaded',(event) =>{
    
    const logout = document.getElementById('logoutPic');
    const search = document.getElementById('searchPic');

    logout.addEventListener('click',() => {
        const authToken = sessionStorage.getItem('authToken');
        submitLogout(authToken);
    });

    search.addEventListener('click',() => {

        const search = document.getElementById('searchInput').value;
        let map = searchMap(search);

        if(map){
            renderTable(map);
        }
    });

});

document.getElementById('create').addEventListener('click', event => {
    event.preventDefault();
    document.getElementById('createPop').style.display = 'block';
});

document.getElementById('submitGame').addEventListener('click', event => {
    const newGameData = document.querySelector('.gameForm');
    event.preventDefault();

    // gets the game data and then creates game in the server
    const data = new FormData(newGameData);
    const gameData = (Object.fromEntries(data));

    const authToken = sessionStorage.getItem('authToken');
    
    if(gameData.gameName){
        submitCreate(authToken,gameData);
    }
});

document.getElementById('refresh').addEventListener('click', async event => {
    await renderTable();
});

// basic function that allows me to join a game
function submitJoin(authToken,joinInfo){ // we are going to need to also get a player color

    const method = 'PUT'
    const endpoint = '/game'
    const request = JSON.stringify(joinInfo);

    send(endpoint,request,method,authToken)
    .then(response => {

        // takes you to the game screen
        window.location.href = 'game.html';

    })
    .catch(error => {

        console.log(error);
    });
}

function submitCreate(authToken,gameName){

    const method = 'POST'
    const endpoint = '/game'
    const request = JSON.stringify(gameName);

    send(endpoint,request,method,authToken)
    .then(async (response) => {

        let game = response;
        document.getElementById('createPop').style.display = 'none';

        await renderTable()

        optionsJoin(authToken,game.gameID,true,true,false);

    })
    .catch((error) => {
        console.log(error);
        //errorPage();
    });
}

function submitLogout(authToken){

    const method = 'DELETE';
    const endpoint = '/session'

    send(endpoint,undefined,method,authToken)
    .then(response => {

        // come back to, for right now I am storing into the session
        
        sessionStorage.setItem('authToken', '');
        window.location.href = 'index.html';

    })
    .catch(error => {
        console.log(error);
        //errorPage();
    });
}

// async means that it needs to wait until it is done
// returns a list of games that the player can join
async function submitList(authToken){
    const method = 'GET';
    const endpoint = '/game';

    try{
        let response = await send(endpoint, undefined, method, authToken);
        return response;
    }catch(error){
        console.log(error);
        //errorPage();
    }
}

function errorPage(){
    window.location.href = 'error.html';
}


// funcitons that handle the table -------------------------------------------------------------------------------------------

function displayGame(gameId){

    let game = gamesMap.get(gameId);

    // if there is nothing there
    if(game == undefined){
        return;
    }

    document.getElementById('name').textContent = 'Game Name: ' + game.gameName;
    document.getElementById('id').textContent = 'Game Id: ' + game.gameID;

    let whiteUsername = game.whiteUsername;
    let blackUsername = game.blackUsername;

    // simple checks to see if anyone is in the game

    if(whiteUsername){
        document.getElementById('whitePlayer').textContent = 'White Player: ' + whiteUsername;
    }else{
        document.getElementById('whitePlayer').textContent = 'White Player: None';
    }
    if(blackUsername){
        document.getElementById('blackPlayer').textContent = 'Black Player: ' + blackUsername;
    }else{
        document.getElementById('blackPlayer').textContent = 'Black Player: None';
    }
}

function searchMap(value){
    const searchMap = new Map();

    if(/^\s*$/.test(value)){ // is the value just whitespace
        return false;
    }

    if(/^\d+$/.test(value)){ // search for an ID
        for(let [id,game] of gamesMap){
            if(id.toString().includes(value)){
                searchMap.set(id,game);
            }
        }
    }else{ // search for a string
        for(let [id,game] of gamesMap){
            if(game.gameName.includes(value)){
                searchMap.set(id,game);
            }
        }
    }
    return searchMap;
}

async function renderTable(Map){
    let authToken = sessionStorage.getItem('authToken');
    
    return new Promise(async(resolve,reject) =>{
        let displayMap;
            try{

                let list = await submitList(authToken);
                renderMap(list); // should fill the map

                if(Map){
                    displayMap = Map;
                }else{
                    displayMap = gamesMap;
                }

                const tableBody = document.getElementById('tableBody');
                tableBody.innerHTML = '';

                let counter = 0;
        
                // the first couple games of a given map
                for(let [id,game] of displayMap){

                    if(counter > 7){
                        break; // breaks the loop if there are too many games
                    }
            
                    const row = document.createElement('tr');
                    row.innerHTML = `
            
                        <td data-label="Game Name">${game.gameName}</td>
                        <td data-label="Game ID">${id}</td>
                        <td data-label=" ">
                            <button class="joinButton" id="button-${id}">
                                <img src="pictures/goto.png" style="height:20px">
                            </button>
                        </td>
            
                    `;
                    tableBody.appendChild(row);

                    // adds event handlers for all of those buttons
                    document.getElementById(`button-${id}`).addEventListener('click',async(event) =>{
                        displayGame(id);
                        selectJoin(id)
                        .then(response =>{
                            optionsJoin(authToken,id,!game.whiteTaken,!game.blackTaken,true);
                        });
                    });
                    counter++;
                }
            resolve();
        }catch(error){
            reject(error);
        }
    });
}

async function selectJoin(gameId){
    const joinButton = document.getElementById('join');

    return new Promise(async(resolve,reject) => {
        if(joinButtonListener){
            joinButton.removeEventListener('click',joinButtonListener);
        }
        joinButtonListener = function(event){
            resolve();
            return gameId;
        };
        joinButton.addEventListener('click',joinButtonListener);
    });
}

async function optionsJoin(authToken,gameID,isWhite,isBlack,isObserve){

    let game = gamesMap.get(gameID);
    
    if(game == undefined){ // just ends if it can't find the game
        console.error('ERROR: Unable to idenfity game');
        return;
    }

    // allows the user to see the menu they can now use
    let playerColor = await getColor(isWhite,isBlack,isObserve);

    const request = {gameID,playerColor};

    // should have all the data it needs to request the server
    submitJoin(authToken,request);

}

function getColor(isWhite,isBlack,isObserve){

    console.log(isWhite);
    console.log(isBlack);

    // if there are no spots left in the game, it automatically makes you a observer
    if(!isWhite && !isBlack && isObserve){
        return 'WATCH';
    }

    return new Promise((resolve) => {

        document.getElementById('joinPop').style.display = 'block';

        const whiteButton = document.getElementById('white');
        const blackButton = document.getElementById('black');
        const observeButton = document.getElementById('watch');

        function handleClick(color){
            whiteButton.style.display = 'none';
            blackButton.style.display = 'none';
            observeButton.style.display = 'none';
            document.getElementById('joinPop').style.display = 'none';
            resolve(color);
        }

        if(isWhite){
            whiteButton.style.display = 'block';
            whiteButton.addEventListener('click',function whiteClick(event){
                handleClick('WHITE');
                whiteButton.removeEventListener('click',whiteClick);
            });
        }
        if(isBlack){
            blackButton.style.display = 'block';
            blackButton.addEventListener('click',function blackClick(event){
                handleClick('BLACK');
                blackButton.removeEventListener('click',blackClick);
            });
        }
        if(isObserve){
            observeButton.style.display = 'block';
            observeButton.addEventListener('click',function observeClick(event){
                handleClick('WATCH');
                observeButton.removeEventListener('click',observeClick);
            });
        }
    })
}

function renderMap(list){

    for(let game of list.games){
        // basic logic
        if(gamesMap.has(game.gameID)){
            continue; //skip ones you already have done
        }else{
            gamesMap.set(game.gameID,game);
        }

        // add some code here that will clear finished games

    }
}