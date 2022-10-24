# telegram bot 텔레그램 봇
## 개요
- [텔레그램 bot (https://core.telegram.org/bots)](https://core.telegram.org/bots)를 쉽게 사용하기 위한 라이브러리 

## 사용
- 텔레그램 bot을 생성한다.
- 다음과 같이 동작한다.

```java
import org.junit.jupiter.api.Test;

public class Test {
    @org.junit.jupiter.api.Test
    void update(){
        TelegramBot bot = DefaultTelegramBotFactory.init("input your telegram bot token");
        bot.send();
        bot.update();
    }
}
```

## test
- 텔레그램과의 통신을 목표로 하기 때문에 통합테스트를 포함하였다.
- 통합테스트를 원한다면 다음과 같이 설정한다.
  - application.properties의 test-integration를 true로 변경한다.