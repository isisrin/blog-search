# 모듈 별 설명
blog-api - Controller Layer를 담당하는 모듈  

blog-core - Service, Repository Layer를 담당하며 시스템에서 공통적으로 사용할 로직들을 담당하는 모듈

blog-scheduler - 주기적으로 실행되는 배치를 담당하는 모듈

# 사용한 외부 오픈소스
## 1. [Redis](https://github.com/redis/redis)
### 사용 목적
- 인기 검색어 목록을 구현하기 위하여 사용 ([sorted-sets](https://redis.io/docs/data-types/sorted-sets/))
- 간단한 분산락을 구현하기 위하여 사용

## 2. [Caffeine](https://github.com/ben-manes/caffeine)
- 로컬 캐시를 구현하기 위하여 사용

## 3. [Mapstruct](https://github.com/mapstruct/mapstruct)
- 다양한 Object의 매핑을 위하여 사용
- 리플렉션 없이 매핑 코드를 auto-generate 해주기 때문에 편의성 및 성능상의 이점을 가져갈 수 있음

# 실행방법
## 1. Redis 설치 및 실행
- Redis는 기본포트인 6379 포트를 그대로 사용하도록 하였습니다.
```shell
# docker
docker pull redis && docker run -d -p 6379:6379 --name redis redis
# homebrew
bree install redis
brew services start redis
```
## 2. Application 실행
```shell
java -jar blog-api.jar
```
# API 호출 예시
```shell
# 블로그 목록 조회 (최신순)
curl --location --request GET 'http://localhost:8080/blogs/search?query=hi&page=2&size=10&sort=LATEST'
# 블로그 목록 조회 (정확도순)
curl --location --request GET 'http://localhost:8080/blogs/search?query=hi&page=2&size=10&sort=ACCURACY'
# 인기 검색어 목록 조회
curl --location --request GET 'http://localhost:8080/blogs/popular-search-words'
```
