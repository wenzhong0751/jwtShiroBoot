## 项目依赖
- MySQL数据库，具体参数可参考配置文件

- Redis数据库，具体参数可参考配置文件

## 启动方法
> 在IDE中运行Application.java

## 接口使用方法
可参考Postman中的测试用例

#### 1. tokenGet 为获取动态密钥接口，使用密钥加密用户密码
* GET http://localhost:8080/account/register?tokenKey=get
* 返回：
> {
    "data": {
        "tokenKey": "fxa02mzc3yiiweii",
        "userKey": "IBW4AQ"
    },
    "meta": {
        "msg": "issued tokenKey success",
        "code": 1000,
        "success": true,
        "timestamp": 1578311346714
    }
}

#### 2. accountLogin为第一个要执行的操作
* POST  http://localhost:8080/account/login
* 请求：
> {
	"appId": "testusername",
    "password": "{{password}}",//使用第一步返回的密钥（5秒内有效)加密后的用户密码
    "methodName": "login",
    "userKey": "{{userKey}}",
    "timestamp": "{{$timestamp}}"
}

* 返回：
> {
      "meta": {
          "msg": "issue jwt success",
          "code": 1003,
          "success": true,
          "timestamp": "2020-01-10T07:12:32.173+0000"
      },
      "data": {
          "jwt": "eyJhbGciOiJIUzUxMiIsInppcCI6IkRFRiJ9.eNoszNsKwjAQBNB_2ecEcuk2aX9G1naVqE0llyKI_24iPs5hZt5wKwFmMMjKThNKMiPKgdQqSRmUi0Otjb_YhR0IyPXcyoVzqZlTpI0bhpw77neOsunBqSMVmDU6Pw7KohHAr-cf0P8g7Q_uQ1q3EEVPp2ttz_D5AgAA__8.7FXafSJVPskCfJKqk313XOnKA_I4A9LECdgaqZKzOIdTmsPr2AK_C3yvddR117KXfyAGnEUqZlF31sZ4bmK5pA",
          "user": {
              "uid": 2,
              "username": "testusername",
              "password": null,
              "salt": null,
              "email": "testemail",
              "nickname": "testnickname",
              "tel": "testtel",
              "addr": "testaddr",
              "regtime": "2019-10-11T09:14:15.000+0000",
              "disabled": false,
              "credentialsSalt": "testusernamenull"
          }
      }
  }

## 授权方法

- 所有资源，建立后即关联角色admin,这样只要非admin角色，都需要授权

> 可参考码云中的原代码说明，有Fork的
