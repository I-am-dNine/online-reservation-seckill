# System Overview - Online Reservation & Seckill System 系统架构总览、组件说明 

本文件为《线上预约抢购系统》的系统总览，目标为展示高并发处理能力的后端作品，适用于简历项目展示与技能实作。

---

## 💼 Project Goals

- 实作一个支持高并发预约 / 抢购操作的后端系统
- 防止超卖、重复预约等问题
- 整合现代后端关键技术：Redis 缓存、分布式锁、限流、异步消息队列、Spring Security、JWT、Docker
- 适用于个人履历项目，可对外展示

---

## 📦 初步模块规划
1. 使用者模块（User Module）
- 注册、登录（JWT）
- 查看可预约项目、查看自己预约纪录

2. 抢购模块（Seckill Module）
- 可预约项目列表（Event）
- 发起预约 / 抢购
- 防止重复、库存不足处理

3. 后台管理模块（Admin Module）
- 项目 CRUD（新增/修改/名额）
- 查看预约纪录 / 数据分析

4. 系统基础设施模块
- ✅ Redis：库存预加载、限流、分布式锁
- ✅ RabbitMQ：异步预约请求排队
- ✅ Spring Security：权限认证
- ✅ Swagger：接口文件说明
- ✅ Docker：部署封装（进阶）

## 🔐 流程说明图（Mermaid）