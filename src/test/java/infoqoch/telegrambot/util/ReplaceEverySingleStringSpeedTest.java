package infoqoch.telegrambot.util;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class ReplaceEverySingleStringSpeedTest {

    Map<String, Integer> compareMap;

    @Test
    @Disabled // 테스트 완료하였고, 빠른 방법을 채택함.
    void compare_speed(){
        compareMap = new HashMap<>();
        compareMap.put("replace", 0);
        compareMap.put("sb", 0);

        for(int i=0; i<10000; i++){
            assertAndCompareTime("_*][()~`>#+-="
                    + UUID.randomUUID().toString()
                    +"|{}.!_*][()~`>#+-=|{}.!_*][()~`>#+-=|{}.!_*][()~`>#+-=|{}.!_*][()~`>#+-=|{}.!_*][()~`>#+-=|{}.!_*][()~`>#+-=|{}.!_*][()~`>#+-=|{}.!_*][()~`>#+-=|{}.!_*][()~`>#+-=|{}.!_*][()~`>#+-=|{}.!");
            assertAndCompareTime("#안녕하세요**!"
                    + UUID.randomUUID().toString()
                    + " 저는 정말로 행복??한가요?헤헤..주말이 빨리!! 왔으면$$ 좋겠#다!!#안녕하세요**! 저는 정말로 행복??한가요?헤헤..주말이 빨리!! 왔으면$$ 좋겠#다!!#안녕하세요**! 저는 정말로 행복??한가요?헤헤..주말이 빨리!! 왔으면$$ 좋겠#다!!#안녕하세요**! 저는 정말로 행복??한가요?헤헤..주말이 빨리!! 왔으면$$ 좋겠#다!!");
        }

        for(int i=0; i<10000; i++){
            reverse_assertAndCompareTime("_*][()~`>#+-="
                    + UUID.randomUUID().toString()
                    +"|{}.!_*][()~`>#+-=|{}.!_*][()~`>#+-=|{}.!_*][()~`>#+-=|{}.!_*][()~`>#+-=|{}.!_*][()~`>#+-=|{}.!_*][()~`>#+-=|{}.!_*][()~`>#+-=|{}.!_*][()~`>#+-=|{}.!_*][()~`>#+-=|{}.!_*][()~`>#+-=|{}.!");
            reverse_assertAndCompareTime("#안녕하세요**!"
                    + UUID.randomUUID().toString()
                    + " 저는 정말로 행복??한가요?헤헤..주말이 빨리!! 왔으면$$ 좋겠#다!!#안녕하세요**! 저는 정말로 행복??한가요?헤헤..주말이 빨리!! 왔으면$$ 좋겠#다!!#안녕하세요**! 저는 정말로 행복??한가요?헤헤..주말이 빨리!! 왔으면$$ 좋겠#다!!#안녕하세요**! 저는 정말로 행복??한가요?헤헤..주말이 빨리!! 왔으면$$ 좋겠#다!!");
        }

        System.out.println("compareMap = " + compareMap); // 느린 것이 많다.
    }

    private void assertAndCompareTime(String target) {
        long a = System.currentTimeMillis();
        final String replace = escapeReplace(target);
        long b = System.currentTimeMillis();
        final String sb = escapeSB(target);
        long c = System.currentTimeMillis();
        assertThat(replace).isEqualTo(sb);

        long dif = (b-a)-(c-b);
        final String result = dif > 0 ? "replace" : "sb";
        // System.out.println("오래 걸린 것" + result + "시간 : " + dif);

        compareMap.put(result, compareMap.get(result)+1);
    }

    private void reverse_assertAndCompareTime(String target) {
        long a = System.currentTimeMillis();
        final String sb = escapeSB(target);
        long b = System.currentTimeMillis();
        final String replace = escapeReplace(target);
        long c = System.currentTimeMillis();
        assertThat(replace).isEqualTo(sb);

        long dif = (b-a)-(c-b);
        final String result = dif > 0 ? "sb" : "replace";
        // System.out.println("오래 걸린 것" + result + "시간 : " + dif);

        compareMap.put(result, compareMap.get(result)+1);
    }

    private String escapeReplace(String str) {
        str = str.replaceAll("[_]", "\\\\_");
        str = str.replaceAll("[\\*]", "\\\\*");
        str = str.replaceAll("[\\]]", "\\\\]");
        str = str.replaceAll("[\\[]", "\\\\[");
        str = str.replaceAll("[\\(]", "\\\\(");
        str = str.replaceAll("[\\)]", "\\\\)");
        str = str.replaceAll("[~]", "\\\\~");
        str = str.replaceAll("[`]", "\\\\`");
        str = str.replaceAll("[>]", "\\\\>");
        str = str.replaceAll("[#]", "\\\\#");
        str = str.replaceAll("[\\+]", "\\\\+");
        str = str.replaceAll("[-]", "\\\\-");
        str = str.replaceAll("[=]", "\\\\=");
        str = str.replaceAll("[\\|]", "\\\\|");
        str = str.replaceAll("[{]", "\\\\{");
        str = str.replaceAll("[}]", "\\\\}");
        str = str.replaceAll("[\\.]", "\\\\.");
        str = str.replaceAll("[!]", "\\\\!");
        return str;
    }

    private final String REGEX = "^[_\\*\\]\\[()~`>#\\+\\-=\\|{}\\.!]$";

    private String escapeSB(String str) {
        StringBuilder result = new StringBuilder();
        for(String s : str.split("")){
            if (Pattern.matches(REGEX, s)) {
                result.append("\\" + s);
            } else {
                result.append(s);
            }
        }
        return result.toString();
    }

}
