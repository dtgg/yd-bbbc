#lobby
C->s
1000001 = PlayerLogin
1000023 = PlayerHeadUrl//更换头像

1000031 = MobileRegister        //手机注册
1000032 = SmsSend               //发送验证码
1000033 = PlayerResetPassword   //重置密码

#推广
1000070 = ParityOrder
1000071 = ParityOrderDetail
1000072 = ParityOrderLv
1000073 = ParityRecommendTask
1000074 = ParityReceive

1000075 = ParityBonusAmountDraw
1000076 = ParityCollectBonus

#获取公告
1000080 = GetAnnouncement
#获取客服配置
1000082 = GetServiceConfig

#客户端进入房间前，判断是否可以进入房间
1000800 = CheckEnterRoom
s->c
1000005 = LoginSuccess
1000002 = LobbyKickPlayer //踢掉用户

1000024 = PlayerHeadUrlSuccess  //更换头像成功

1001031 = MobileRegisterSuc     //手机注册成功
1001032 = SmsSendSuc            //发送验证码成功
1001033 = PlayerResetPasswordSuc//重置密码成功

9010000 = CoinPointSuccess  //通知玩家筹码变化
9030000 = SRemainTickets    //通知玩家门票变化

#推广
1001070 = ParityOrderSuc
1001071 = ParityOrderDetailSuc
1001072 = ParityOrderLvSuc
1001073 = ParityRecommendTaskSuc
1001074 = ParityReceiveSuc

1001075 = ParityBonusAmountDrawSuc
1001076 = ParityCollectBonusSuc

#获取公告
1000081 = SAnnouncement
#获取客服配置
1000083 = SServiceConfig

#客户端进入房间前，判断是否可以进入房间
1000801 = CheckEnterRoomSuc

addmoney
1000030
c->s
1000060 = GetGameVersion //获取版本号
s->c
1000061 =GameVersion //获取版本号

