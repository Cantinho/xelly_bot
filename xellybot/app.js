var token = 'your telegram token here';
var Bot = require('node-telegram-bot-api');

var sys = require('sys');
const exec = require('child_process').exec;

var bot = new Bot(token, {polling: true});

console.log('xelly bot server started');

bot.onText(/^\/help/, function (msg) {
    console.log('xelly_bot:help');
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

bot.onText(/^\/hello (.+)$/, function ( msg, match) {
    console.log('xelly_bot:say_hello');
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
            //sys.puts(stdout);
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

});
