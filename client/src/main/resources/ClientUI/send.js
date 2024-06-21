export async function send(endpoint, params, method, authToken){
    params = !!params ? params : undefined; // checks input
  
    return fetch('http://localhost:8080'+endpoint,{
      method: method,
      body: params,
      headers: {
        Authorization: authToken,
        'Accept':'application/json',
        'Content-Type':'application/json',
      },
    })
    .then(response => {
      if(!response.ok){
        throw new Error('_' + response.status + '_');
      }else{
        return response.json();
      }
    })
    .catch(error => {
      if(error instanceof TypeError){
        throw new Error('_500_');
      }else{
        throw error;
      }
    });
  }