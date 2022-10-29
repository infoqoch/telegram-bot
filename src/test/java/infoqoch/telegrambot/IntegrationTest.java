package infoqoch.telegrambot;

public class IntegrationTest {
    static boolean isIntegrationTest() {
        final boolean integrationTest = Boolean.parseBoolean(PropertiesUtil.findProperty("test.telegram.integration"));
        System.out.println("integrationTest = " + integrationTest);
        return integrationTest;
    }
}
