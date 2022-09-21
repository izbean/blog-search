# 블로그 검색 API

## 개요

카카오 블로그 검색 API 를 통해 블로그 글을 검색 할 수 있는 기능을 제공한다. <br>
만약, 카카오 API 가 장애 상황 일 시엔 네이버 블로그 검색 API 를 통해 검색 결과를 제공 한다.

## Jar 다운로드

> https://github.com/izbean/blog-search/raw/main/jar/blog-search-0.0.1-SNAPSHOT.jar

## 사용한 외부 라이브러리
- Caffeine(https://github.com/ben-manes/caffeine)
  - 사용 목적: 로컬 캐싱

## API 명세

### URL

> GET /search

### Request

#### Parameter

| Name  | Type    | Description                                                | Required |
|-------|---------|------------------------------------------------------------|----------|
| query | String  | 검색을 원하는 질의어                                                | O        |
| sort  | String  | 결과 문서 정렬 방식, accuracy(정확도순) 또는 recency(최신순), 기본 값 accuracy | X        |
| page  | Integer | 결과 페이지 번호, 1~50 사이의 값, 기본 값 1                              | X        |
| size  | Integer | 한 페이지에 보여질 문서 수, 1~50 사이의 값, 기본 값 10                       | X        |

### Response

#### meta

| Name        | Type    | Description                                                 |
|-------------|---------|-------------------------------------------------------------|
| totalCount  | Integer | 검색된 문서 수                                                    |
| display     | Integer | 현재 표시 되는 데이터 수                                              |
| currentPage | Integer | 현재 페이지 번호                                                   |
| isEnd       | Boolean | 현재 페이지가 마지막 페이지인지 여부, 값이 false면 page를 증가시켜 다음 페이지를 요청할 수 있음 |

#### documents

| Name      | Type   | Description                                                        |
|-----------|--------|--------------------------------------------------------------------|
| title     | String | 블로그 글 제목                                                           |
| contents  | String | 블로그 글 요약                                                           |
| url       | String | 블로그 글 URL                                                          |
| blogName  | String | 블로그의 이름                                                            |
| thumbnail | String | 검색 시스템에서 추출한 대표 미리보기 이미지 URL, 미리보기 크기 및 화질은 변경될 수 있음               |
| dateTime  | String | 블로그 글 작성시간, ISO 8601 <br> [YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz] |

### Sample

#### Request

```shell
curl --location --request GET 'http://localhost:8080/search?query=이효리'
```

#### Response

```json
{
  "meta": {
    "totalCount": 369180,
    "display": 10,
    "currentPage": 1,
    "isEnd": false
  },
  "documents": [
    {
      "title": "<b>이효리</b> 효과",
      "contents": "지금의 30, 40대라면 핑클의 <b>이효리</b>를 모르시는 분들은 없을 겁니다. 그만큼 한 시대를 풍미했던 유명한 연예인인 <b>이효리</b>의 파급력에 대해서 알아보고자 합니다. 예나 지금이나 델몬트 역사상 과일주스 부동의 1위는 오렌지 주스입니다. 그런데 이게 딱 한번 뒤집힌 적이 있었는데, 바로 2003년 당시 연예계를 씹어먹던...",
      "url": "http://pronician.tistory.com/1500",
      "blogName": "Accessible Insight",
      "thumbnail": "",
      "dateTime": "2022-07-20T18:54:43.000+09:00"
    },
    ...
  ]
}
```

#### Error

```json
{
  "code": "400",
  "message": "잘못 된 요청 입니다.",
  "validation": {
    "query": "검색어는 필수 입력 값 입니다."
  }
}
```