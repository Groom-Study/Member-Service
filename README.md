# Auth Service

Spring Boot와 JPA를 기반으로 한 회원가입 및 로그인 기능이 구현된 인증 서비스 프로젝트입니다.

## 🚀 주요 기능

- **회원 관리**
  - 회원가입 (BCrypt 비밀번호 암호화 적용)
  - 로그인/로그아웃 (HttpSession 기반 세션 관리)
- **권한 관리**
  - Role 기반 접근 제어 (`USER`, `ADMIN`)
  - 관리자 전용 대시보드 및 회원 목록 조회

## 🛠 Tech Stack

- **Backend**: Java 17, Spring Boot 3.x
- **Persistence**: Spring Data JPA, Hibernate, MySQL 8.0
- **Frontend**: Thymeleaf, CSS3
- **DevOps**: Docker, Docker Compose
- **Build Tool**: Gradle
- **Library**: Lombok, Spring Security (Crypto)

## 🏗 프로젝트 구조

```text
src/main/java/notnull/authservice/
├── config/             # 설정 클래스 (AppConfig)
├── controller/         # 웹 컨트롤러
├── dto/                # 데이터 전송 객체 (LoginDto, RegisterDto)
├── entity/             # JPA 엔티티 (Member)
├── exception/          # 커스텀 예외 처리
├── repository/         # JPA 레포지토리
└── service/            # 비즈니스 로직
```

## ⚙️ 실행 방법

### 1. 사전 요구사항
- Docker 및 Docker Compose가 설치되어 있어야 합니다.

### 2. Docker를 사용하여 실행
프로젝트 루트 디렉토리에서 다음 명령어를 입력합니다.

```bash
docker-compose up -d
```
이 명령어는 MySQL 데이터베이스와 애플리케이션 서버를 함께 실행합니다.

### 3. 접속 정보
- **Application**: `http://localhost:8080`
- **Database Host**: `localhost:3306`
- **Database Name**: `authservice`

## 🔑 테스트 계정

`data.sql`을 통해 초기 데이터가 생성됩니다.

| 역할 | 아이디 | 비밀번호 |
| :--- | :--- | :--- |
| **관리자** | `admin` | `admin1234` |
| **사용자** | `user1` ~ `user10` | `pass1234` |

## 📁 주요 엔드포인트

- `/login`: 로그인 페이지
- `/register`: 회원가입 페이지
- `/`: 메인 홈 (로그인 시 접근 가능)
- `/admin/home`: 관리자 홈
- `/admin/members`: 관리자 전용 회원 목록 조회
- `/logout`: 로그아웃
