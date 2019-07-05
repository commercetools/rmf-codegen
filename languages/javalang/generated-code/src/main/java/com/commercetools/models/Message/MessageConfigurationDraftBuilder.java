package com.commercetools.models.Message;

import java.lang.Boolean;
import java.lang.Integer;
import com.commercetools.models.Message.MessageConfigurationDraft;
import javax.annotation.Nullable;
import java.util.List;

public final class MessageConfigurationDraftBuilder {
   
   
   private Integer deleteDaysAfterCreation;
   
   
   private Boolean enabled;
   
   public MessageConfigurationDraftBuilder deleteDaysAfterCreation( final Integer deleteDaysAfterCreation) {
      this.deleteDaysAfterCreation = deleteDaysAfterCreation;
      return this;
   }
   
   public MessageConfigurationDraftBuilder enabled( final Boolean enabled) {
      this.enabled = enabled;
      return this;
   }
   
   
   public Integer getDeleteDaysAfterCreation(){
      return this.deleteDaysAfterCreation;
   }
   
   
   public Boolean getEnabled(){
      return this.enabled;
   }

   public MessageConfigurationDraft build() {
       return new MessageConfigurationDraftImpl(deleteDaysAfterCreation, enabled);
   }
   
   
   public static MessageConfigurationDraftBuilder of() {
      return new MessageConfigurationDraftBuilder();
   }
   
   
   public static MessageConfigurationDraftBuilder of(final MessageConfigurationDraft template) {
      MessageConfigurationDraftBuilder builder = new MessageConfigurationDraftBuilder();
      builder.deleteDaysAfterCreation = template.getDeleteDaysAfterCreation();
      builder.enabled = template.getEnabled();
      return builder;
   }
   
}