# telegram bot 텔레그램 봇
## 개요
- [텔레그램 bot (https://core.telegram.org/bots)](https://core.telegram.org/bots)를 쉽게 사용하기 위한 라이브러리

## 사용
- 의존성을 다운로드 받는다.

```text
// 그래들
implementation 'io.github.infoqoch:telegram-bot:0.2.4'

// 메이븐
<dependency>
  <groupId>io.github.infoqoch</groupId>
  <artifactId>telegram-bot</artifactId>
  <version>0.2.3</version>
</dependency>
```

- DefaultTelegramBotFactory#init을 사용한다.
- 텔레그램 봇에서 발급받은 토큰을 입력한다. 
- 기타 자세한 설정이 필요할 경우 구현한다.

```java
public class Test {
    TelegramBot bot = DefaultTelegramBotFactory.init("input your telegram bot token");
    
    void update(){
        TelegramUpdate update = bot.update();
        Response<List<Update>> response = update.get(0L);
    }
}
```

## 지원 기능
### getUpdate -> TelegramUpdate
- 클라이언트가 작성한 채팅 메시지를 수거한다.
- long-polling으로 동작한다. 최대 값은 60초 이다.

### send -> TelegramSend
- sendMessage : 특정 클라이언트에 메시지를 보낸다.
- sendDocument : 특정 클라이언트에 파일을 보낸다.

### getFile -> TelegramFile
- Document의 file_id를 기준으로 텔레그램 클라우드의 실제 파일 주소(file_path)의 일부를 가져온다.
- 파일의 url의 형태는 다음과 같다 : https://api.telegram.org/file/bot{token}/{filePath}

### 문자열 포맷 : 마크다운
- 텔레그램이 제공하는 포맷 중 마크다운을 사용한다.
- MarkdownStringBuilder 객체로 추상화하였다.

## 테스트
- 기본적으로 유닛테스트만 동작한다.
- 텔레그램과의 정상적인 통신을 보장하기 위하여 통합테스트를 포함한다. 통합 테스트는 ***IntegrationTest 형태의 이름을 가진다. 
- 테스트에 대한 구체적인 설정은 /test/resources/application.properties 를 참고한다.

# 구현 프로젝트
## telegram-framwork
- [텔레그램프레임워크 telegram-framework : https://github.com/infoqoch/telegram-framework](https://github.com/infoqoch/telegram-framework)
- telegram-bot을 확장하여 annotation 기반의 쉬운 확장이 가능한 프레임워크를 제작

## dictionary-bot
- telegram-framework를 기반으로 구현한 사전봇
- 현재 텔레그램의 채널로 동작 중 : @custom_dictionary_bot  

## version
- 0.2.3 메이븐 리포지토리 최초 배포
- 0.2.4 자바 8 버전으로 컴파일