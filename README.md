# 🎓 회원관리 시스템 (Member Management System)

> **구름 팀미션(회원관리시스템)** > Spring Boot와 Vanilla JS를 활용한 풀스택 회원 인증 시스템입니다.

---

## 🛠 기술 스택 (Tech Stack)

### 🧱 Backend
- **Framework**: Spring Boot 3.x
- **Language**: Java 17
- **Database**: H2 Database (In-Memory/File)
- **ORM**: Spring Data JPA
- **Build Tool**: Gradle

### 🎨 Frontend
- **Languages**: HTML5, CSS3, JavaScript (ES6+)
- **Communication**: Fetch API (Asynchronous JSON)

### ☁️ Infrastructure
- **Platform**: AWS EC2 (Ubuntu 22.04 LTS)
- **Network**: Elastic IP (고정 IP), Security Groups (Inbound: 8080)

---

## ✨ 주요 기능 (Key Features)

### 1. 회원가입 (Join)
- 사용자로부터 이름, 이메일, 비밀번호를 입력받아 DB에 저장
- 비동기 통신을 통한 실시간 데이터 전송

### 2. 로그인 및 인증 (Login & Auth)
- 등록된 이메일/비밀번호 대조를 통한 사용자 인증
- 로그인 성공 시 서버로부터 사용자 실명(Name)을 전달받아 세션 유지

### 3. 동적 UI 렌더링
- **로그인 전**: 가입/로그인 폼 노출
- **로그인 후**: "OOO님, 환영합니다!" 메시지와 함께 대시보드 화면 전환
- `localStorage`를 활용하여 브라우저 새로고침 시에도 로그인 상태 유지

---

## 🏗 시스템 아키텍처 (Architecture)

1. **Presentation Layer**: `index.html`, `Vanilla JS` (사용자 인터페이스)
2. **Control Layer**: `MemberController` (API 엔드포인트)
3. **Business Layer**: `MemberService` (회원 가입/로그인 로직)
4. **Data Layer**: `MemberRepository`, `H2 DB` (데이터 영속성 관리)

---

## 📂 프로젝트 구조 (Project Structure)

```text
src/main/java/com/example/member_system/
├── controller/   # API 엔드포인트 정의
├── service/      # 비즈니스 로직 처리
├── repository/   # DB 접근 인터페이스
├── entity/       # DB 테이블 매핑 클래스
└── dto/          # 데이터 전송 객체 (Request/Response)

src/main/resources/
├── static/       # 프론트엔드 리소스 (index.html)
└── application.properties # 서버 및 DB 설정 파일


## 🚀 실행 및 배포 (Execution & Deployment)

### 💻 로컬 환경 (Local Development)
로컬 개발 환경에서 프로젝트를 빌드하고 실행하는 방법입니다.

* **빌드 및 실행**
    ```bash
    ./gradlew clean bootRun
    ```
* **접속 주소**
    > [http://localhost:8080](http://localhost:8080)

---

### ☁️ AWS 클라우드 배포 (AWS EC2 Deployment)
운영 서버(Ubuntu 22.04 LTS)에 결과물을 배포하고 무중단 실행을 설정하는 단계입니다.

1.  **실행 파일(Jar) 빌드**
    ```bash
    ./gradlew clean build -x test
    ```

2.  **서버로 파일 전송 (SCP)**
    ```powershell
    # [내 컴퓨터] -> [AWS 서버] 전송
    scp -i my-key.pem build/libs/member-system-0.0.1-SNAPSHOT.jar ubuntu@43.203.227.11:/home/ubuntu/
    ```

3.  **서버 무중단 실행 (Nohup)**
    ```bash
    # [AWS 터미널] 기존 프로세스 종료 후 새 버전 실행
    ps -ef | grep java
    kill -9 [PID]
    nohup java -jar member-system-0.0.1-SNAPSHOT.jar &
    ```

4.  **배포 완료 주소**
    > **URL**: [http://43.203.227.11:8080](http://43.203.227.11:8080)