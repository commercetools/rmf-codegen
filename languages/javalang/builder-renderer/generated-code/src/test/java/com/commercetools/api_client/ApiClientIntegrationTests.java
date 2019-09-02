package com.commercetools.api_client;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.ApiClient.ApiClient;
import com.commercetools.models.ApiClient.ApiClientPagedQueryResponse;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ApiClientIntegrationTests {
    
    @Test
    public void createAndDeleteById() {
        ApiClient apiClient = ApiClientFixtures.createApiClient();
        ApiClient deletedApiClient = ApiClientFixtures.deleteApiClient(apiClient.getId());

        Assertions.assertEquals(apiClient.getId(), deletedApiClient.getId());
    }
    
    @Test
    public void getById() {
        ApiClientFixtures.withApiClient(apiClient -> {
            ApiClient queriedApiClient = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .apiClients()
                    .withId(apiClient.getId())
                    .get()
                    .executeBlocking();
            
            Assertions.assertNotNull(queriedApiClient);
            Assertions.assertEquals(queriedApiClient.getId(), apiClient.getId());
        });
    }
    
    @Test
    public void query() {
        ApiClientFixtures.withApiClient(apiClient -> {
            ApiClientPagedQueryResponse response = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .apiClients()
                    .get()
                    .addWhere("id=" + "\"" + apiClient.getId() + "\"")
                    .executeBlocking();

            Assertions.assertNotNull(response);
            Assertions.assertEquals(response.getResults().get(0).getId(), apiClient.getId());
        });
    }
}
