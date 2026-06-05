# Tlias 智能学习辅助系统

基于 Spring Boot + Vue 3 的企业级智能学习管理系统。

## 技术栈

### 后端
- **框架**: Spring Boot 4.0 + MyBatis
- **数据库**: MySQL 8.0
- **分页**: PageHelper
- **认证**: JWT 令牌
- **对象存储**: 阿里云 OSS

### 前端
- **框架**: Vue 3 + Vite
- **UI**: Element Plus
- **HTTP**: Axios
- **路由**: Vue Router
- **图表**: ECharts 5

## 项目结构

```
tlias-parent/                Maven 父项目
├── tlias-common/            通用模块
├── tlias-pojo/              POJO 类
├── tlias-utils/             工具类
└── tlias-web-management/    Spring Boot 后端 (端口 8080)
    ├── controller/          RESTful 接口
    ├── service/             业务逻辑
    ├── mapper/              数据访问
    ├── interceptor/         JWT 拦截器
    ├── aop/                 操作日志切面
    └── exception/           全局异常处理

vue-tlias-management/        Vue 3 前端
├── src/
│   ├── api/                 API 接口封装
│   ├── views/               页面组件
│   ├── router/              路由配置
│   └── utils/               Axios 工具封装
└── deploy.bat               一键构建部署脚本
```

## 功能模块

- 登录认证（JWT）
- 部门管理
- 员工管理（含头像上传）
- 班级管理
- 学员管理（含违纪扣分）
- 操作日志
- 数据统计报表（ECharts 饼图）

## 快速开始

### 1. 数据库
```sql
-- 创建数据库
CREATE DATABASE tlias;
-- 导入建表脚本（见 docs/sql/）
```

### 2. 后端
```bash
cd tlias-web-management
mvn spring-boot:run
```
后端启动在 `http://localhost:8080`

### 3. 前端
```bash
cd vue-tlias-management
npm install
npm run dev
```
开发服务器在 `http://localhost:5173`

### 4. 生产部署
```bash
cd vue-tlias-management
deploy.bat     # 构建并部署到 Nginx
```
访问 `http://localhost:90`
