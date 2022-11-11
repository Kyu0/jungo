(function () {
    
    document.getElementById('form-register').addEventListener('submit', (event) => {
        fetch('/api/member', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                'id': document.getElementById('username').value,
                'password': document.getElementById('password').value,
                'authority': 'USER'
            })
        })
        .then((res) => {
            console.log('success')
        })
        .catch((err) => {
            console.error('error occured')
        })
    })
})();