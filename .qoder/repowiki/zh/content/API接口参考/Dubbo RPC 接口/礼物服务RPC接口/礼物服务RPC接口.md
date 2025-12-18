# 礼物服务RPC接口

<cite>
**本文档引用的文件**  
- [IGiftConfigRPC.java](file://live-gift-interface/src/main/java/com/logilong/live/gift/interfaces/IGiftConfigRPC.java)
- [IGiftRecordRPC.java](file://live-gift-interface/src/main/java/com/logilong/live/gift/interfaces/IGiftRecordRPC.java)
- [IRedPacketConfigRPC.java](file://live-gift-interface/src/main/java/com/logilong/live/gift/interfaces/IRedPacketConfigRPC.java)
- [GiftConfigDTO.java](file://live-gift-interface/src/main/java/com/logilong/live/gift/dto/GiftConfigDTO.java)
- [GiftRecordDTO.java](file://live-gift-interface/src/main/java/com/logilong/live/gift/dto/GiftRecordDTO.java)
- [RedPacketConfigReqDTO.java](file://live-gift-interface/src/main/java/com/logilong/live/gift/dto/RedPacketConfigReqDTO.java)
- [RedPacketConfigRespDTO.java](file://live-gift-interface/src/main/java/com/logilong/live/gift/dto/RedPacketConfigRespDTO.java)
- [RedPacketReceiveDTO.java](file://live-gift-interface/src/main/java/com/logilong/live/gift/dto/RedPacketReceiveDTO.java)
- [GiftConfigRPCImpl.java](file://live-gift-provider/src/main/java/com/logilong/live/gift/provider/rpc/GiftConfigRPCImpl.java)
- [GiftRecordRPCImpl.java](file://live-gift-provider/src/main/java/com/logilong/live/gift/provider/rpc/GiftRecordRPCImpl.java)
- [RedPacketConfigRPCImpl.java](file://live-gift-provider/src/main/java/com/logilong/live/gift/provider/rpc/RedPacketConfigRPCImpl.java)
- [IGiftConfigService.java](file://live-gift-provider/src/main/java/com/logilong/live/gift/provider/service/IGiftConfigService.java)
- [IGiftRecordService.java](file://live-gift-provider/src/main/java/com/logilong/live/gift/provider/service/IGiftRecordService.java)
- [IRedPacketConfigService.java](file://live-gift-provider/src/main/java/com/logilong/live/gift/provider/service/IRedPacketConfigService.java)
- [SendGiftTypeEnum.java](file://live-gift-interface/src/main/java/com/logilong/live/gift/constants/SendGiftTypeEnum.java)
- [RedPacketStatusEnum.java](file://live-gift-interface/src/main/java/com/logilong/live/gift/constants/RedPacketStatusEnum.java)
</cite>

## 目录
1. [简介](#简介)
2. [礼物配置管理接口](#礼物配置管理接口)
3. [送礼记录持久化接口](#送礼记录持久化接口)
4. [红包雨功能接口](#红包雨功能接口)
5. [核心数据传输对象](#核心数据传输对象)
6. [状态与类型枚举](#状态与类型枚举)

## 简介
本接口文档全面描述了直播平台礼物服务的核心RPC接口，涵盖礼物配置管理、送礼记录持久化以及红包雨功能三大模块。这些接口通过Dubbo服务暴露，为上层业务系统提供稳定可靠的礼物相关功能支持。

**Section sources**
- [IGiftConfigRPC.java](file://live-gift-interface/src/main/java/com/logilong/live/gift/interfaces/IGiftConfigRPC.java)
- [IGiftRecordRPC.java](file://live-gift-interface/src/main/java/com/logilong/live/gift/interfaces/IGiftRecordRPC.java)
- [IRedPacketConfigRPC.java](file://live-gift-interface/src/main/java/com/logilong/live/gift/interfaces/IRedPacketConfigRPC.java)

## 礼物配置管理接口
`IGiftConfigRPC` 接口负责礼物配置的全生命周期管理，包括查询、新增和更新操作。

### getByGiftId
根据礼物ID查询单个礼物配置信息。该方法在用户选择礼物或展示礼物详情时被调用，返回指定ID的礼物完整配置。

**Section sources**
- [IGiftConfigRPC.java#L12](file://live-gift-interface/src/main/java/com/logilong/live/gift/interfaces/IGiftConfigRPC.java#L12)
- [GiftConfigRPCImpl.java#L18-L20](file://live-gift-provider/src/main/java/com/logilong/live/gift/provider/rpc/GiftConfigRPCImpl.java#L18-L20)

### queryGiftList
查询所有可用礼物列表。此方法在直播间礼物面板初始化时被调用，返回系统中所有激活状态的礼物配置，供用户选择。

**Section sources**
- [IGiftConfigRPC.java#L17](file://live-gift-interface/src/main/java/com/logilong/live/gift/interfaces/IGiftConfigRPC.java#L17)
- [GiftConfigRPCImpl.java#L23-L25](file://live-gift-provider/src/main/java/com/logilong/live/gift/provider/rpc/GiftConfigRPCImpl.java#L23-L25)

### insertOne
插入一个新的礼物配置。该方法用于后台管理系统新增礼物时调用，将新的礼物信息持久化到数据库。

**Section sources**
- [IGiftConfigRPC.java#L22](file://live-gift-interface/src/main/java/com/logilong/live/gift/interfaces/IGiftConfigRPC.java#L22)
- [GiftConfigRPCImpl.java#L28-L30](file://live-gift-provider/src/main/java/com/logilong/live/gift/provider/rpc/GiftConfigRPCImpl.java#L28-L30)

### updateOne
更新现有礼物配置。当需要修改礼物价格、名称或状态时调用此方法，确保礼物信息的实时性和准确性。

**Section sources**
- [IGiftConfigRPC.java#L27](file://live-gift-interface/src/main/java/com/logilong/live/gift/interfaces/IGiftConfigRPC.java#L27)
- [GiftConfigRPCImpl.java#L33-L35](file://live-gift-provider/src/main/java/com/logilong/live/gift/provider/rpc/GiftConfigRPCImpl.java#L33-L35)

## 送礼记录持久化接口
`IGiftRecordRPC` 接口专注于送礼行为的记录与持久化。

### insertOne
持久化一条送礼记录。每当用户完成一次送礼操作，系统将调用此方法将送礼详情（包括用户ID、礼物ID、价格等）写入数据库，用于后续的数据分析和账单统计。

**Section sources**
- [IGiftRecordRPC.java#L10](file://live-gift-interface/src/main/java/com/logilong/live/gift/interfaces/IGiftRecordRPC.java#L10)
- [GiftRecordRPCImpl.java#L17-L19](file://live-gift-provider/src/main/java/com/logilong/live/gift/provider/rpc/GiftRecordRPCImpl.java#L17-L19)

## 红包雨功能接口
`IRedPacketConfigRPC` 接口提供完整的红包雨功能支持，涵盖权限查询、配置管理、红包准备与发放等核心流程。

### queryByAnchorId
根据主播ID查询其是否具备发放红包雨的权限。在主播尝试发起红包雨前调用此方法进行权限校验。

**Section sources**
- [IRedPacketConfigRPC.java#L12](file://live-gift-interface/src/main/java/com/logilong/live/gift/interfaces/IRedPacketConfigRPC.java#L12)
- [RedPacketConfigRPCImpl.java#L20-L22](file://live-gift-provider/src/main/java/com/logilong/live/gift/provider/rpc/RedPacketConfigRPCImpl.java#L20-L22)

### updateById
更新红包雨配置信息。当需要修改已存在的红包雨配置（如总金额、红包数量等）时调用此方法。

**Section sources**
- [IRedPacketConfigRPC.java#L17](file://live-gift-interface/src/main/java/com/logilong/live/gift/interfaces/IRedPacketConfigRPC.java#L17)
- [RedPacketConfigRPCImpl.java#L25-L27](file://live-gift-provider/src/main/java/com/logilong/live/gift/provider/rpc/RedPacketConfigRPCImpl.java#L25-L27)

### addOne
新增红包雨配置。在主播创建新的红包雨活动时调用，将配置信息存储到系统中。

**Section sources**
- [IRedPacketConfigRPC.java#L22](file://live-gift-interface/src/main/java/com/logilong/live/gift/interfaces/IRedPacketConfigRPC.java#L22)
- [RedPacketConfigRPCImpl.java#L30-L32](file://live-gift-provider/src/main/java/com/logilong/live/gift/provider/rpc/RedPacketConfigRPCImpl.java#L30-L32)

### prepareRedPacket
准备红包金额列表。在红包雨开始前，系统调用此方法根据配置生成随机金额的红包列表，并将其缓存以备快速分发。

**Section sources**
- [IRedPacketConfigRPC.java#L27](file://live-gift-interface/src/main/java/com/logilong/live/gift/interfaces/IRedPacketConfigRPC.java#L27)
- [RedPacketConfigRPCImpl.java#L35-L37](file://live-gift-provider/src/main/java/com/logilong/live/gift/provider/rpc/RedPacketConfigRPCImpl.java#L35-L37)

### receiveRedPacket
处理用户领取红包的请求。当用户参与红包雨活动时，调用此方法获取随机金额并记录领取行为。

**Section sources**
- [IRedPacketConfigRPC.java#L32](file://live-gift-interface/src/main/java/com/logilong/live/gift/interfaces/IRedPacketConfigRPC.java#L32)
- [RedPacketConfigRPCImpl.java#L40-L42](file://live-gift-provider/src/main/java/com/logilong/live/gift/provider/rpc/RedPacketConfigRPCImpl.java#L40-L42)

### startRedPacket
启动红包雨活动。此方法触发红包雨的正式开始，通知相关系统模块（如IM服务）向直播间内所有用户推送红包雨消息。

**Section sources**
- [IRedPacketConfigRPC.java#L37](file://live-gift-interface/src/main/java/com/logilong/live/gift/interfaces/IRedPacketConfigRPC.java#L37)
- [RedPacketConfigRPCImpl.java#L45-L47](file://live-gift-provider/src/main/java/com/logilong/live/gift/provider/rpc/RedPacketConfigRPCImpl.java#L45-L47)

## 核心数据传输对象

### GiftConfigDTO
礼物配置数据传输对象，包含以下关键字段：
- **giftId**: 礼物唯一标识符
- **price**: 礼物价格（钻石数量）
- **giftName**: 礼物名称
- **status**: 礼物状态（如启用/禁用）
- **coverImgUrl**: 礼物封面图片URL
- **svgaUrl**: 礼物动画SVGA资源URL
- **createTime**: 创建时间
- **updateTime**: 更新时间

**Section sources**
- [GiftConfigDTO.java#L15-L23](file://live-gift-interface/src/main/java/com/logilong/live/gift/dto/GiftConfigDTO.java#L15-L23)

### GiftRecordDTO
送礼记录数据传输对象，关键字段包括：
- **id**: 记录唯一标识
- **userId**: 送礼用户ID
- **objectId**: 接收对象ID（如主播ID）
- **source**: 送礼来源类型（参考SendGiftTypeEnum）
- **price**: 本次送礼价格
- **priceUnit**: 价格单位
- **giftId**: 所送礼物ID
- **sendTime**: 送礼时间

**Section sources**
- [GiftRecordDTO.java#L14-L22](file://live-gift-interface/src/main/java/com/logilong/live/gift/dto/GiftRecordDTO.java#L14-L22)

### RedPacketConfigReqDTO
红包雨配置请求DTO，用于新增和查询操作，包含：
- **id**: 配置ID
- **roomId**: 房间ID
- **status**: 配置状态
- **userId**: 用户ID
- **redPacketConfigCode**: 配置编码
- **totalPrice**: 红包总金额
- **totalCount**: 红包总数量
- **remark**: 备注信息

**Section sources**
- [RedPacketConfigReqDTO.java#L13-L21](file://live-gift-interface/src/main/java/com/logilong/live/gift/dto/RedPacketConfigReqDTO.java#L13-L21)

### RedPacketConfigRespDTO
红包雨配置响应DTO，用于返回查询结果，包含：
- **anchorId**: 主播ID
- **totalPrice**: 红包总金额
- **totalCount**: 红包总数量
- **configCode**: 配置编码
- **remark**: 备注信息

**Section sources**
- [RedPacketConfigRespDTO.java#L13-L18](file://live-gift-interface/src/main/java/com/logilong/live/gift/dto/RedPacketConfigRespDTO.java#L13-L18)

### RedPacketReceiveDTO
红包领取结果DTO，包含：
- **price**: 领取到的金额
- **notifyMsg**: 通知消息

**Section sources**
- [RedPacketReceiveDTO.java#L15-L17](file://live-gift-interface/src/main/java/com/logilong/live/gift/dto/RedPacketReceiveDTO.java#L15-L17)

## 状态与类型枚举

### SendGiftTypeEnum
送礼类型枚举，定义了不同场景下的送礼行为：
- **DEFAULT_SEND_GIFT(0)**: 直播间默认送礼物
- **PK_SEND_GIFT(1)**: 直播间PK送礼物

**Section sources**
- [SendGiftTypeEnum.java#L12-L13](file://live-gift-interface/src/main/java/com/logilong/live/gift/constants/SendGiftTypeEnum.java#L12-L13)

### RedPacketStatusEnum
红包状态枚举，表示红包雨的不同生命周期阶段：
- **NOT_PREPARED(1)**: 待准备
- **IS_PREPARED(2)**: 已准备
- **IS_SEND(3)**: 已发送

**Section sources**
- [RedPacketStatusEnum.java#L12-L14](file://live-gift-interface/src/main/java/com/logilong/live/gift/constants/RedPacketStatusEnum.java#L12-L14)