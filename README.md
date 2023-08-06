# wanted-pre-onboarding-backend 사전과제


## 1. 지원자
**박진영**

</br>

## 2. 애플리케이션 실행 방법

1. 프로젝트 클론
```
git clone https://github.com/jny0/wanted-pre-onboarding-backend .
```

2. application-secret.yml에 환경변수 설정
```
mv src/main/resources/application-secret.yml.default src/main/resources/application-secret.yml
vim src/main/resources/application-secret.yml
```
```
custom:
  dev_db:
    name: 'DATABASE NAME'
    username: 'DATABASE USERNAME'
    password: 'DATABASE PASSWORD'
  prod_dv:
    name: 'DATABASE NAME'
    username: 'DATABASE USERNAME'
    password: 'DATABASE PASSWORD'
  jwt:
     secretKey: "JWT SECRET_KEY"
     accessTime : "accessTime"
```

3. docker-compose.yml에 환경변수 설정
```
vim docker-compose.yml
```
```
version: "3"
services:
  app:
    image: 'jc21/nginx-proxy-manager:latest'
    restart: unless-stopped
    ports:
      - '80:80'
      - '443:443'
      - '81:81'
    environment:
      TZ: "Asia/Seoul"
      DB_MYSQL_HOST: "172.17.0.1"
      DB_MYSQL_PORT: 3306
      DB_MYSQL_USER: "your_db_username"
      DB_MYSQL_PASSWORD: "your_db_password"
      DB_MYSQL_NAME: "nginx"
    volumes:
      - ./data:/data
      - ./letsencrypt:/etc/letsencrypt
```
4. 빌드
```
./gradlew clean build
```
5. 실행
- docker-compose로 실행
```
docker compose up -d
```
- 서버 실행
```
java -jar -Dspring.profiles.active=prod build/libs/wanted-0.0.1-SNAPSHOT.jar
```
</br>

#### 엔드포인트 호출 방법
- 자세한 API 명세서는 [아래](https://github.com/jny0/wanted-pre-onboarding-backend#6-api-%EB%AA%85%EC%84%B8requestresponse-%ED%8F%AC%ED%95%A8)를 참조해주세요.

|     기능      |  HTTP METHOD  | URL                                          |
|:-----------:|:-------------:|----------------------------------------------|
|    회원가입     |     POST      | /member/join                                 |
|     로그인     |     POST      | /member/login                                |
|   게시글 등록    |     POST      | /article                                     |
|  게시글 단건 조회  |      GET      | /article/{article_id}                        |
|  게시글 목록 조회  |      GET      | /article?pageNumber={number}<br/>&pageSize={size} |
|   게시글 수정    |     PATCH     |/article/{article_id}                                     |
|   게시글 수정    |    DELETE     |/article/{article_id}                                     |

</br>


## 3. 데이터베이스 테이블 구조
![ERD.png](https://velog.velcdn.com/images/jyp1102/post/a37c9d30-0133-4e25-b8a1-e0a885da677c/image.png)
- article과 member를 ManyToOne으로 설정
- 각 테이블마다 id, createDate, modifyDate 공통적으로 포함

</br>

## 4. 구현한 API의 동작을 촬영한 데모 영상 링크
[데모 영상](https://drive.google.com/file/d/1OCxvmy0e606NpbxXn_ltvvxJgc83m1QY/view?usp=sharing)

</br>

## 5. 구현 방법 및 이유에 대한 간략한 설명
- 도메인별 응집도를 높이기 위해 도메인형 패키지 구조를 선택했습니다. 
- ResponseDTO를 사용해 응답에 공통적으로 resultCode, message, data를 포함하도록 했습니다.

</br>

**1. 회원가입 및 로그인**
- `BCryptPasswordEncoder`를 사용해 비밀번호를 암호화했습니다.
- `@Vaild` 어노테이션을 통해 회원가입 및 로그인 Request에서 이메일과 비밀번호에 대한 유효성 검사를 구현했습니다.
- `GlobalExceptionHandler`로 유효성 검사에서 예외처리를 구현했습니다.
- 회원가입 시 `MEMBER 권한을 부여하여 추후 admin등의 회원 유형이 추가될 경우 확장성을 고려했습니다.
- jjwt 라이브러리를 사용해 로그인 정보가 일치하면 accessToken을 반환하도록 구현했습니다.


</br>

**2. 게시글 CRUD**
- 게시글 등록
  - 로그인 상태에서 유효한 accessToken을 헤더에 포함하여 요청할 경우에만 가능합니다.
- 게시글 목록 조회
  - JPA를 통한 페이징 및 정렬을 구현했습니다.
  - N+1 문제를 해결하기 위해 `@EntityGraph`을 이용한 페치조인을 사용했습니다.
- 게시글 수정 및 삭제
  - 게시글을 등록한 사용자 이외에는 권한없음으로 실패하도록 구현했습니다.


</br>

## 6. API 명세(request/response 포함)
[POSTMAN API 명세서](https://documenter.getpostman.com/view/27461750/2s9XxySZ7L)

</br>

## 7. 가산점 요소
**1. 테스트**
- `MemberController`와 `ArticleController`에 대한 단위테스트를 진행했습니다.

</br>

**2. docker compose를 이용한 어플리케이션 환경 구현**
- 어플리케이션 실행방법을 참조해주세요.

</br>

**3. 클라우드 환경에서 배포**
- Naver Cloud Platform (NCP)를 통해 배포 환경을 설계했습니다.
- GitHub Webhook와 Jenkins를 활용해 CI/CD를 구축했습니다.
- URL : https://www.wanted.jny0.xyz/
![](https://velog.velcdn.com/images/jyp1102/post/6b8e1404-6dd4-492a-88c6-3720d1fd1de3/image.png)
