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

- [x] OrderService — 주문 목록 조회 N+1 → IN 절 배치 조회로 개선
- [x] ReviewListService — 리뷰 이미지 N+1 → EntityGraph 활용으로 개선
- [x] CartService — 루프 내 개별 조회 → IN 절 일괄 조회로 개선
- [x] OrderService — createOrder() 내 Member 이중 조회 제거
- [x] MemberService — existsByEmail() + findByEmail() 이중 조회 → 단일 조회로 개선
- [x] MileageService — 불필요한 Member/Order 엔티티 선조회 제거
- [x] ReviewListService — checkPurchaseStatus() N+1 체인 + 로직 버그 수정
- [x] QnAListService — checkWritingStatus() 전체 조회 + Long 비교 버그 수정
- [x] ItemService — getOne() 연관 데이터 분리 조회 → EntityGraph 통합 조회로 개선

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

---

## 🔧 리팩토링 상세 내역

> 서비스 레이어의 DB 접근 비효율 패턴 5가지 유형을 분석하고 개선하였습니다.

---

### 유형 1. N+1 쿼리 문제

루프 안에서 DB를 반복 호출하는 패턴입니다.
목록 N건을 조회한 뒤 각 건마다 연관 데이터를 개별 조회하여 총 N+1번의 쿼리가 발생합니다.

#### 1-1. OrderServiceImpl — 주문 목록 조회 (7개 메서드)

| 항목 | 내용 |
|------|------|
| **대상 메서드** | `findAllOrders`, `findAllByMemberId`(List/Page), `findByDuringPeriod`(List/Page), `findByDuringPeriodFromMemberId`(List/Page) |
| **문제** | 주문 목록 조회 후 루프 안에서 각 주문마다 `orderItemRepository.findByOrderId()` 개별 호출 |
| **쿼리 수** | 페이지 20건 → **21번** |

```java
// 개선 전
return orderPage.map(order -> {
    List<OrderItem> items = orderItemRepository.findByOrderId(order.getId()); // 주문 수만큼 반복
    return new OrderDTO(order, items);
});

// 개선 후
List<Long> orderIds = orderPage.getContent().stream().map(Order::getId).collect(toList());
Map<Long, List<OrderItem>> itemsByOrderId = orderItemRepository.findByOrderIds(orderIds) // IN 절 1번
        .stream().collect(groupingBy(oi -> oi.getOrder().getId()));
return orderPage.map(order -> new OrderDTO(order, itemsByOrderId.getOrDefault(order.getId(), List.of())));
```

**추가된 메서드 — `OrderItemRepository`**
```java
@Query("SELECT oi FROM OrderItem oi WHERE oi.order.id IN :orderIds ORDER BY oi.id DESC")
List<OrderItem> findByOrderIds(@Param("orderIds") List<Long> orderIds);
```

| | 개선 전 | 개선 후 |
|--|---------|---------|
| 쿼리 수 (20건) | 21번 | **2번** |
| 쿼리 수 (50건) | 51번 | **2번** |

---

#### 1-2. ReviewListServiceImpl — 리뷰 이미지 조회 (4개 메서드)

| 항목 | 내용 |
|------|------|
| **대상 메서드** | `getReviewListPage`, `getReviewListPageWithDelFlag`, `getReviewListPageByItemId`, `getReviewListPageByItemIdWithDelFlag` |
| **문제** | `ReviewListRepository`의 모든 쿼리에 `@EntityGraph(attributePaths = "images")`가 이미 적용되어 이미지가 함께 로딩됨에도 불구하고, 서비스 레이어에서 `reviewImageRepository.findAllByReviewId()`를 루프 안에서 재호출 |

```java
// 개선 전
return reviewListPage.map(review -> {
    List<ReviewImage> reviewImageList = reviewImageRepository.findAllByReviewId(review.getId()); // 이미 로딩된 데이터를 또 조회
    return new ReviewListDTO(reviewImageList, review);
});

// 개선 후
return reviewListPage.map(review -> new ReviewListDTO(review.getImages(), review)); // 메모리에서 바로 사용
```

| | 개선 전 | 개선 후 |
|--|---------|---------|
| 쿼리 수 (20건) | 21번 | **1번** |

---

#### 1-3. CartServiceImpl — 장바구니 다중 조회 (4개 메서드)

| 항목 | 내용 |
|------|------|
| **대상 메서드** | `getCartListByMemberIdANDOptionId`, `getCartListByMemberIdANDOptionIdAndCheckItem`, `editCartListByMemberIdANDOptionId`, `multipleDeleteItemFromWishList` |
| **문제** | 선택된 옵션 ID 목록을 루프 안에서 `findByMemberIdAndOptionId()` 단건 반복 호출 |

```java
// 개선 전
for (Long cartItemId : cartDTO.getSelectId()) {
    Cart cart = cartRepository.findByMemberIdAndOptionId(member.getId(), cartItemId); // N번 반복
    cartList.add(cart);
}

// 개선 후
List<Long> optionIds = Arrays.asList(cartDTO.getSelectId());
List<Cart> cartList = cartRepository.findByMemberIdAndOptionIds(member.getId(), optionIds); // 1번
```

