/**
 * 存放主要交互逻辑js代码
 * javascript 模块化
 * seckill.detail.init{(params)}
 */

var seckill={
    //封装秒杀相关ajax的url
    URL:{
        now :function(){
            return '/seckill/time/now';
        },

        exposer:function(seckillId){
            console.log(seckillId);
            return '/seckill/'+seckillId +'/exposer';
        },
        execution: function (seckillId, md5) {
            console.log(seckillId);
            return '/seckill/' + seckillId + '/' + md5 + '/execution';
        }
    },

    //验证手机号
    vaildatePhone:function(phone){
        if(phone && phone.length==11 && !isNaN(phone)){
            return true;
        }else{
            return false;
        }
    },

    countDown: function (seckillId, nowTime, startTime, endTime){
        console.log(seckillId + '_' + nowTime + '_' + startTime + '_' + endTime);
        var seckillBox = $('#seckill-box');
        if(nowTime>endTime){
            //秒杀结束
            seckillBox.html('秒杀结束!');
        }else if (nowTime < startTime) {
            //秒杀未开始,计时事件绑定
            var killTime = new Date(startTime + 1000);//todo 防止时间偏移
            seckillBox.countdown(killTime, function (event) {
                //时间格式
                var format = event.strftime('秒杀倒计时: %D天 %H时 %M分 %S秒 ');
                seckillBox.html(format);
            }).on('finish.countdown', function () {
                //时间完成后回调事件
                //获取秒杀地址,控制现实逻辑,执行秒杀
                console.log(seckillId);
                seckill.handlerSeckill(seckillId, seckillBox);
            });
        } else {
            //秒杀开始
            seckill.handlerSeckill(seckillId, seckillBox);
        }
    },

    //获取秒杀地址,控制现实逻辑,执行秒杀
    handlerSeckill:function(seckillId,node){
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        console.log(seckillId);
        $.post(seckill.URL.exposer(seckillId),{},function(result){
            console.log(seckillId);
            //在回掉函数中执行交互逻辑
            if (result && result['success']){
                var exposer = result['data'];
                //开启秒杀
                if (exposer['exposed']){
                    //获取秒杀地址
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId, md5);
                    console.log("killUrl: " + killUrl);
                    //绑定一次点击事件
                    $('#killBtn').one('click', function () {
                        //执行秒杀请求
                        //1.先禁用按钮
                        $(this).addClass('disabled');//,<-$(this)===('#killBtn')->
                        //2.发送秒杀请求执行秒杀
                        $.post(killUrl, {}, function (result) {
                            if (result && result['success']) {
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                //显示秒杀结果
                                node.html('<span class="label label-success">' + stateInfo + '</span>');
                            }
                        });
                    });
                    node.show();

                }else{
                    //未开启秒杀(浏览器计时偏差)
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    console.log(seckillId);
                    seckill.countDown(seckillId, now, start, end);
                }
            }else{
                console.log('result: ' + result);
            }
        });
    },

    //详情页秒杀逻辑
    detail:{
        //详情页初始化
        init:function(params){
            //用户手机验证和登陆,计时交互
            //规划交互流程
            //在cookie中查找手机号
            var killPhone=$.cookie('killPhone');
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];

            //验证手机
            if(!seckill.vaildatePhone(killPhone)){
                //绑定手机
                //控制输出
                var killPhoneModal=$('#killPhoneModal');
                killPhoneModal.modal({
                    //显示弹出层
                    show :true,
                    //禁止位置关闭
                    backdrop:'static',
                    //关闭键盘事件
                    keyboard:false
                });
                $('#killPhoneBtn').click(function(){
                    var inputPhone=$('#killPhoneKey').val();
                    if(seckill.vaildatePhone(inputPhone)){
                        //电话写入cookie(7天过期)
                        $.cookie('killPhone', inputPhone, {expires: 7, path: '/seckill'});
                        //刷新页面
                        window.location.reload();
                    }else{
                        //todo 错误文案信息抽取到前端字典里
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误!</label>').show(300);
                    }
                });
            }

            //已经登陆 计时交互
            $.get(seckill.URL.now(),{},function(result){
                if(result && result['success']){
                    var nowTime = result['data'];
                    console.log(seckillId);
                    //时间判断 计时交互
                    seckill.countDown(seckillId, nowTime, startTime, endTime);

                }else{
                    alert('result: ' + result);
                }
            });


        }

    }
}