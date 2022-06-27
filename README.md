## 1. 개발 환경
- **Develop Tools :** Spring Tool Suite 4
- **Language :** Java / openjdk11
- **Project Type :** Maven
- **Packaging :** Jar
- **Spring Boot Version :** 2.7.1
- **Database :** h2database / JPAQueryDSL

## 2. DDL(MySql)
- 사용자 누적 포인트 테이블 **point**
```sql
CREATE TABLE point (
  point_id BIGINT(20) NOT NULL AUTO_INCREMENT,
  user_id VARCHAR(40) NOT NULL,
  accue_point BIGINT(20) NOT NULL,
  PRIMARY_KEY (point_id),
  INDEX idx_user_id (user_id)
);
```
- 포인트 부여 히스토리 테이블 **point_history**
```sql
CREATE TABLE point_history (
  point_history_id BIGINT(20) NOT NULL AUTO_INCREMENT,
  action VARCHAR(10) NOT NULL,
  review_id VARCHAR(40) NOT NULL,
  user_id VARCHAR(40) NOT NULL,
  place_id VARCHAR(40) NOT NULL,
  save_date_time TIMESTAMP NOT NULL,
  point BIGINT(20) NOT NULL,
  attached_count INT(11) NOT NULL,
  delete_yn CHAR(1) NOT NULL,
  PRIMARY_KEY (point_history_id),
  INDEX idx_review_id (review_id),
  INDEX idx_user_id (user_id),
  INDEX idx_place_id (place_id)
);
```

## 3. API 정보
1. (전체) 포인트 부여 히스토리 : (GET) /events
2. (개인) 포인트 부여 히스토리 : (GET) /events/{userId}
3. 사용자별 누적 포인트 조회 : (GET) /events/{userId}/points
4. 포인트 적립 : (POST) /events

## 4. 실행 방법
- **Github를 이용하는 방법**<br>
1. Spring Develop Tools에 코드 클론 받기<br>
2. 클론 받은 프로젝트 Maven build 하여 jar 파일 생성하기<br>
3. 터미널 또는 cmd 접속하기<br>
4. 생성한 jar 파일 위치로 이동하기<br>
5. **java -jar [생성한 jar 파일명].jar** 입력하여 jar 파일 실행하기<br>
6. **localhost:8080/** URL로 API 정보 호출하기<br>
- **프로젝트 jar 파일 다운로드**<br>
1. https://drive.google.com/file/d/1hn2IYM87Bs10uBu19MMYgUy-3_fbSP1Y/view?usp=sharing
2. 위 링크로 Maven build 해놓은 jar 파일 다운로드 받기<br>
3. 터미널 또는 cmd 접속하기<br>
4. 다운로드 받은 jar 파일 위치로 이동하기<br>
5. **java -jar triple-task.jar** 입력하여 jar 파일 실행하기<br>
6. **localhost:8080/** URL로 API 정보 호출하기
