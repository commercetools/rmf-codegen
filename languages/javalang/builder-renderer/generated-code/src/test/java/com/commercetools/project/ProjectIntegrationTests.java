package com.commercetools.project;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.Project.Project;
import com.commercetools.models.Project.ProjectChangeCountriesActionBuilder;
import com.commercetools.models.Project.ProjectUpdateAction;
import com.commercetools.models.Project.ProjectUpdateBuilder;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectIntegrationTests {
    
    @Test
    public void byKeyGet() throws Exception {
        String projectKey = CommercetoolsTestUtils.getProjectKey();
        Project project = ApiRoot.withProjectKey(projectKey)
                .get()
                .executeBlocking();
        Assertions.assertNotNull(project);
        Assertions.assertEquals(projectKey, project.getKey());
    }
    
    @Test
    public void updateProject() throws Exception {
        List<String> countries = Arrays.asList("DE");
        List<ProjectUpdateAction> updateActions = new ArrayList<>();
        updateActions.add(ProjectChangeCountriesActionBuilder.of().countries(countries).build());
        Project project = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                .post(ProjectUpdateBuilder.of().actions(updateActions).build())
                .executeBlocking();
        System.out.println(project.getCountries());
    }
    
}