**추가된 메서드 — `CartRepository`**
```java
@Query("SELECT c FROM Cart c WHERE c.member.id = :memberId AND c.itemOption.id IN :optionIds ORDER BY c.id DESC")
List<Cart> findByMemberIdAndOptionIds(@Param("memberId") Long memberId, @Param("optionIds") List<Long> optionIds);

@Query("SELECT c FROM Cart c WHERE c.member.id = :memberId AND c.itemOption.id IN :optionIds AND c.checkItem = true ORDER BY c.id DESC")
List<Cart> findByMemberIdAndOptionIdsAndCheckItem(@Param("memberId") Long memberId, @Param("optionIds") List<Long> optionIds);
```

| | 개선 전 | 개선 후 |
|--|---------|---------|
| 쿼리 수 (선택 5건) | 5번 | **1번** |

---

### 유형 2. 이미 있는 데이터를 또 조회

메모리에 이미 올라와 있거나, 한 번의 쿼리로 해결 가능한 데이터를 DB에 재요청하는 패턴입니다.

#### 2-1. OrderServiceImpl.createOrder() — Member 이중 조회

```java
// 개선 전
Member member = memberRepository.findById(orderDTO.getMemberId())...;  // 1번째 조회 (42번째 줄)
// ...
Member memberMileage = memberRepository.findById(orderDTO.getMemberId())...;  // 2번째 조회 (140번째 줄, 완전히 동일)
if (orderDTO.getUsingMileage() <= memberMileage.getStockMileage()) { ... }

// 개선 후
Member member = memberRepository.findById(orderDTO.getMemberId())...;  // 1번만 조회
// ...
if (orderDTO.getUsingMileage() <= member.getStockMileage()) { ... }   // 기존 변수 재사용
```

#### 2-2. MemberServiceImpl.updateMember() — 존재 확인 후 즉시 재조회

```java
// 개선 전
if (!existsByEmail(memberDTO.getEmail())) {               // SELECT COUNT(*) ... 1번째 쿼리
    throw new RuntimeException("회원을 찾을 수 없습니다.");
}
Member searchMember = memberRepository.findByEmail(...);  // SELECT * ... 2번째 쿼리 (동일 조건)

// 개선 후
Member searchMember = memberRepository.findByEmail(...);  // SELECT * ... 1번만
if (searchMember == null) {
    throw new RuntimeException("회원을 찾을 수 없습니다.");
}
```

---

### 유형 3. 필요 없는 엔티티 선조회

실제로 필요한 필드는 ID 하나뿐인데 엔티티 전체를 DB에서 불러오는 패턴입니다.

#### 3-1. MileageServiceImpl — Member/Order 선조회 (6개 메서드)

| 항목 | 내용 |
|------|------|
| **대상 메서드** | `findAllByMemberId`(List/Page), `findAllByMemberEmail`(List/Page), `findAllByOrderId`(List/Page) |
| **문제** | `member.getId()`가 파라미터 `memberId`와 동일한 값임에도 Member 엔티티 전체를 먼저 조회 |

```java
// 개선 전
Member member = memberRepository.findById(memberId)...;  // 불필요한 선조회
List<Mileage> mileageList = mileageRepository.findAllByMemberId(member.getId()); // 결국 memberId와 동일

// 개선 후
List<Mileage> mileageList = mileageRepository.findAllByMemberId(memberId); // 바로 사용
```

`findAllByMemberEmail`의 경우 email → ID 변환 과정이 필요했으므로, `MileageRepository`에 이메일 직접 조회 쿼리를 추가하여 Member 조회를 완전히 제거하였습니다.

**추가된 메서드 — `MileageRepository`**
```java
@Query("SELECT m FROM Mileage m WHERE m.member.email = :email ORDER BY m.id DESC")
List<Mileage> findAllByMemberEmail(@Param("email") String email);
```

| | 개선 전 | 개선 후 |
|--|---------|---------|
| 쿼리 수 (메서드당) | 2번 | **1번** |
| 개선 메서드 수 | — | 6개 |

---

### 유형 4. 전체 조회 후 루프 탐색 (+ 버그 수정)

존재 여부 하나를 확인하기 위해 전체 목록을 가져온 뒤 Java에서 루프를 도는 패턴입니다.
두 케이스 모두 **로직 버그**가 함께 존재하였습니다.

#### 4-1. ReviewListServiceImpl.checkPurchaseStatus() — 3가지 문제

| 문제 | 내용 |
|------|------|
| N+1 체인 | `orderService.findAllByMemberId()` 호출 → 내부에서 N+1 발생 |
| 전체 조회 | 구매 여부 확인을 위해 해당 회원의 주문 전체를 가져옴 |
| **로직 버그** | `listIndex`가 항상 0으로 고정되어 각 주문의 **첫 번째 상품만** 확인, 두 번째 이후 상품은 영원히 구매 확인 불가 |

