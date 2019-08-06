package com.commercetools.project;

import com.commercetools.utils.CommercetoolsTestUtils;
import com.commercetools.client.ApiRoot;
import com.commercetools.models.Project.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectIntegrationTests {
    
    @Test
    public void byKeyGet() throws Exception {
        String projectKey = CommercetoolsTestUtils.getProjectKey();
        Project project = ApiRoot.withProjectKeyValue(projectKey)
                .get()
                .executeBlocking();
        Assert.assertNotNull(project);
        Assert.assertEquals(projectKey, project.getKey());
    }
    
    @Test
    public void updateProject() throws Exception {
        List<String> countries = Arrays.asList("DE");
        List<ProjectUpdateAction> updateActions = new ArrayList<>();
        updateActions.add(ProjectChangeCountriesActionBuilder.of().countries(countries).build());
        Project project = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                .post(ProjectUpdateBuilder.of().actions(updateActions).build())
                .executeBlocking();
        System.out.println(project.getCountries());
    }
    
}
