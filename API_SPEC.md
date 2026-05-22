# Auth API 명세서

> Base URL: `https://spring-login-demo-production.up.railway.app`  
> 모든 요청/응답의 Content-Type은 `application/json`

---

## 인증 방식

로그인 성공 후 발급되는 **JWT 토큰**을 사용합니다.  
인증이 필요한 모든 API 요청 헤더에 아래와 같이 포함하세요.

```
Authorization: Bearer <accessToken>
```

---

## 엔드포인트

### 1. 로그인

| 항목 | 내용 |
|------|------|
| Method | `POST` |
| URL | `/api/auth/login` |
| 인증 필요 | 없음 |

#### Request Body

```json
{
  "email": "test@test.com",
  "password": "password"
}
```

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `email` | String | Y | 사용자 이메일 |
| `password` | String | Y | 비밀번호 (평문) |

#### Response (200 OK)

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer"
}
```

| 필드 | 타입 | 설명 |
|------|------|------|
| `accessToken` | String | JWT 액세스 토큰 |
| `tokenType` | String | 토큰 타입 (항상 `"Bearer"`) |

#### JWT 토큰 Payload

발급된 토큰을 디코딩하면 아래 정보가 포함됩니다.

```json
{
  "sub": "test@test.com",
  "userId": 1,
  "email": "test@test.com",
  "role": "ROLE_USER",
  "iat": 1716345600,
  "exp": 1716349200
}
```

| 필드 | 설명 |
|------|------|
| `sub` | 이메일 (subject) |
| `userId` | 사용자 ID |
| `email` | 이메일 |
| `role` | 권한 (`ROLE_USER`) |
| `iat` | 발급 시각 (Unix timestamp) |
| `exp` | 만료 시각 (발급 후 **1시간**) |

---

## 오류 응답

모든 오류는 아래 형식으로 반환됩니다.

```json
{
  "error": "오류 메시지"
}
```

| HTTP 상태 | 발생 상황 | error 메시지 |
|-----------|-----------|-------------|
| `401 Unauthorized` | 이메일 또는 비밀번호 불일치 | `"Invalid email or password"` |
| `401 Unauthorized` | JWT 토큰이 유효하지 않거나 만료됨 | `"Invalid or expired JWT token"` |
| `403 Forbidden` | 인증 헤더 없이 보호된 API 접근 | (Spring Security 기본 응답) |

---

## 사용 예시

### 로그인 요청

```bash
curl -X POST https://spring-login-demo-production.up.railway.app/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@test.com","password":"password"}'
```

### 인증이 필요한 API 요청

```bash
curl https://spring-login-demo-production.up.railway.app/api/some-endpoint \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

### JavaScript (fetch)

```javascript
// 로그인
const res = await fetch('https://spring-login-demo-production.up.railway.app/api/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ email: 'test@test.com', password: 'password' })
});
const { accessToken, tokenType } = await res.json();

// 인증 필요 API 호출
const data = await fetch('https://spring-login-demo-production.up.railway.app/api/some-endpoint', {
  headers: { 'Authorization': `${tokenType} ${accessToken}` }
});
```

---

## 테스트 계정

| 이메일 | 비밀번호 | 권한 |
|--------|----------|------|
| `test@test.com` | `password` | `ROLE_USER` |

> 운영 환경에서는 반드시 비밀번호를 변경하세요.

---

## 참고 사항

- 토큰 유효시간: **1시간** (만료 후 재로그인 필요)
- 서버는 **Stateless** 방식으로 세션을 저장하지 않습니다
- 현재 DB: H2 인메모리 (서버 재시작 시 데이터 초기화됨)
- CORS 설정이 필요한 경우 백엔드에 도메인 추가 요청
