// installs board and pieces at the beginning of the game

let chessboard = undefined;
const letters = ['a','b','c','d','e','f','g','h'];
let turn = false;
let isBlack = false;

document.addEventListener("DOMContentLoaded",event=>{

    generateGrid();
    generatePieces();

    console.log(document.getElementById('pawn[1][W]'));
    console.log(board);

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
            square.setAttribute('occupied','space');

            // maybe make it here so they can't see the green dots

            square.style.left = k * squareSize + 'px';
            square.style.top = i * squareSize + 'px';

            chessboard.appendChild(square);
    
        }
    }
}

function generatePieces(serverBoard){


    generatePiece('pawn',1,'W','a5');
    generatePiece('pawn',1,'W','b1');


    /*
    const pieces = document.querySelectorAll('.piece');
    
    // for each piece, get the piece and then asign it to the required square
    pieces.forEach(piece =>{

        const curPos = piece.getAttribute('pos');
        const square = document.getElementById(curPos);

        if(square){

            const x = square.style.left - 7;
            const y = square.style.top - 7;

            piece.style.left = x;
            piece.style.top = y;

            square.setAttribute('occupied','piece');
            addDrag(piece);
        }else{
            return;
        }

    });

    */
}

function generatePiece(type,number,color,position){

    const square = document.getElementById(position);

    if(square){

        const piece = document.createElement('div');
        piece.className = 'piece';
        piece.id = type + '[' + number.toString() + ']'+'[' + color + ']';


        piece.style.left = 75;
        piece.style.top = square.style.top;
    
        // sets the piece of the board
        square.setAttribute('occupied','piece');

        square.appendChild(piece);

        console.log(piece.id);
    }

}

function addDrag(piece){

    piece.addEventListener('mousedown', (e) => {
        e.preventDefault();

        console.log(piece.style.left);
        console.log(piece.style.top);

        // stores the coordinates of 
        let shiftX = e.clientX
        let shiftY = e.clientY

        function moveAt(pageX,pageY) {

            piece.style.left = pageX - shiftX + 'px';
            piece.style.top = pageY - shiftY + 'px';
        }

        function onMouseMove(e){
            moveAt(e.pageX, e.pageY);
        }

        document.addEventListener('mousemove', onMouseMove);

        piece.addEventListener('mouseup',() => {

            console.log(piece.style.left);
            console.log(piece.style.top);

            document.removeEventListener('mousemove',onMouseMove);
            piece.onmouseup = null;
        });
    });
}


function move(){

}

function addPixel(orignal,addValue){
    let newPixel = parseInt(orignal) + addValue;
    return newPixel + 'px';
}