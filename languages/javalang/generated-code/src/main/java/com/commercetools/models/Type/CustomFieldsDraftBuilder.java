package com.commercetools.models.Type;

import com.commercetools.models.Type.FieldContainer;
import java.lang.Object;
import com.commercetools.models.Type.CustomFieldsDraft;
import javax.annotation.Nullable;
import java.util.List;

public final class CustomFieldsDraftBuilder {
   
   @Nullable
   private FieldContainer fields;
   
   
   private Object type;
   
   public CustomFieldsDraftBuilder fields(@Nullable final FieldContainer fields) {
      this.fields = fields;
      return this;
   }
   
   public CustomFieldsDraftBuilder type( final Object type) {
      this.type = type;
      return this;
   }
   
   @Nullable
   public FieldContainer getFields(){
      return this.fields;
   }
   
   
   public Object getType(){
      return this.type;
   }

   public CustomFieldsDraft build() {
       return new CustomFieldsDraftImpl(fields, type);
   }
   
   
   public static CustomFieldsDraftBuilder of() {
      return new CustomFieldsDraftBuilder();
   }
   
   
   public static CustomFieldsDraftBuilder of(final CustomFieldsDraft template) {
      CustomFieldsDraftBuilder builder = new CustomFieldsDraftBuilder();
      builder.fields = template.getFields();
      builder.type = template.getType();
      return builder;
   }
   
}