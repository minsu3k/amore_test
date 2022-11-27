# 제품 주문 생산 관리 구현 과제

## Table of contents
* [프로젝트 설명](#프로젝트-설명)
* [기술 스펙](#기술-스펙)
* [패키지 구조](#패키지-구조)
* [빌드 및 실행](#빌드-및-실행)

## 프로젝트 설명
- 고객의 주문에 따라 생산 수량 및 순서를 관리하는 REST API 형태의 프로그램
- 타이머 쓰레드와 공장 쓰레드를 이용하여 Queue에 담긴 주문을 처리합니다.
## 기술 스펙
- Java 8
- SpringBoot 2.7.5
- Swagger-ui 3.0
- Lombok
- H2 MEM DB

## 패키지 구조

```
src
├── config
├── controller
├── dto
│   ├── request
│   └── response
├── entity
├── exception
├── mapper
├── model
├── schedule
├── service
└── util
resourses
├── mapper
└── sql
```
- src
  - config : Spring Configuration용 패키지   
  - controller : API를 받는 컨트롤러 패키지   
  - dto : request, response 등의 Data Transfer Object 패키지   
  - entity : DB에 대응되는 태이블 클래스 패키지   
  - exception : Exception 및 ErrorCode 정의 패키지   
  - mapper : DB관련 Interface 패키지   
  - model : ENUM 패키지   
  - schedule : 스케쥴 패키지   
  - service : 비즈니스 로직을 처리하는 클래스 패키지   
  - util : 시간 및 공장 설비 관리 등 util 관련 클래스 패키지   
- resources   
  - mapper : DB관련 xml 패키지   
  - sql : TABLE SCHEMA 와 INIT SQL문이 있습니다.   


## 빌드 및 실행
- Gradle을 이용하여 빌드 및 실행합니다.
- 프로젝트 설치 경로로 이동
- cmd 창에서 gradlew.bat 실행

```
ex : gradlew build (window)   
./gradlew build (Linux)
```