```java
// 개선 전 (버그 포함)
List<OrderDTO> orderDTO = orderService.findAllByMemberId(memberId); // 전체 주문 조회 + N+1 유발
for (OrderDTO targetOrder : orderDTO) {
    if (targetOrder.getOrderItemList().get(listIndex).getItemId() == itemId) { // listIndex 항상 0
        checkStatus = true;
    }
}

// 개선 후 (단일 EXISTS 쿼리)
return orderService.existsPurchase(memberId, itemId);
```

**추가된 메서드 — `OrderItemRepository`**
```java
@Query("SELECT COUNT(oi) > 0 FROM OrderItem oi WHERE oi.order.member.id = :memberId AND oi.item.id = :itemId AND oi.order.delFlag = false")
boolean existsByMemberIdAndItemId(@Param("memberId") Long memberId, @Param("itemId") Long itemId);
```

---

#### 4-2. QnAListServiceImpl.checkWritingStatus() — 2가지 문제

| 문제 | 내용 |
|------|------|
| 전체 조회 | 작성자 확인을 위해 회원의 QnA 전체 목록을 가져온 뒤 루프 탐색 |
| **Long 비교 버그** | `targetQnAList.getId() == qnaListId` — Java에서 `Long` 객체는 -128 ~ 127 범위만 캐싱되므로 **ID가 128 이상이면 `==` 비교가 항상 `false`** → 작성자임에도 "권한 없음" 오류 발생 |

```java
// 개선 전 (버그 포함)
List<QnAList> qnAList = qnAListRepository.findAllByMemberId(memberId); // 전체 조회
for (QnAList targetQnAList : qnAList) {
    if (targetQnAList.getId() == qnaListId) { // Long == 버그
        checkStatus = true;
    }
}

// 개선 후 (단일 EXISTS 쿼리 + 버그 해결)
return qnAListRepository.existsByMemberIdAndQnaListId(memberId, qnaListId);
```

**추가된 메서드 — `QnAListRepository`**
```java
@Query("SELECT COUNT(ql) > 0 FROM QnAList ql WHERE ql.member.id = :memberId AND ql.id = :qnaListId")
boolean existsByMemberIdAndQnaListId(@Param("memberId") Long memberId, @Param("qnaListId") Long qnaListId);
```

---

### 유형 5. 연관 데이터 분리 조회

한 번에 가져올 수 있는 연관 데이터를 각각 별도 쿼리로 조회하는 패턴입니다.

#### 5-1. ItemServiceImpl.getOne() — 4번 분리 조회

| 항목 | 내용 |
|------|------|
| **문제** | 상품 1건 조회 시 기본 정보, 이미지, 옵션, 인포를 각각 별도 쿼리로 총 4번 조회 |

```java
// 개선 전 (4번 쿼리)
Item item = itemRepository.findById(id)...;                        // 쿼리 1
List<ItemImage> images = itemImageRepository.findByItemId(id);     // 쿼리 2
List<ItemOption> options = itemOptionRepository.findByItemId(id);  // 쿼리 3
return new ItemDTO(item, images, options, item.getInfo());          // 쿼리 4 (지연 로딩)

// 개선 후 (EntityGraph로 통합)
Item item = itemRepository.findWithDetailsById(id)...;
return new ItemDTO(item, item.getImages(), item.getOptions(), item.getInfo()); // 추가 조회 없음
```

**추가된 메서드 — `ItemRepository`**
```java
@EntityGraph(attributePaths = {"images", "options", "info"})
@Query("SELECT i FROM Item i WHERE i.id = :id")
Optional<Item> findWithDetailsById(@Param("id") Long id);
```

| | 개선 전 | 개선 후 |
|--|---------|---------|
| 쿼리 수 | 4번 | **1~3번** |

---

### 전체 변경 파일 목록

| 파일 | 변경 유형 |
|------|----------|
| `OrderItemRepository.java` | 메서드 2개 추가 (`findByOrderIds`, `existsByMemberIdAndItemId`) |
| `OrderRepository.java` | — |
| `OrderService.java` | `existsPurchase()` 인터페이스 추가 |
| `OrderServiceImpl.java` | 9개 메서드 수정, `existsPurchase()` 구현 추가 |
| `CartRepository.java` | 메서드 2개 추가 (`findByMemberIdAndOptionIds`, `findByMemberIdAndOptionIdsAndCheckItem`) |
| `CartServiceImpl.java` | 4개 메서드 수정 |
| `ReviewListServiceImpl.java` | 5개 메서드 수정, import 정리 |
| `ReviewImageRepository.java` | — |
| `ReviewListRepository.java` | — |
| `MileageRepository.java` | 메서드 2개 추가 (`findAllByMemberEmail` List/Page) |
| `MileageServiceImpl.java` | 6개 메서드 수정 |
| `MemberServiceImpl.java` | `updateMember()` 수정 |
| `QnAListRepository.java` | 메서드 1개 추가 (`existsByMemberIdAndQnaListId`) |
| `QnAListServiceImpl.java` | `checkWritingStatus()` 수정, import 정리 |
| `ItemRepository.java` | 메서드 1개 추가 (`findWithDetailsById`) |
| `ItemServiceImpl.java` | `getOne()` 수정 |

---

## 🐛 트러블슈팅

> 리팩토링 진행 중 추가 예정

---

## 📝 라이선스

This project is licensed under the MIT License.
