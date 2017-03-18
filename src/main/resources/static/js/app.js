
var inst = new mdui.Dialog('#single-chat', {
    modal: true
});

var mine_id, to_id, socket;
var t_wait;

/**
 * 开始聊天
 */
function show_signle_dialog() {
    var nick_name = $('#nick_name').val();
    if (nick_name == '') {
        $('#nick_name').focus();
        mdui.snackbar({
            message: '请输入一个昵称'
        });
        return;
    }
    // 将昵称记录到cookie
    $.cookie('FC_NICKNAME', nick_name, {expires: 7});
    inst.open();
    socket.emit('upname', nick_name);
}

/**
 * 关闭聊天室
 */
function close_dialog() {
    $('#chat_msg').val('');
    mine_id = null, to_id = null;
    setTimeout(function () {
        $('.mdui-dialog-content .chat-body').html('<p class="mdui-m-b-3"><span class="chat-box-info">系统正在为您匹配好友...</span></p>');
    }, 180);
}

/**
 * 发送消息
 */
function send_msg() {
    var msg = $('#chat_msg').val();
    if (socket && msg != '') {
        var nick_name = $('#nick_name').val();
        var sendObj = {
            '@class'    :   'com.fc.model.Message',
            id          :   mine_id,
            to_id       :   to_id,
            msg         :   msg,
            nick_name   :   nick_name
        };
        socket.json.send(sendObj);
        var html = '<p class="mdui-m-b-3"><span class="chat-box-pink">我 : '+ msg +'</span></p>';
        $('.mdui-dialog-content .chat-body').append(html);
        $('.mdui-dialog-content .chat-body').scrollTop($('.mdui-dialog-content .chat-body').height());
        $('#chat_msg').val('');
        $('#chat_msg').focus();
    }
}

$(document).ready(function () {
    var nickName = $.cookie('FC_NICKNAME');
    if(nickName && nickName != ''){
        $('#nick_name').val(nickName);
    }
    var chat_addr = $('#chat_addr').val();
    socket = io.connect(chat_addr + '/single');
    if (socket) {
        socket.on('connect', function(){
            console.log('连接到服务器...');
        });
        socket.on('disconnect', function(){
            console.log('断开连接...');
        });
        socket.on('error', function(err){
            console.log(err);
        });

        // 接收聊天消息
        socket.on('receiveMsg', function(data){
            var nick_name = data.nick_name;
            var msg = data.msg;
            var html = '<p class="mdui-m-b-3"><span class="chat-box-green">'+ nick_name +' : '+ msg +'</span></p>';
            to_id = data.id;
            $('.mdui-dialog-content .chat-body').append(html);
            $('.mdui-dialog-content .chat-body').scrollTop($('.mdui-dialog-content .chat-body').height());
        });

        // 自己上线
        socket.on('online', function (data) {
            mine_id = data.user.id;
            console.log('我的id:' + mine_id + ', 昵称:' + data.user.nick_name);
            t_wait = setInterval(function () {
                socket.emit('matach_friend', '');
            }, 500);
        });

        // 监听下线
        socket.on('changeOnline', function (data) {
            // 聊天用户已经离线，禁止输入消息
            if(data.state == 'offline' && to_id && data.user.id == to_id){
                to_id = null;
                $('#chat_msg').attr('disabled', true);
                console.log('用户'+ data.user.id +'/'+ data.user.nick_name+'下线了');
                var html = '<p class="mdui-m-b-3"><span class="chat-box-info">提示: '+ data.user.nick_name+'下线了' +'</span></p>';
                $('.mdui-dialog-content .chat-body').append(html);
                $('.mdui-dialog-content .chat-body').scrollTop($('.mdui-dialog-content .chat-body').height());
            }
            // 更新当前在线用户数
            $('#current_users').text(data.users || 0);
        });

        // 匹配
        socket.on('matched', function (data) {
            to_id = data.id;
            $('#chat_msg').attr('disabled', false);
            $('.mdui-dialog-content .chat-body').html('<p class="mdui-m-b-3"><span class="chat-box-info">为您匹配到了好友：'+ data.nick_name +'</span></p>');
            window.clearInterval(t_wait);
            $('.mdui-dialog-content .chat-body').scrollTop($('.mdui-dialog-content .chat-body').height());
        });
    }

});

