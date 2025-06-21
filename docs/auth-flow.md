# 用户注册 / 登录 / JWT 身份验证流程

本文件记录了线上预约抢购系统中的使用者认证流程，包含注册、登录、JWT Token 验证的完整过程。

---

## 🔐 身份验证流程图（Mermaid）
```mermaid
sequenceDiagram
    participant C as Client
    participant A as API Server
    participant DB as MySQL DB
    participant JWT as JWT Generator

    %% 注册流程
    C->>A: POST /api/auth/register (username, password)
    A->>DB: 查询用户名是否已存在
    alt 用户名重复
        A-->>C: 返回 400 用户已存在
    else 用户名可用
        A->>A: 使用 BCrypt 加密密码
        A->>DB: 存入新用户
        A-->>C: 返回 注册成功
    end

    %% 登录流程
    C->>A: POST /api/auth/login (username, password)
    A->>DB: 查询用户资料
    A->>A: BCrypt 验证密码
    alt 密码不正确
        A-->>C: 返回 401 登录失败
    else 密码正确
        A->>JWT: 生成 JWT（含 userId/username）
        JWT-->>A: 回传 JWT Token
        A-->>C: 返回 Token
    end

    %% 后续请求验证
    C->>A: 请求 API，附带 Authorization: Bearer Token
    A->>A: 验证 Token 有效性
    A->>A: 解析 userId，设置登录上下文
    A-->>C: 返回业务数据
```

## 🧩 流程概览

### 📌 注册流程
- 用户提交用户名与密码
- 检查用户名是否已存在
- 使用 BCryptPasswordEncoder 加密密码
- 保存用户信息至数据库

### 📌 登录流程
- 用户提交用户名与密码
- 验证用户是否存在与密码正确
- 生成 JWT Token，返回给客户端
- 客户端保存 Token（建议存在 LocalStorage）

### 📌 请求认证流程（JWT 验证）
- 所有需要认证的 API，需附上 Authorization: Bearer <token>
- 后端解析 JWT，验证有效性
- 若合法，则将用户信息存入 SecurityContext，允许请求通过

## ✅ 技术关键点

| 项目 | 工具 / 技术 |
|------|--------------|
| 密码加密 | BCryptPasswordEncoder |
| Token 签发 | JWT（Java JWT 或 jjwt） |
| 身份认证流程 | Spring Security + JWT 过滤器 |
| 无状态登录 | 后端不保存 session，全部透过 Token 身份识别 |

## 📁 相关文件路径（待开发）

| 文件 / 类 | 功能说明 |
|-----------|-----------|
| `User.java` | 用户实体类 |
| `UserRepository.java` | JPA Repository |
| `AuthController.java` | 注册与登录的 API |
| `JwtTokenProvider.java` | JWT 生成与验证工具类 |
| `SecurityConfig.java` | Spring Security 配置类 |
| `JwtFilter.java` | 自定义 JWT 过滤器（验证 token 有效性） |