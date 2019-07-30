package client;

public class CommercetoolsClient {
    
    private static final TempClient TEMP_CLIENT = new TempClient();
    
    public static TempClient getClient() {
        return TEMP_CLIENT;
    }
}
