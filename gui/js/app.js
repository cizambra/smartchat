var stompClient = null;
var userCanSendMessage = true;

var Routes = {
    // Endpoints for Client - Server messages (@MessageMapping URIs).
    HALO: "/app/bot/halo",
    INCOMING_USER_MSG: "/app/bot/new_message",

    // Endpoints for Server - Client messages (@SendTo URIs).
    INCOMING_BOT_MSG: "/topic/user/message/received",
    BOT_WRITING: "/topic/bot/writing"
};

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
        stompClient.subscribe(Routes.INCOMING_BOT_MSG, function (message) {
            renderBotReply(JSON.parse(message.body));
            toggleInteraction();
        });
        stompClient.subscribe(Routes.BOT_WRITING, function(rawStatus) {
            renderState(JSON.parse(rawStatus.body));
            toggleInteraction();
        });
        stompClient.send(Routes.HALO, {}, "");
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
    var state = message.state;
    var domain = message.domain;

    var rowTemplate = "<div class=\"row\" created-at=\"%s\" whoami=\"%s\" state=\"%s\" domain=\"%s\"></div>";
    var row = $(sprintf(rowTemplate, createdAt, whoami, state, domain));
    var identifierCol = $("<div class=\"col-md-1\"></div>");
    var identifier = $(sprintf("<span class=\"identifier\" >%s</span>", capsWhoami));
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
    var message_body = $("#user-message-body").val();
    return message_body.trim() != "";
}

function sendUserMessage() {
    var nextState = $(".row[whoami=bot]").last().attr("state");
    var nextDomain = $(".row[whoami=bot]").last().attr("domain");
    var msgBody = $("#user-message-body").val();
    var message = {'body': msgBody, 'state': nextState, 'domain': nextDomain};
    renderUserReply(message);
    stompClient.send(Routes.INCOMING_USER_MSG, {}, JSON.stringify(message));
    $("#user-message-body").val("");
}

function renderUserReply(message) {
    // The message es generated and rendered into #historial.
    var row = generateReply(message, "user");
    row.appendTo("#historial");

    // UI Triggers
    historialBottomScroll();
}