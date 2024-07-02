# 무신사 상품 관리 앱

## 1. 구현 범위에 대한 설명

1. 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API
2. 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회하는 API
3. 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회하는 API
4. 브랜드 및 상품을 추가 / 업데이트 / 삭제하는 API

## 2. 코드 빌드, 테스트, 실행 방법

### 요구사항

- Java 17 이상
- Gradle 6.8 이상
- h2 database

### Build

프로젝트를 빌드 명령은 다음과 같습니다.

```bash
./gradlew build
```

### Test

단위 테스트를 실행 명령은 다음과 같습니다.

```bash
./gradlew test
```

### Run

애플리케이션을 실행 명령은 다음과 같습니다.

```bash
./gradlew bootRun
```

## 기타 접속정보

1. H2 콘솔 접속
    - http://localhost:8080/h2-console
    - 애플리케이션이 시작될 때 H2 인메모리 데이터베이스가 data.sql 파일을 실행하여 자동으로 초기화합니다.
2. API 문서
    - http://localhost:8080/swagger-ui/index.html

## 추가 구현 내용

1. Unit test 및 Integration test 작성
    - 비즈니스 영역 테스트 커버리지 100% 수행
2. Thymeleaf를 이용한 Frontend 페이지 구현
    - 브랜드 CRUD
        - http://localhost:8080/view/brands
    - 상품 CRUD
        - http://localhost:8080/view/products
    - 최저가 브랜드 상품 가격 정보
        - http://localhost:8080/view/lowest-brand-price
    - 카테고리 별 최저 가격 및 브랜드
        - http://localhost:8080/view/lowest-prices
    - 카테고리 상품정보
        - http://localhost:8080/view/category/prices