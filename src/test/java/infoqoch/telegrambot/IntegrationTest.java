package infoqoch.telegrambot;

public class IntegrationTest {
    static boolean isIntegrationTest() {
        final boolean integrationTest = PropertiesUtil.isIntegrationTest("test-integration");
        System.out.println("integrationTest = " + integrationTest);
        return integrationTest;
    }
}
