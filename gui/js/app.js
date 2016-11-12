var stompClient = null;
var userCanSendMessage = true;

$(document).ready(function () {
    connect();
    $( "#send" ).click(function() {
        if (messageCanBeSent()) { sendUserMessage(); }
        else if(!messageInTextarea()) { renderState({ body: "There is no message!", fade: true }); }
    });
    $("#user-message-body").keypress(function(e) {
        if(e.which == 13 & !e.shiftKey) {
            e.preventDefault();
            $("#send").click();
        }
    });
});

function connect() {
    var socket = new SockJS('http://localhost:8080/connect');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/user/message/received', function (message) {
            renderBotReply(JSON.parse(message.body));
            toggleInteraction();
        });
        stompClient.subscribe('/topic/bot/writing', function(rawStatus) {
            renderState(JSON.parse(rawStatus.body));
            toggleInteraction();
        });
    });
}

function renderBotReply(message) {
    // The message es generated and rendered into #historial.
    var row = generateReply(message, "bot");
    row.appendTo("#historial");

    // UI Triggers
    historialBottomScroll();
    clearState();
}

function generateReply(message, whoami) {
    var createdAt = (message.createdAt) ? message.createdAt.epochSecond : null;
    var capsWhoami = s.capitalize(whoami);
    var parsedBody = parse(message.body);

    var row = $(sprintf("<div class=\"row\" created-at=\"%s\"></div>", createdAt));
    var identifierCol = $("<div class=\"col-md-1\"></div>");
    var identifier = $(sprintf("<span class=\"identifier\" whoami=\"%s\">%s</span>", whoami, capsWhoami));
    var message = $(sprintf("<div class=\"col-md-11\"><p>%s</p></div>", parsedBody));

    identifier.appendTo(identifierCol);
    identifierCol.appendTo(row);
    message.appendTo(row);
    return row;
}

function parse(string) {
    var result = s.replaceAll(string, "\n", "<br/>");
    return result;
}

function historialBottomScroll() {
    var historial = $("#historial");
    historial.stop(true, true).animate({ scrollTop: historial.prop("scrollHeight") - historial.height() }, 200);
}

function clearState() {
    $("#status-bar > span").html("");
}

function toggleInteraction() {
    userCanSendMessage = !userCanSendMessage;
    $("#send").attr("disabled", !userCanSendMessage);
}

function renderState(status) {
    $("#status-bar > span").html(status.body);
    if (status.fade) {
        setTimeout(function() {
            $("#status-bar > span").remove();
            $("<span></span>").appendTo("#status-bar");
        }, 500);
    }
}

function messageCanBeSent() {
    return userCanSendMessage && messageInTextarea();
}

function messageInTextarea() {
    return $("#user-message-body").val() != "";
}

function sendUserMessage() {
    var msgBody = $("#user-message-body").val();
    var message = {'body': msgBody};
    renderUserReply(message);
    stompClient.send("/app/bot/reply", {}, JSON.stringify(message));
    $("#user-message-body").val("");
}

function renderUserReply(message) {
    // The message es generated and rendered into #historial.
    var row = generateReply(message, "user");
    row.appendTo("#historial");

    // UI Triggers
    historialBottomScroll();
}