# Spring Login Demo

이 프로젝트는 JWT 로그인만 제공하는 Spring Boot 백엔드입니다.

## Railway로 배포하기

### 1. GitHub에 코드 업로드
1. `git init`
2. `.gitignore` 추가 확인
3. `git add .`
4. `git commit -m "Initial commit"
5. GitHub에 푸시

### 2. Railway에 연결
1. Railway에 로그인
2. 새 프로젝트 생성
3. GitHub repo 연결
4. `Dockerfile` 기반 배포 선택

### 3. 환경 변수 설정
Railway에 다음 환경 변수를 추가합니다:
- `JWT_SECRET` (실제 운영용 비밀값)
- `SPRING_DATASOURCE_URL` (MySQL 사용 시)
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

### 4. 빌드 커맨드
Railway Docker 배포에서는 Dockerfile이 자동으로 사용됩니다.

### 5. 실행 포트
- 애플리케이션은 기본적으로 `8080` 포트를 사용합니다.

## 로컬 실행
```powershell
./gradle/gradle-8.10.2/bin/gradle.bat bootRun
```

## 로그인 테스트
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "test@test.com",
  "password": "1234"
}
```
