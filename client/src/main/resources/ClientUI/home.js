import {send} from "./send.js";

document.addEventListener('DOMContentLoaded',(event) =>{
    
    const logout = document.getElementById('logoutPic');
    logout.addEventListener('click',() => {

        const authToken = sessionStorage.getItem('authToken');
        submitLogout(authToken);

    });
});


function submitLogout(authToken){

    const method = 'DELETE';
    const endpoint = '/session'

    send(endpoint,undefined,method,authToken)
    .then(response => {

        // come back to, for right now I am storing into the session
        sessionStorage.setItem('authToken', '');

        window.location.href = 'login.html';

    })
    .catch(error => {
        errorPage();
    });
}

function errorPage(){
    window.location.href = 'error.html';
}
