var token = 'put your telegram token here';
var Bot = require('node-telegram-bot-api');
var crypto = require('crypto');
var m_chat_id = undefined;
const m_username = 'admin';
const m_password = '57c6cbff0d421449be820763f03139eb'; //atlanta 57c6cbff0d421449be820763f03139eb

var loggedIn = false;
var lastLogginMilliseconds = (new Date).getTime();
var lastCommandMilliseconds = (new Date).getTime();

const exec = require('child_process').exec;

var bot = new Bot(token, {polling: true});

console.log('xelly bot server started');

var checkLastCommandTime = function (msg, newTimestamp) {
    var diffMilliseconds = newTimestamp - lastCommandMilliseconds;
    lastCommandMilliseconds = newTimestamp;
    if(diffMilliseconds <= 5000) {
        bot.sendMessage(msg.chat.id, 'Abusive caller!').then(function (){
            // reply sent!
        });
        return false;
    } else {
        return true;
    }

};

var chatIdUserValidation = function (msg) {
    if (m_chat_id === msg.chat.id) {
	return true;
    }
    return false;
}

var accessValidation = function (msg) {
    var notAbusive = checkLastCommandTime(msg, (new Date).getTime());
    if (notAbusive === true) {
        var newChatIdUserValidation =  chatIdUserValidation(msg);
        if(newChatIdUserValidation === true) {
            return true;
        } else {
            console.log('xelly_bot:chat_id is wrong!');
            bot.sendMessage(msg.chat.id, 'Unauthorized!').then(function(){
                // reply sent!
            });
            return false;
        }
    }
    return false;
}


bot.onText(/^\/help/, function (msg) {
    console.log('xelly_bot:help');
    console.log('xelly_bot:msg.chat.id:' + msg.chat.id);
    bot.sendMessage(msg.chat.id, 
        'Commands:\n' +
        '/help \n' +
        '/hello \n' +
        '/shell pwd \n' +
        '/shell date \n' +
        '/shell ls \n'
    ).then(function(){
      // reply sent!
    });
});

bot.onText(/^\/a/, function (msg, match) {
    console.log('xelly_bot:a');
    var accessible = accessValidation(msg);
    if (accessible === true) {
        bot.sendMessage(msg.chat.id, 'Accessible!').then(function(){
            // reply sent!
        });
    }
});

bot.onText(/^\/login (.+)$/, function (msg, match) {
    console.log('xelly_bot:login');
    m_chat_id = msg.chat.id;
    var accessible = accessValidation(msg);
    console.log('CHAT_ID:' + m_chat_id);
    if (accessible === true) {
        var credentials = match[1].split(/\s+/);    
        var username = credentials[0];
        var md5Password = crypto.createHash('md5').update(credentials[1]).digest("hex");

        if (m_username === username && m_password === md5Password) {
            console.log('Authenticated');
            loggedIn = true;
            lastLogginMilliseconds = (new Date).getTime();
            console.log(lastLogginMilliseconds);
        } else {
            console.log('Authentication failed!');
            loggedIn = false;
            lastLogginMilliseconds = undefined;
        }

        bot.sendMessage(msg.chat.id, 'Hello, ' + username + '!').then(function(){
            // reply sent!
        });
    }
});

bot.onText(/^\/logout/, function (msg, match) {
    console.log('xelly_bot:logout');
    loggedIn = false;
    lastLogginMilliseconds = undefined;
    m_chat_id = undefined;
    bot.sendMessage(msg.chat.id, 'Logged out').then(function (){
        // reply sent!
    });
});


bot.onText(/^\/hello (.+)$/, function (msg, match) {
    console.log('xelly_bot:hello');
    var name = match[1];
    bot.sendMessage(msg.chat.id, 'Hello ' + name + '!').then(function(){
      // reply sent!
    });
});

bot.onText(/^\/sum((\s+\d+)+)$/, function (msg, match) {
    console.log('xelly_bot:sum');
    var result = 0;
    match[1].trim().split(/\s+/).forEach(function (i) {
        result += (+i || 0);
    })
    bot.sendMessage(msg.chat.id, result).then(function() {
        // reply sent!
    });
});


//    var cmd = 'ls -la';
bot.onText(/^\/shell (.+)$/, function (msg, match) {
    var accessible = accessValidation(msg);
    if (accessible === true && loggedIn === true) {

        var cmd = match[1];
        var validCommand = false;
        switch(cmd)
        {
        case 'pwd':
        case 'date':
            validCommand = true;
            break;
        case 'ls':
            validCommand = true;
            break;
        default:
            console.log('default');
            break;
        }
        if(validCommand === true) {
            exec(cmd, function (error, stdout, stderr) {
                console.log('stdout: %s', stdout);
                console.log('stderr: %s', stderr);
                bot.sendMessage(msg.chat.id, '$ ' + cmd + '\n' + stdout).then(function(){
                    // reply sent!
                });
                if (error !== null) {
                    console.log('exec error: ${error}');
                    bot.sendMessage(msg.chat.id, 'Shell: ' + name + '!').then(function(){
                        // reply sent!
                    });
                }
            });
        } else {
            bot.sendMessage(msg.chat.id, 'Invalid command!').then(function(){
                    // reply sent!
            });
        }
    } 

});
