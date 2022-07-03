package infoqoch.telegrambot.bot.entity;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class Response<T> {
	private boolean ok;
	private T result;
	private String errorCode;
	private String description;

	public boolean emptyResult(){
		if(result instanceof List)
			if(((List) result).size()==0)
				return true;

		return false;
	}

	public void setResult(T result) {
		this.result = result;
	}
}
