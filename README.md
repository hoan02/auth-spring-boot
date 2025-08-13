# 🔐 Spring Boot Auth Service

## 📋 Giới thiệu
Spring Boot Auth Service là ứng dụng xác thực và phân quyền hiện đại, được xây dựng với Spring Boot 3.x, hỗ trợ JWT token, Redis caching và Oracle Database.

## ✨ Tính năng chính
- ✅ **Xác thực JWT**: Access & Refresh token với thời gian sống linh hoạt
- ✅ **Phân quyền RBAC**: Role-based access control với groups, roles, permissions
- ✅ **Caching Redis**: Tối ưu hiệu suất với Redis caching
- ✅ **Oracle Database**: Hỗ trợ Oracle Database với JPA/Hibernate
- ✅ **MicroK8s Ready**: Deployment package cho MicroK8s với Helm 3
- ✅ **CI/CD Jenkins**: Pipeline tự động hoàn chỉnh

## 🛠️ Công nghệ sử dụng
- **Backend**: Spring Boot 3.4.4, Java 17
- **Security**: Spring Security, JWT (jjwt)
- **Database**: Oracle Database, JPA/Hibernate
- **Caching**: Redis
- **Container**: Docker, MicroK8s
- **CI/CD**: Jenkins, Helm 3

## 🚀 Triển khai nhanh

### 1. Local Development
```bash
# Clone repository
git clone https://github.com/hoan02/auth-spring-boot.git
cd auth-spring-boot

# Build & run
./mvnw clean package
java -jar target/demo-spring-boot-*.jar
```

### 2. Docker
```bash
docker build -t auth-service .
docker run -p 7999:7999 auth-service
```

### 3. MicroK8s với Helm
```bash
# Cài đặt MicroK8s
./scripts/setup-microk8s.sh

# Deploy với Helm
helm install auth-service ./helm/auth-service

# Kiểm tra
mkc get pods -n auth-service
```

### 4. Jenkins CI/CD
- Pipeline tự động: build → test → package → deploy
- Hỗ trợ Kaniko build, kubectl deploy
- Health check và rollback tự động

## 👨‍💻 Thông tin tác giả
- **Tác giả**: HoanIT
- **GitHub**: [https://github.com/hoan02/auth-spring-boot](https://github.com/hoan02/auth-spring-boot)
- **Email**: lehoan.dev@gmail.com

## 📄 License
MIT License - xem file LICENSE để biết thêm chi tiết.

## 🤝 Đóng góp
Mọi đóng góp đều được hoan nghênh! Vui lòng tạo Pull Request hoặc Issue trên GitHub.
