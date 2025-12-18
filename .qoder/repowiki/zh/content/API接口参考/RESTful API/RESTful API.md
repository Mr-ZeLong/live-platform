# RESTful API

<cite>
**本文档引用文件**  
- [UserLoginController.java](file://live-api/src/main/java/com/logilong/live/api/controller/UserLoginController.java)
- [GiftController.java](file://live-api/src/main/java/com/logilong/live/api/controller/GiftController.java)
- [BankController.java](file://live-api/src/main/java/com/logilong/live/api/controller/BankController.java)
- [LivingRoomController.java](file://live-api/src/main/java/com/logilong/live/api/controller/LivingRoomController.java)
- [ShopInfoController.java](file://live-api/src/main/java/com/logilong/live/api/controller/ShopInfoController.java)
- [ImController.java](file://live-api/src/main/java/com/logilong/live/api/controller/ImController.java)
- [HomePageController.java](file://live-api/src/main/java/com/logilong/live/api/controller/HomePageController.java)
- [GiftReqVO.java](file://live-api/src/main/java/com/logilong/live/api/vo/req/GiftReqVO.java)
- [LivingRoomReqVO.java](file://live-api/src/main/java/com/logilong/live/api/vo/req/LivingRoomReqVO.java)
- [PayProductReqVO.java](file://live-api/src/main/java/com/logilong/live/api/vo/req/PayProductReqVO.java)
- [ShopCarReqVO.java](file://live-api/src/main/java/com/logilong/live/api/vo/req/ShopCarReqVO.java)
- [SkuInfoReqVO.java](file://live-api/src/main/java/com/logilong/live/api/vo/req/SkuInfoReqVO.java)
- [OnlinePKReqVO.java](file://live-api/src/main/java/com/logilong/live/api/vo/req/OnlinePKReqVO.java)
- [GiftConfigVO.java](file://live-api/src/main/java/com/logilong/live/api/vo/resp/GiftConfigVO.java)
- [ImConfigVO.java](file://live-api/src/main/java/com/logilong/live/api/vo/resp/ImConfigVO.java)
- [HomePageVO.java](file://live-api/src/main/java/com/logilong/live/api/vo/HomePageVO.java)
- [LivingRoomInitVO.java](file://live-api/src/main/java/com/logilong/live/api/vo/LivingRoomInitVO.java)
- [PrepareOrderVO.java](file://live-api/src/main/java/com/logilong/live/api/vo/PrepareOrderVO.java)
- [ShopCarRespVO.java](file://live-api/src/main/java/com/logilong/live/api/vo/resp/ShopCarRespVO.java)
- [PayProductRespVO.java](file://live-api/src/main/java/com/logilong/live/api/vo/resp/PayProductRespVO.java)
- [LivingRoomRespVO.java](file://live-api/src/main/java/com/logilong/live/api/vo/resp/LivingRoomRespVO.java)
- [WebResponseVO.java](file://live-common-interface/src/main/java/com/logilong/live/common/interfaces/vo/WebResponseVO.java)
- [ApiErrorEnum.java](file://live-api/src/main/java/com/logilong/live/api/error/ApiErrorEnum.java)
</cite>

## 目录
1. [简介](#简介)
2. [统一响应结构](#统一响应结构)
3. [认证方式](#认证方式)
4. [用户登录控制器](#用户登录控制器)
5. [礼物控制器](#礼物控制器)
6. [支付控制器](#支付控制器)
7. [直播间控制器](#直播间控制器)
8. [购物信息控制器](#购物信息控制器)
9. [IM控制器](#im控制器)
10. [首页控制器](#首页控制器)
11. [错误码说明](#错误码说明)

## 简介
本文档详细描述了基于 `live-api` 模块中各 Controller 类实现的 RESTful API 接口。涵盖用户登录、送礼、支付、直播间管理、购物车、IM 配置及首页数据聚合等核心功能。每个接口均提供 HTTP 方法、URL 路径、请求参数、请求体结构（VO）、响应格式（WebResponseVO）、错误码（ApiErrorEnum）及认证方式的完整说明。

## 统一响应结构
所有 API 接口均返回统一的响应结构 `WebResponseVO`，包含以下字段：

| 字段 | 类型 | 说明 |
|------|------|------|
| code | Integer | 业务状态码，0 表示成功，非 0 表示失败 |
| msg | String | 响应消息，成功时为 "success"，失败时为具体错误描述 |
| data | Object | 响应数据，具体结构根据接口而定，可能为 null |

**示例（成功响应）：**
```json
{
  "code": 0,
  "msg": "success",
  "data": { /* 具体数据 */ }
}
```

**示例（错误响应）：**
```json
{
  "code": 1001,
  "msg": "参数错误",
  "data": null
}
```

**Section sources**
- [WebResponseVO.java](file://live-common-interface/src/main/java/com/logilong/live/common/interfaces/vo/WebResponseVO.java)

## 认证方式
大部分接口需要用户登录状态。系统通过上下文 `LiveRequestContext` 获取当前用户 ID（`userId`）。用户需先通过 `/userLogin/login` 接口登录获取会话，后续请求携带会话信息即可完成认证。

**Section sources**
- [LiveRequestContext.java](file://live-framework-web-starter/src/main/java/com/logilong/live/web/starter/context/LiveRequestContext.java)

## 用户登录控制器
处理用户登录与验证码相关业务。

### 发送登录验证码
发送手机验证码。

- **HTTP 方法**: POST
- **URL 路径**: `/userLogin/sendLoginCode`
- **请求参数**:
  - `phone` (String, 必填): 手机号码
- **请求体**: 无
- **响应格式**: `WebResponseVO<Void>`
- **认证方式**: 无需登录

**示例请求：**
```http
POST /userLogin/sendLoginCode?phone=13800138000
```

**Section sources**
- [UserLoginController.java](file://live-api/src/main/java/com/logilong/live/api/controller/UserLoginController.java#L19-L23)

### 用户登录
使用手机号和验证码登录。

- **HTTP 方法**: POST
- **URL 路径**: `/userLogin/login`
- **请求参数**:
  - `phone` (String, 必填): 手机号码
  - `code` (Integer, 必填): 验证码
- **请求体**: 无
- **响应格式**: `WebResponseVO<UserLoginVO>` (注：VO 结构未在代码中直接体现，但服务层会返回用户信息)
- **认证方式**: 无需登录

**示例请求：**
```http
POST /userLogin/login?phone=13800138000&code=123456
```

**Section sources**
- [UserLoginController.java](file://live-api/src/main/java/com/logilong/live/api/controller/UserLoginController.java#L25-L29)

## 送礼控制器
管理直播间送礼逻辑。

### 获取礼物列表
获取可发送的礼物配置列表。

- **HTTP 方法**: POST
- **URL 路径**: `/gift/listGift`
- **请求参数**: 无
- **请求体**: 无
- **响应格式**: `WebResponseVO<List<GiftConfigVO>>`
- **认证方式**: 需登录

**GiftConfigVO 字段说明：**
| 字段 | 类型 | 说明 |
|------|------|------|
| giftId | Integer | 礼物ID |
| price | Integer | 价格（单位：虚拟币） |
| giftName | String | 礼物名称 |
| status | Integer | 状态（1:启用, 0:禁用） |
| coverImgUrl | String | 封面图片URL |
| svgaUrl | String | SVGA动画资源URL |
| createTime | Date | 创建时间 |
| updateTime | Date | 更新时间 |

**示例响应：**
```json
{
  "code": 0,
  "msg": "success",
  "data": [
    {
      "giftId": 1,
      "price": 100,
      "giftName": "玫瑰",
      "status": 1,
      "coverImgUrl": "https://...",
      "svgaUrl": "https://...",
      "createTime": "2023-01-01T00:00:00",
      "updateTime": "2023-01-01T00:00:00"
    }
  ]
}
```

**Section sources**
- [GiftController.java](file://live-api/src/main/java/com/logilong/live/api/controller/GiftController.java#L22-L30)
- [GiftConfigVO.java](file://live-api/src/main/java/com/logilong/live/api/vo/resp/GiftConfigVO.java)

### 发送礼物
向直播间或用户发送礼物。

- **HTTP 方法**: POST
- **URL 路径**: `/gift/send`
- **请求参数**: 无
- **请求体**: `GiftReqVO`
- **响应格式**: `WebResponseVO<Void>`
- **认证方式**: 需登录

**GiftReqVO 字段说明：**
| 字段 | 类型 | 是否必填 | 说明 |
|------|------|----------|------|
| giftId | int | 是 | 礼物ID |
| roomId | Integer | 否 | 直播间ID |
| senderUserId | Long | 否 | 发送者用户ID（通常由上下文获取） |
| receiverId | Long | 否 | 接收者用户ID（私聊送礼时使用） |
| type | int | 是 | 送礼类型（1:普通礼物, 2:红包等） |

**示例请求：**
```json
POST /gift/send
Content-Type: application/json

{
  "giftId": 1,
  "roomId": 1001,
  "type": 1
}
```

**Section sources**
- [GiftController.java](file://live-api/src/main/java/com/logilong/live/api/controller/GiftController.java#L32-L38)
- [GiftReqVO.java](file://live-api/src/main/java/com/logilong/live/api/vo/req/GiftReqVO.java)

## 支付控制器
处理支付产品查询与下单。

### 查询支付产品
根据类型查询可购买的支付产品。

- **HTTP 方法**: POST
- **URL 路径**: `/bank/products`
- **请求参数**:
  - `type` (Integer, 必填): 产品类型
- **请求体**: 无
- **响应格式**: `WebResponseVO<List<PayProductVO>>` (注：具体VO结构未在代码中直接体现，但服务层会返回产品列表)
- **认证方式**: 需登录

**示例请求：**
```http
POST /bank/products?type=1
```

**Section sources**
- [BankController.java](file://live-api/src/main/java/com/logilong/live/api/controller/BankController.java#L21-L25)

### 下单支付
为指定产品创建订单并发起支付。

- **HTTP 方法**: POST
- **URL 路径**: `/bank/payProduct`
- **请求参数**: 无
- **请求体**: `PayProductReqVO`
- **响应格式**: `WebResponseVO<PayProductRespVO>` (注：具体VO结构未在代码中直接体现，但服务层会返回支付信息)
- **认证方式**: 需登录

**PayProductReqVO 字段说明：**
| 字段 | 类型 | 是否必填 | 说明 |
|------|------|----------|------|
| productId | Long | 是 | 产品ID |
| paySource | Integer | 是 | 支付来源（参考 `PaySourceEnum`） |
| payChannel | Integer | 是 | 支付渠道（参考 `PayChannelEnum`） |

**示例请求：**
```json
POST /bank/payProduct
Content-Type: application/json

{
  "productId": 10001,
  "paySource": 1,
  "payChannel": 1
}
```

**Section sources**
- [BankController.java](file://live-api/src/main/java/com/logilong/live/api/controller/BankController.java#L33-L36)
- [PayProductReqVO.java](file://live-api/src/main/java/com/logilong/live/api/vo/req/PayProductReqVO.java)

## 直播间控制器
管理直播间相关操作。

### 获取直播间列表
分页查询直播间列表。

- **HTTP 方法**: POST
- **URL 路径**: `/living/list`
- **请求参数**: 无
- **请求体**: `LivingRoomReqVO`
- **响应格式**: `WebResponseVO<PageWrapper<LivingRoomRespVO>>` (注：`PageWrapper` 和 `LivingRoomRespVO` 结构未在代码中直接体现)
- **认证方式**: 需登录

**LivingRoomReqVO 字段说明：**
| 字段 | 类型 | 是否必填 | 说明 |
|------|------|----------|------|
| type | Integer | 是 | 房间类型 |
| page | int | 是 | 页码（从1开始） |
| pageSize | int | 是 | 每页数量（≤100） |
| roomId | Integer | 否 | 特定房间ID（用于查询单个房间） |

**示例请求：**
```json
POST /living/list
Content-Type: application/json

{
  "type": 1,
  "page": 1,
  "pageSize": 10
}
```

**Section sources**
- [LivingRoomController.java](file://live-api/src/main/java/com/logilong/live/api/controller/LivingRoomController.java#L25-L30)
- [LivingRoomReqVO.java](file://live-api/src/main/java/com/logilong/live/api/vo/req/LivingRoomReqVO.java)

### 开始直播
主播开启直播间。

- **HTTP 方法**: POST
- **URL 路径**: `/living/startingLiving`
- **请求参数**:
  - `type` (Integer, 必填): 直播间类型
- **请求体**: 无
- **响应格式**: `WebResponseVO<LivingRoomInitVO>`
- **认证方式**: 需登录
- **限流策略**: 10秒内最多调用1次

**LivingRoomInitVO 字段说明：**
| 字段 | 类型 | 说明 |
|------|------|------|
| roomId | Integer | 分配的直播间ID |

**示例请求：**
```http
POST /living/startingLiving?type=1
```

**Section sources**
- [LivingRoomController.java](file://live-api/src/main/java/com/logilong/live/api/controller/LivingRoomController.java#L32-L40)

### 结束直播
主播关闭直播间。

- **HTTP 方法**: POST
- **URL 路径**: `/living/closeLiving`
- **请求参数**:
  - `roomId` (Integer, 必填): 直播间ID
- **请求体**: 无
- **响应格式**: `WebResponseVO<Void>`
- **认证方式**: 需登录
- **限流策略**: 10秒内最多调用1次

**示例请求：**
```http
POST /living/closeLiving?roomId=1001
```

**Section sources**
- [LivingRoomController.java](file://live-api/src/main/java/com/logilong/live/api/controller/LivingRoomController.java#L49-L58)

### 主播配置
获取主播在特定直播间的相关配置。

- **HTTP 方法**: POST
- **URL 路径**: `/living/anchorConfig`
- **请求参数**:
  - `roomId` (Integer, 必填): 直播间ID
- **请求体**: 无
- **响应格式**: `WebResponseVO<AnchorConfigVO>` (注：具体VO结构未在代码中直接体现)
- **认证方式**: 需登录（且必须是主播）

**示例请求：**
```http
POST /living/anchorConfig?roomId=1001
```

**Section sources**
- [LivingRoomController.java](file://live-api/src/main/java/com/logilong/live/api/controller/LivingRoomController.java#L63-L66)

### 准备红包雨
为主播准备红包雨活动数据。

- **HTTP 方法**: POST
- **URL 路径**: `/living/prepareRedPacket`
- **请求参数**: 无
- **请求体**: `LivingRoomReqVO`
- **响应格式**: `WebResponseVO<RedPacketConfigVO>` (注：具体VO结构未在代码中直接体现)
- **认证方式**: 需登录（且必须是主播）
- **限流策略**: 10秒内最多调用1次

**示例请求：**
```json
POST /living/prepareRedPacket
Content-Type: application/json

{
  "roomId": 1001
}
```

**Section sources**
- [LivingRoomController.java](file://live-api/src/main/java/com/logilong/live/api/controller/LivingRoomController.java#L71-L75)

### 开始红包雨
广播开始红包雨活动。

- **HTTP 方法**: POST
- **URL 路径**: `/living/startRedPacket`
- **请求参数**:
  - `userId` (Long, 必填): 主播用户ID
  - `code` (String, 必填): 红包配置码
- **请求体**: 无
- **响应格式**: `WebResponseVO<Void>`
- **认证方式**: 需登录（且必须是主播）
- **限流策略**: 10秒内最多调用1次

**示例请求：**
```http
POST /living/startRedPacket?userId=10001&code=RP123456
```

**Section sources**
- [LivingRoomController.java](file://live-api/src/main/java/com/logilong/live/api/controller/LivingRoomController.java#L80-L84)

### 领取红包
用户领取红包。

- **HTTP 方法**: POST
- **URL 路径**: `/living/receiveRedPacket`
- **请求参数**: 无
- **请求体**: `LivingRoomReqVO`
- **响应格式**: `WebResponseVO<RedPacketReceiveVO>` (注：具体VO结构未在代码中直接体现)
- **认证方式**: 需登录
- **限流策略**: 1秒内最多调用1次

**示例请求：**
```json
POST /living/receiveRedPacket
Content-Type: application/json

{
  "redPacketConfigCode": "RP123456"
}
```

**Section sources**
- [LivingRoomController.java](file://live-api/src/main/java/com/logilong/live/api/controller/LivingRoomController.java#L89-L93)

## 购物信息控制器
处理购物车与订单准备。

### 获取商品列表
根据直播间ID获取商品列表。

- **HTTP 方法**: POST
- **URL 路径**: `/shop/listSkuInfo`
- **请求参数**:
  - `roomId` (Integer, 必填): 直播间ID
- **请求体**: 无
- **响应格式**: `WebResponseVO<List<SkuInfoVO>>` (注：具体VO结构未在代码中直接体现)
- **认证方式**: 需登录

**示例请求：**
```http
POST /shop/listSkuInfo?roomId=1001
```

**Section sources**
- [ShopInfoController.java](file://live-api/src/main/java/com/logilong/live/api/controller/ShopInfoController.java#L20-L23)

### 获取商品详情
获取指定商品的详细信息。

- **HTTP 方法**: POST
- **URL 路径**: `/shop/detail`
- **请求参数**: 无
- **请求体**: `SkuInfoReqVO`
- **响应格式**: `WebResponseVO<SkuDetailInfoVO>` (注：具体VO结构未在代码中直接体现)
- **认证方式**: 需登录

**SkuInfoReqVO 字段说明：**
| 字段 | 类型 | 是否必填 | 说明 |
|------|------|----------|------|
| skuId | Long | 是 | 商品SKU ID |
| anchorId | Long | 是 | 主播ID |

**示例请求：**
```json
POST /shop/detail
Content-Type: application/json

{
  "skuId": 20001,
  "anchorId": 10001
}
```

**Section sources**
- [ShopInfoController.java](file://live-api/src/main/java/com/logilong/live/api/controller/ShopInfoController.java#L25-L28)
- [SkuInfoReqVO.java](file://live-api/src/main/java/com/logilong/live/api/vo/req/SkuInfoReqVO.java)

### 添加商品到购物车
将商品添加到指定直播间的购物车。

- **HTTP 方法**: POST
- **URL 路径**: `/shop/addCar`
- **请求参数**: 无
- **请求体**: `ShopCarReqVO`
- **响应格式**: `WebResponseVO<Void>`
- **认证方式**: 需登录

**ShopCarReqVO 字段说明：**
| 字段 | 类型 | 是否必填 | 说明 |
|------|------|----------|------|
| skuId | Long | 是 | 商品SKU ID |
| roomId | Integer | 是 | 直播间ID |

**示例请求：**
```json
POST /shop/addCar
Content-Type: application/json

{
  "skuId": 20001,
  "roomId": 1001
}
```

**Section sources**
- [ShopInfoController.java](file://live-api/src/main/java/com/logilong/live/api/controller/ShopInfoController.java#L37-L40)

### 从购物车移除商品
从指定直播间的购物车中移除商品。

- **HTTP 方法**: POST
- **URL 路径**: `/shop/removeFromCar`
- **请求参数**: 无
- **请求体**: `ShopCarReqVO`
- **响应格式**: `WebResponseVO<Void>`
- **认证方式**: 需登录

**示例请求：**
```json
POST /shop/removeFromCar
Content-Type: application/json

{
  "skuId": 20001,
  "roomId": 1001
}
```

**Section sources**
- [ShopInfoController.java](file://live-api/src/main/java/com/logilong/live/api/controller/ShopInfoController.java#L42-L45)

### 获取购物车信息
获取指定直播间的购物车内容。

- **HTTP 方法**: POST
- **URL 路径**: `/shop/getCarInfo`
- **请求参数**: 无
- **请求体**: `ShopCarReqVO`
- **响应格式**: `WebResponseVO<ShopCarRespVO>`
- **认证方式**: 需登录

**示例请求：**
```json
POST /shop/getCarInfo
Content-Type: application/json

{
  "roomId": 1001
}
```

**Section sources**
- [ShopInfoController.java](file://live-api/src/main/java/com/logilong/live/api/controller/ShopInfoController.java#L47-L50)

### 清空购物车
清空指定直播间的购物车。

- **HTTP 方法**: POST
- **URL 路径**: `/shop/clearCar`
- **请求参数**: 无
- **请求体**: `ShopCarReqVO`
- **响应格式**: `WebResponseVO<Void>`
- **认证方式**: 需登录

**示例请求：**
```json
POST /shop/clearCar
Content-Type: application/json

{
  "roomId": 1001
}
```

**Section sources**
- [ShopInfoController.java](file://live-api/src/main/java/com/logilong/live/api/controller/ShopInfoController.java#L52-L55)

### 预下单
为购物车中的商品准备订单，锁定库存。

- **HTTP 方法**: POST
- **URL 路径**: `/shop/prepareOrder`
- **请求参数**: 无
- **请求体**: `PrepareOrderVO`
- **响应格式**: `WebResponseVO<PrepareOrderVO>` (返回包含订单号等信息)
- **认证方式**: 需登录

**示例请求：**
```json
POST /shop/prepareOrder
Content-Type: application/json

{
  "roomId": 1001,
  "skuList": [
    {"skuId": 20001, "count": 2}
  ]
}
```

**Section sources**
- [ShopInfoController.java](file://live-api/src/main/java/com/logilong/live/api/controller/ShopInfoController.java#L61-L64)

### 立即购买
为单个商品立即下单。

- **HTTP 方法**: POST
- **URL 路径**: `/shop/payNow`
- **请求参数**: 无
- **请求体**: `PrepareOrderVO`
- **响应格式**: `WebResponseVO<PayProductRespVO>`
- **认证方式**: 需登录

**示例请求：**
```json
POST /shop/payNow
Content-Type: application/json

{
  "roomId": 1001,
  "skuList": [
    {"skuId": 20001, "count": 1}
  ]
}
```

**Section sources**
- [ShopInfoController.java](file://live-api/src/main/java/com/logilong/live/api/controller/ShopInfoController.java#L71-L74)

## IM控制器
处理IM配置获取。

### 获取IM配置
获取客户端连接IM服务器所需的配置信息。

- **HTTP 方法**: POST
- **URL 路径**: `/im/getImConfig`
- **请求参数**: 无
- **请求体**: 无
- **响应格式**: `WebResponseVO<ImConfigVO>`
- **认证方式**: 需登录

**ImConfigVO 字段说明：**
| 字段 | 类型 | 说明 |
|------|------|------|
| token | String | IM认证Token |
| wsImServerAddress | String | WebSocket IM服务器地址 |
| tcpImServerAddress | String | TCP IM服务器地址 |

**示例响应：**
```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "token": "abc123xyz",
    "wsImServerAddress": "wss://im.example.com:8080",
    "tcpImServerAddress": "tcp://im.example.com:9090"
  }
}
```

**Section sources**
- [ImController.java](file://live-api/src/main/java/com/logilong/live/api/controller/ImController.java#L18-L21)
- [ImConfigVO.java](file://live-api/src/main/java/com/logilong/live/api/vo/resp/ImConfigVO.java)

## 首页控制器
提供首页数据聚合。

### 初始化首页
获取首页初始化数据。

- **HTTP 方法**: POST
- **URL 路径**: `/home/initPage`
- **请求参数**: 无
- **请求体**: 无
- **响应格式**: `WebResponseVO<HomePageVO>`
- **认证方式**: 可选（未登录用户也可访问，部分数据为空）

**HomePageVO 字段说明：**
| 字段 | 类型 | 说明 |
|------|------|------|
| loginStatus | boolean | 登录状态 |
| ... | ... | 其他聚合数据（如推荐直播间、热门商品等） |

**示例响应：**
```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "loginStatus": true,
    "recommendRooms": [...],
    "hotProducts": [...]
  }
}
```

**Section sources**
- [HomePageController.java](file://live-api/src/main/java/com/logilong/live/api/controller/HomePageController.java#L20-L30)
- [HomePageVO.java](file://live-api/src/main/java/com/logilong/live/api/vo/HomePageVO.java)

## 错误码说明
系统定义了统一的业务错误码，通过 `ApiErrorEnum` 枚举类管理。

| 错误码 | 错误消息 | 触发场景 |
|--------|----------|----------|
| 1001 | 参数错误 | 请求参数缺失或格式不正确 |
| 1002 | 业务异常 | 业务逻辑处理失败（如库存不足） |
| 1003 | 系统异常 | 系统内部错误 |
| 1004 | 重复请求 | 请求过于频繁，触发限流 |
| 2001 | 用户未登录 | 需要登录的接口未携带有效会话 |
| 2002 | 权限不足 | 当前用户无权执行该操作 |
| 3001 | 资源不存在 | 请求的资源（如直播间、商品）不存在 |
| 3002 | 资源已存在 | 创建资源时发现已存在 |
| 4001 | 库存不足 | 下单或预下单时库存不足 |
| 5001 | 支付失败 | 调用第三方支付接口失败 |

**Section sources**
- [ApiErrorEnum.java](file://live-api/src/main/java/com/logilong/live/api/error/ApiErrorEnum.java)