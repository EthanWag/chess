// installs board and pieces at the beginning of the game

let chessboard = undefined;
const letters = ['a','b','c','d','e','f','g','h'];
let turn = false;
let isBlack = false;

document.addEventListener("DOMContentLoaded",event=>{

    generateGrid();
    generatePieces();

});

// generates the grid that the player is going to use
function generateGrid(){
    chessboard = document.getElementById('board');
    const squareSize = 75;
    
    for(let i = 0; i < 8; i++){
        for(let k = 0; k < 8; k++){
    
            const square = document.createElement('div');
            square.id = letters[i] + (k + 1);
            square.className = 'square';

            square.addEventListener('drop',dragDrop);
            square.addEventListener('dragover',dragOver);

            // maybe make it here so they can't see the green dots

            square.style.left = k * squareSize + 'px';
            square.style.top = i * squareSize + 'px';

            chessboard.appendChild(square);
    
        }
    }
}

function generatePieces(chessBoard){

    generatePiece('pawn',1,'W','c4');
    generatePiece('pawn',1,'W','h2');

    console.log(document.querySelectorAll('#board .piece'));

}

function generatePiece(type,number,color,position){

    const square = document.getElementById(position);

    if(square){

        const piece = document.createElement('div');
        piece.setAttribute('draggable',true);
        piece.className = 'piece';
        piece.id = type + '[' + number.toString() + ']'+'[' + color + ']';
    
        piece.addEventListener('dragstart',dragStart);

        square.appendChild(piece);
    }

}

// moving pieces around the board

let curPiece = undefined;
let lastSquare = undefined;

function dragStart(e){
    e.stopPropagation();
    curPiece = e.target;
    lastSquare = e.target.parentNode;

}

function dragOver(e){
    e.preventDefault();
}

function dragDrop(e){
    const dropSquare = e.target.parentNode;

    // no change
    if(lastSquare.id === dropSquare.id){return;}


    // right here send a websocket


    e.target.appendChild(curPiece);
}
