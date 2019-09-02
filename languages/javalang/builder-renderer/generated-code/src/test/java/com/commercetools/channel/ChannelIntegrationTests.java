package com.commercetools.channel;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.Channel.*;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ChannelIntegrationTests {
    
    @Test
    public void createAndDeleteById() {
        Channel channel = ChannelFixtures.createChannel();
        Channel deletedChannel = ChannelFixtures.deleteChannel(channel.getId(), channel.getVersion());

        Assertions.assertEquals(channel.getId(), deletedChannel.getId());
    }
    
    @Test
    public void getById() {
        ChannelFixtures.withChannel(channel -> {
            Channel queriedChannel = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .channels()
                    .withId(channel.getId())
                    .get()
                    .executeBlocking();
            
            Assertions.assertNotNull(queriedChannel);
            Assertions.assertEquals(channel.getId(), queriedChannel.getId());
        });
    }
    
    @Test
    public void query() {
        ChannelFixtures.withChannel(channel -> {
            ChannelPagedQueryResponse response = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .channels()
                    .get()
                    .addWhere("id=" + "\"" + channel.getId() + "\"")
                    .executeBlocking();

            Assertions.assertNotNull(response);
            Assertions.assertEquals(response.getResults().get(0).getId(), channel.getId());
        });
    }
    
    @Test
    public void update() {
        ChannelFixtures.withUpdateableChannel(channel -> {
            List<ChannelUpdateAction> updateActions = new ArrayList<>();
            updateActions.add(ChannelSetGeoLocationActionBuilder.of().build());
            
            Channel updateChannel = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .channels()
                    .withId(channel.getId())
                    .post(ChannelUpdateBuilder.of()
                            .actions(updateActions)
                            .version(channel.getVersion())
                            .build())
                    .executeBlocking();

            Assertions.assertNotNull(updateChannel);
            
            return updateChannel;
        });
    }
    
}
