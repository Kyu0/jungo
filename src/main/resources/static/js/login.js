(function () {
    
    document.getElementById('form-login').addEventListener('submit', (event) => {
        fetch('/api/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                'id': document.getElementById('username').value,
                'password': document.getElementById('password').value,
            })
        })
        .then((res) => {
            res.json()
        })
        .then((data) => {
            console.log(data)
        })
        .catch((err) => {
            console.error('error occured')
        })
    })
})();