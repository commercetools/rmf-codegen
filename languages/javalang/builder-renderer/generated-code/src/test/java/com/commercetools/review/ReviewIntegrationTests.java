package com.commercetools.review;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.Review.*;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ReviewIntegrationTests {
    
    @Test
    public void createAndDelete(){
        ReviewDraft reviewDraft = ReviewDraftBuilder.of()
                .key(CommercetoolsTestUtils.randomKey())
                .title("review-title-1")
                .build();

        Review review = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                .reviews()
                .post(reviewDraft)
                .executeBlocking();

        Assert.assertNotNull(review);
        Assert.assertEquals(reviewDraft.getKey(), review.getKey());
        
        Review deletedReview = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                .reviews()
                .withId(review.getId())
                .delete()
                .addVersion(review.getVersion())
                .executeBlocking();

        Assert.assertNotNull(deletedReview);
        Assert.assertEquals(deletedReview.getId(), review.getId());
    }
 
    @Test
    public void getById() {
        ReviewFixtures.withReview(review -> {
            Review queriedReview = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .reviews()
                    .withId(review.getId())
                    .get()
                    .executeBlocking();
            Assert.assertNotNull(queriedReview);
            Assert.assertEquals(queriedReview.getId(), review.getId());
        });
    }
    
    @Test
    public void getByKey(){
        ReviewFixtures.withReview(review -> {
            Review queriedReview = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .reviews()
                    .withKey(review.getKey())
                    .get()
                    .executeBlocking();
            Assert.assertNotNull(queriedReview);
            Assert.assertEquals(queriedReview.getKey(), review.getKey());
        });
    }    
    
    @Test
    public void query(){
        ReviewFixtures.withReview(review -> {
            ReviewPagedQueryResponse response = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .reviews()
                    .get()
                    .addWhere("id=" + "\"" + review.getId() +"\"")
                    .executeBlocking();
            Assert.assertNotNull(response);
            Assert.assertEquals(response.getResults().get(0).getId(), review.getId());
        });
    }
    
    @Test
    public void updateById(){
        ReviewFixtures.withUpdateableReview(review -> {
            List<ReviewUpdateAction> updateActions = new ArrayList<>();
            String newKey = CommercetoolsTestUtils.randomKey();
            updateActions.add(ReviewSetKeyActionBuilder.of().key(newKey).build());
            
            Review updatedReview = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .reviews()
                    .withId(review.getId())
                    .post(ReviewUpdateBuilder.of()
                        .actions(updateActions)
                        .version(review.getVersion())
                        .build())
                    .executeBlocking();

            Assert.assertNotNull(updatedReview);
            Assert.assertEquals(updatedReview.getKey(), newKey);
            
            return updatedReview;
        });
    }

    @Test
    public void updateByKey(){
        ReviewFixtures.withUpdateableReview(review -> {
            List<ReviewUpdateAction> updateActions = new ArrayList<>();
            String newKey = CommercetoolsTestUtils.randomKey();
            updateActions.add(ReviewSetKeyActionBuilder.of().key(newKey).build());

            Review updatedReview = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .reviews()
                    .withKey(review.getKey())
                    .post(ReviewUpdateBuilder.of()
                            .actions(updateActions)
                            .version(review.getVersion())
                            .build())
                    .executeBlocking();

            Assert.assertNotNull(updatedReview);
            Assert.assertEquals(updatedReview.getKey(), newKey);

            return updatedReview;
        });
    }
}
