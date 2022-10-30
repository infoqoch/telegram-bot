# telegram bot 텔레그램 봇
## 개요
- [텔레그램 bot (https://core.telegram.org/bots)](https://core.telegram.org/bots)를 쉽게 사용하기 위한 라이브러리

## 사용
- DefaultTelegramBotFactory#init으로 사용한다.
- 필요할 경우 인터페이스에 맞춰 구현할 수 있다.

```java
public class Test {
    TelegramBot bot = DefaultTelegramBotFactory.init("input your telegram bot token");
    
    void update(){
        TelegramUpdate update = bot.update();
        final Response<List<Update>> response = update.get(0L);
    }
}
```

## 지원 기능
### getUpdate -> TelegramUpdate
- 클라이언트가 작성한 채팅 메시지를 수거한다.
- long-polling을 지원하며 최대 값은 60초 이다.

### send -> TelegramSend
- sendMessage : 특정 클라이언트에 메시지를 보낸다.
- sendDocument : 특정 클라이언트에 파일을 보낸다.

### getFile -> TelegramFile
- Document의 file_id를 기준으로 텔레그램 클라우드의 실제 파일 주소(file_path)의 일부를 가져온다.
- 파일의 url의 형태는 다음과 같다 : https://api.telegram.org/file/bot{token}/{filePath}

### 마크다운
- 텔레그램이 제공하는 포맷 중 텔레그램을 사용한다.
- MarkdownStringBuilder로 메시지를 작성한다.

## 테스트
- 텔레그램과의 정상적인 통신을 보장하기 위하여 통합테스트를 포함한다. 통합 테스트는 ***IntegrationTest란 이름을 가진다.
- 테스트에 대한 구체적인 설정은 /test/resources/application.properties 를 참고한다.

## dictionary-bot
- frontend controller pattern과 adapater pattern을 적용하여, 클라이언트의 채팅 메시지를 해석하는 어플리케이션을 개발하였음.
- [사전봇 dictionary-bot : https://github.com/infoqoch/dictionary-v3](https://github.com/infoqoch/dictionary-v3)