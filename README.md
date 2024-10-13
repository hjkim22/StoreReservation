# 🛎️ StoreReservation - 매장 테이블 예약 서비스
> 매장 예약 및 관리 시스템

## 📌 주요 기능
- 날짜/시간별 예약 관리
- 예약 상태 업데이트(예약 승인 및 사용자 도착 여부)

## 🛠️ Tech Stack
- **Language**: `Java`
- **Framework**: `Spring Boot 3.3.4`
- **Build Tool**: `Gradle 8.10.2`
- **Database**: `MySQL`
- **Security**: `Spring Security`, `JWT`
- **Libraries**:
    - `JPA` , `Security`, `Validation`, `Web`
    - `Lombok`
    - `jjwt` (JWT 처리)

## ⛓️ 프로젝트 기능
- **회원 기능**:
    - 사용자 및 관리자 회원가입/로그인 기능
    - JWT 기반의 인증 시스템
- **예약 관리**:
    - 매장별 예약 생성, 수정, 취소 기능
    - 예약 상태 확인 (예약 승인, 도착 여부 체크)
- **매장 및 리뷰 관리**:
    - 매장 정보 관리 API 제공
    - 사용자 리뷰 및 평점 시스템

**🎯 기타 세부 사항**
- **보안 강화**:
    - 향후 OAuth 2.0 등 추가 인증 방식 고려
- **테스트**:
    - `JUnit` 기반 테스트 코드 작성
    - `Spring Security` 테스트 라이브러리로 보안 기능 테스트