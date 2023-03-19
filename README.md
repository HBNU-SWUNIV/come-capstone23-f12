# 한밭대학교 커뮤터공학과 F12팀

## 팀 구성원

- 20197121 정지용
- 20181605 홍현표
- 20177123 김태언

## Teamate Project Background

- ### 필요성

  1. 블로그는 개발자 뿐만 아니라 많은 사람들이 자신의 지식을 보여주고 공유하기 위해 운영하는 사람이 많다.
  2. 노션은 팀 단위 혹인 개인 단위로 문서관리, 노트 등으로 사용하기 좋은 툴이며, 많은 사람이 사용할 수 있다.

- ### 기존 해결책의 문제점

1. 노션에 개인적으로 작성한 내용을 블로그에 옮기기 위해서는 여러가지 방법이 있다.
   1. 노션 자체에 내장되어있는 내보내기 기능을 사용하는데 변경되는 부분이 생겨 일일이 수정을 해주어야 하며 매우 불안정하다.
   2. 노션 자체를 페이지로 웹에서 공유하는 방법이 있다. 하지만 이 방법은 URL이 매우 복잡해지고 SEO도 좋지않아 결과 노출이 어렵다.
   3. 마지막 방법은 프로그램을 사용하는 방법이다. [노션 - 티스토리 프로젝트]("https://github.com/jmjeon94/N2T") 현재는 버그가 많아 사용이 어렵다고 한다. 항상 프로그램을 실행시켜야 하고 어느정도 개발지식도 필요해 접근성도 떨어진다.

## System Design

- ### System Requirements

#### 주요 기능

  1. 노션 연동
      가. 자신이 작성한 노션 글 중에서 원하는 글을 선택하면 가져와서 자동으로 글을 작성.
  2. 포스팅
      나. 마크다운으로 입력하면 이를 뷰로 바꿔준다(다시 쓰기)
      다. 마크다운의 # 문법을 미리보기로 보여준다.
      라. 텍스트, 이미지 업로드
      마. 해쉬태그 기능
      바. 썸네일 기능
      사. 좋아요 기능
      아. 댓글 기능
      자. 시리즈 기능
      차. 시리즈에 등록된 글들을 모아서 볼 수 있다
      카. 포스트에서 포스트로 넘어갈 때 시리즈에 있는 글들끼리 모아서 쉽게 넘어갈 수 있게 한다.
  3. 트렌딩
      타. 특정 기간동안(한달, 한주, 일년)의 포스트중 사람들이 많이 읽은 글들을 보여주기
      파. 최신 올라온 글들을 보여주기
  4. 구독 및 피드 기능
      하. 특정 사용자의 글을 구독하여 알람을 받을 수 있음
  5. 방문 통계 기능
  6. 관리자 기능

    <br>

#### 비기능적 요구사항

- 가용성: 시스템이 가급적 서비스가 중단되지 않도록 클라우드 환경인 AWS 프리티어 플랜을 기본으로 선택하고 필요에 따라 유료 플랜으로 변경한다.
- 보안성: 악의적인 사용자를 방지하기 위해 이메일 기반 인증 및 Oauth2 회원가입을 통해 스팸 계정을 제한한다.
- 이식성: 웹 기반 서비스이기 때문에 PC, 모바일, 태블릿 등 기종에 상관없이 웹 브라우저만 있다면 이용할 수 있도록 최대한의 이식성을 제공한다.

## Case Study

- ### Description

## Conclusion

- ### OOO

## Project Outcome

- ### 2023 년 OO학술대회

## Commit Rule

### Commit Message Structure

```
type: subject

body
```

### Commit Type

| 키워드   | 설명                                                     |
| -------- | -------------------------------------------------------- |
| feat     | 새로운 기능 추가                                         |
| fix      | 버그 수정                                                |
| docs     | 문서 수정                                                |
| chore    | 코드 포맷팅, 세미콜론 누락 등 기능과 관련 없는 변경 사항 |
| refactor | 코드 리팩토링                                            |
| test     | 테스트 코드, 리팩토링 테스트 코드 추가                   |

### Subject

- 제목은 50자를 넘기지 않고, 소문자로 작성하며 마침표를 붙이지 않는다.
- 명령어로 작성한다.
- 가능한 제목으로 커밋 의도를 알 수 있도록 간결하고 명확하게 작성한다.

### Body

- 선택사항이므로 모든 커밋에 본문 내용을 작성할 필요는 없다.
- 추가 설명이 필요한 경우 작성한다.
- 72자를 넘기지 않고 제목과 구분을 위해 한 줄을 띄우고 작성한다.
