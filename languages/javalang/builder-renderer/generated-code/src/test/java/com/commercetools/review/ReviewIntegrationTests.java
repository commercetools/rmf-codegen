package com.commercetools.review;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.Review.*;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ReviewIntegrationTests {
    
    @Test
    public void createAndDelete(){
        ReviewDraft reviewDraft = ReviewDraftBuilder.of()
                .key(CommercetoolsTestUtils.randomKey())
                .title("review-title-1")
                .build();

        Review review = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                .reviews()
                .post(reviewDraft)
                .executeBlocking();

        Assertions.assertNotNull(review);
        Assertions.assertEquals(reviewDraft.getKey(), review.getKey());
        
        Review deletedReview = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                .reviews()
                .withId(review.getId())
                .delete()
                .addVersion(review.getVersion())
                .executeBlocking();

        Assertions.assertNotNull(deletedReview);
        Assertions.assertEquals(deletedReview.getId(), review.getId());
    }
 
    @Test
    public void getById() {
        ReviewFixtures.withReview(review -> {
            Review queriedReview = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .reviews()
                    .withId(review.getId())
                    .get()
                    .executeBlocking();
            Assertions.assertNotNull(queriedReview);
            Assertions.assertEquals(queriedReview.getId(), review.getId());
        });
    }
    
    @Test
    public void getByKey(){
        ReviewFixtures.withReview(review -> {
            Review queriedReview = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .reviews()
                    .withKey(review.getKey())
                    .get()
                    .executeBlocking();
            Assertions.assertNotNull(queriedReview);
            Assertions.assertEquals(queriedReview.getKey(), review.getKey());
        });
    }    
    
    @Test
    public void query(){
        ReviewFixtures.withReview(review -> {
            ReviewPagedQueryResponse response = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .reviews()
                    .get()
                    .addWhere("id=" + "\"" + review.getId() +"\"")
                    .executeBlocking();
            Assertions.assertNotNull(response);
            Assertions.assertEquals(response.getResults().get(0).getId(), review.getId());
        });
    }
    
    @Test
    public void updateById(){
        ReviewFixtures.withUpdateableReview(review -> {
            List<ReviewUpdateAction> updateActions = new ArrayList<>();
            String newKey = CommercetoolsTestUtils.randomKey();
            updateActions.add(ReviewSetKeyActionBuilder.of().key(newKey).build());
            
            Review updatedReview = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .reviews()
                    .withId(review.getId())
                    .post(ReviewUpdateBuilder.of()
                        .actions(updateActions)
                        .version(review.getVersion())
                        .build())
                    .executeBlocking();

            Assertions.assertNotNull(updatedReview);
            Assertions.assertEquals(updatedReview.getKey(), newKey);
            
            return updatedReview;
        });
    }

    @Test
    public void updateByKey(){
        ReviewFixtures.withUpdateableReview(review -> {
            List<ReviewUpdateAction> updateActions = new ArrayList<>();
            String newKey = CommercetoolsTestUtils.randomKey();
            updateActions.add(ReviewSetKeyActionBuilder.of().key(newKey).build());

            Review updatedReview = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .reviews()
                    .withKey(review.getKey())
                    .post(ReviewUpdateBuilder.of()
                            .actions(updateActions)
                            .version(review.getVersion())
                            .build())
                    .executeBlocking();

            Assertions.assertNotNull(updatedReview);
            Assertions.assertEquals(updatedReview.getKey(), newKey);

            return updatedReview;
        });
    }
}
