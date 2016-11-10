var socket = io('http://localhost');

socket.on('news', function (data) {
    console.log(data);
    socket.emit('an-event', { my: 'data' });
});