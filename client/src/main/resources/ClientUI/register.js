import {send} from './send.js';

document.getElementById('newUserH1').addEventListener('click', event => {        
    event.preventDefault();

    if(checkPasswords()){

        const username = document.getElementById('newUsername').value;
        const password = document.getElementById('newPassword').value;
        const email = document.getElementById('newEmail').value;

        clear();

        const apiData = {username,password,email};
        submitRegister(apiData);

    }else{
        clear();
        printError('Passwords do not match');
    }
});

function submitRegister(data){

    const method = 'POST';
    const endpoint = '/user'
    const request = JSON.stringify(data);

    send(endpoint,request,method,undefined)
    .then(response => {
    
        document.getElementById('failUser').textContent = '\n';

        // come back to, for right now I am storing into the session
        sessionStorage.setItem('authToken', response.authToken);
  
        window.location.href = 'home.html';
  
    })
      .catch(error => {
        if(error == 'Error: _403_'){
          document.getElementById('failUser').textContent = 'Username Already Taken';
        }else if(error == 'Error: _500_'){
          document.getElementById('failUser').textContent = 'Server Not Loaded';
        }else{
          document.getElementById('failUser').textContent = error;
        }
      });
}

function checkPasswords(){

    const newPassword = document.getElementById('newPassword').value;
    const check = document.getElementById('newCheck').value;
    return newPassword === check;
}

function clear(){

    document.getElementById('newUsername').value = '';
    document.getElementById('newPassword').value = '';
    document.getElementById('newEmail').value = '';
    document.getElementById('newCheck').value = '';
}

function printError(message){
    document.getElementById('failUser').textContent = message;
}