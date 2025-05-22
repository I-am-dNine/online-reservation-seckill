# 单元测试情境规划：UserService

| 方法          | 测试情境描述            | 输入 / 前提                              | 模拟依赖                        | 预期行为断言                                  |
|---------------|-------------------------|------------------------------------------|----------------------------------|-----------------------------------------------|
| register      | 注册成功                | 新用户名与密码                            | `userRepository.existsByUsername()` 回传 false<br>`userRepository.save()` 模拟保存 | 回传 "注册成功"                                |
| register      | 用户名重复注册失败      | 已存在用户名                              | `userRepository.existsByUsername()` 回传 true | 抛出 RuntimeException("用户名已存在")         |
| login         | 登录成功                | 用户存在且密码正确                        | `userRepository.findByUsername()` 回传 User 实体<br>`passwordEncoder.matches()` 回传 true<br>`jwtTokenProvider.generateToken()` 回传 token | 回传 `AuthResponse(token)`                    |
| login         | 用户不存在登录失败      | 用户名不存在                              | `userRepository.findByUsername()` 回传 empty | 抛出 RuntimeException("用户不存在")           |
| login         | 密码错误登录失败        | 用户存在但密码不正确                      | `passwordEncoder.matches()` 回传 false       | 抛出 RuntimeException("密码错误")             |
| createAdmin   | 创建管理员成功          | 新用户名与密码                            | 与 register 类似                         | 回传 "管理员创建成功"                          |
| createAdmin   | 管理员用户名已存在失败  | 与 register 相同                          | 与 register 相同                         | 抛出 RuntimeException("用户名已存在")         |
