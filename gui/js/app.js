var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('http://localhost:8080/connect');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/user/message/received', function (message) {
            renderBotReply(JSON.parse(message.body));
        });
        stompClient.subscribe('/topic/bot/writing', function(rawStatus) {
            renderState(JSON.parse(rawStatus.body));
        });
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    var msgBody = $("#user-message-body").val();
    var message = {'body': msgBody};
    renderUserReply(message);
    stompClient.send("/app/bot/reply", {}, JSON.stringify(message));
    $("#user-message-body").val("");
}

function renderBotReply(message) {
    var createdAt = message.createdAt.epochSecond;
    var row = $(sprintf("<div class=\"row\" created-at=\"%s\"></div>", createdAt));
    var identifier = $("<div class=\"col-md-1\"><span class=\"identifier\" whoami=\"bot\">Bot</span></div>");
    var message = $(sprintf("<div class=\"col-md-11\"><p>%s</p></div>", message.body));

    identifier.appendTo(row);
    message.appendTo(row);
    row.appendTo("#historial");

    historialBottomScroll();
    clearState();
}

function renderUserReply(message) {
    var row = $("<div class=\"row\"></div>");
    var identifier = $("<div class=\"col-md-1\"><span class=\"identifier\" whoami=\"user\">User</span></div>");
    var message = $(sprintf("<div class=\"col-md-11\"><p>%s</p></div>", message.body));

    identifier.appendTo(row);
    message.appendTo(row);
    row.appendTo("#historial");

    historialBottomScroll();
}

function renderState(status) {
    $("#status-bar > span").html(status.body);
}

function clearState() {
    $("#status-bar > span").html("");
}

function historialBottomScroll() {
    var historial = $("#historial");
    historial.stop(true, true).animate({ scrollTop: historial.prop("scrollHeight") - historial.height() }, 200);
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    connect();
    $( "#send" ).click(function() { sendName(); });
});