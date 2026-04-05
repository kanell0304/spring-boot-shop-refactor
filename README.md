# 🛍️ Spring Boot 쇼핑몰 (리팩토링)

### **"React + Spring Boot 기반 온라인 의류 쇼핑몰"**

---

## 📎 원본 프로젝트

> 이 레포지토리는 아래 원본 프로젝트를 기반으로 **N+1 쿼리 문제 개선을 목적으로 리팩토링**한 버전입니다.

| 구분 | 링크 |
| --- | --- |
| **원본 프로젝트** | https://github.com/heungsu89/shop_project |
| **리팩토링 버전 (현재)** | 이 레포지토리 |

### 리팩토링 목적

기존 코드에서 서비스 메서드마다 루프 내부에서 개별 DB 조회를 반복하는 N+1 문제가 발견되었습니다.
예를 들어 장바구니 다중 삭제 시 삭제 대상 수만큼 SELECT 쿼리가 발생하고,
주문 목록 조회 시 주문 수만큼 주문 상품 조회 쿼리가 추가로 발생하는 구조였습니다.
이를 `IN` 절 일괄 조회 및 `JOIN FETCH`로 개선하여 불필요한 DB 호출을 줄이는 것이 목표입니다.

### 리팩토링 진행 상황

- [ ] CartService — 루프 내 개별 조회 → IN 절 일괄 조회
- [ ] OrderService — 주문 목록 조회 N+1 → JOIN FETCH 개선
- [ ] 트러블슈팅 내용 추가 예정

---

## 📌 프로젝트 소개

React와 Spring Boot를 활용한 온라인 의류 쇼핑몰입니다.
상품 조회, 장바구니, 주문, 배송, 마일리지, 위시리스트 등 실제 쇼핑몰의 주요 기능을 구현하였습니다.

### 주요 기능

| 기능 | 설명 |
| --- | --- |
| 상품 관리 | 상품 목록/상세 조회, 카테고리 분류, 옵션별 재고 관리 |
| 장바구니 | 상품 담기, 수량 변경, 선택 삭제, 전체 삭제 |
| 주문 | 선택 상품 주문, 전체 장바구니 주문, 결제 방법 선택 (카드/무통장) |
| 배송 | 배송 상태 관리 (준비중/배송중/배송완료) |
| 마일리지 | 회원 등급별 마일리지 자동 적립 및 사용 |
| 위시리스트 | 관심 상품 저장 및 관리 |
| 회원 | 회원가입, 로그인, 소셜 로그인, 회원 정보 수정 |
| 관리자 | 상품/주문/회원/이벤트/매거진 관리 |
| 검색 | 상품 검색 기능 |
| 리뷰/QnA | 상품 리뷰 및 문의 등록 |

---

## 🛠️ 기술 스택

| 분류 | 기술 |
| --- | --- |
| **Backend** | Spring Boot 3.4.3, Java 17 |
| **ORM** | Spring Data JPA, Hibernate |
| **Database** | MySQL |
| **인증/보안** | Spring Security, JWT |
| **빌드** | Gradle |
| **Frontend** | React, Vite |
| **스타일링** | SCSS |
| **기타** | Lombok, ModelMapper, Thumbnailator |

---

## 🏗️ 프로젝트 구조

```
shop_project/
├── backend/shop/
│   └── src/main/java/com/shop/shop/
│       ├── config/          # Security, CORS 설정
│       ├── controller/      # REST API 컨트롤러
│       ├── domain/          # JPA 엔티티
│       │   ├── cart/        # 장바구니, 위시리스트
│       │   ├── item/        # 상품, 옵션, 이미지
│       │   ├── member/      # 회원, 마일리지
│       │   ├── order/       # 주문, 주문상품
│       │   ├── delivery/    # 배송
│       │   ├── category/    # 카테고리
│       │   └── list/        # 이벤트, 매거진, 리뷰, QnA
│       ├── dto/             # DTO 클래스
│       ├── repository/      # JPA 레포지토리
│       ├── service/         # 비즈니스 로직
│       ├── security/        # JWT 필터, 인증 핸들러
│       ├── exception/       # 전역 예외 처리
│       └── util/            # JWT, 파일 유틸리티
└── frontend/shop/
    └── src/
```

---

## 🚀 실행 방법

### 환경 요구사항

- Java 17
- MySQL 8.0 이상
- Node.js 18.x 이상

### Backend 실행

```bash
# 1. 저장소 클론
git clone <repository_url>

# 2. application.properties 또는 application.yml 설정
# src/main/resources/ 경로에 DB 접속 정보 입력
spring.datasource.url=jdbc:mysql://localhost:3306/<your_db>
spring.datasource.username=<your_username>
spring.datasource.password=<your_password>

# 3. 빌드 및 실행
cd backend/shop
./gradlew bootRun
```

### Frontend 실행

```bash
cd frontend/shop
npm install
npm run dev
```

---

## 👥 팀 구성

**개발 기간**: 2025.03 ~ 2025.06

| 이름 | 역할 | 담당 |
| --- | --- | --- |
| 최흥수 | **팀장, Frontend** | 프론트엔드 전반, 프로젝트 관리 |
| 이경준 | Backend | DB 설계, 백엔드 전체 구현 |
| 강지석 | Frontend | 프론트엔드 일부, PPT, 사이트 정책 |

---

## 🐛 트러블슈팅

> 리팩토링 진행 중 추가 예정

---

## 📝 라이선스

This project is licensed under the MIT License.
