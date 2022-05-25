package infoqoch.telegrambot.bot.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Photo {
    private String fileId;
    private String fileUniqueId;
    private Long fileSize;
    private int width;
    private int height;
}
