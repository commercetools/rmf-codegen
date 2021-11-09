### Guide to Update CTP Validators

* Add [test examples](https://github.com/commercetools/rmf-codegen/tree/main/ctp-validators/src/test/resources) to validate your new addition. Ensure to include both valid and invalid test cases to ensure it works fine and as expected.

* Add the new rule to the [ctp-validators ruleset](https://github.com/commercetools/rmf-codegen/blob/main/ctp-validators/src/main/resources/ruleset.xml). Ensure to exclude any exceptions e.g
```raml
<rule>
    <name>com.commercetools.rmf.validators.ResourcePluralRule</name>
    <options>
        <option type="exclude">in-store</option>
        <option type="exclude">me</option>
        <option type="exclude">inventory</option>
        ...
    </options>
</rule>
```

This rule states that all resources should be plural. But some Platform APIs don't follow this and they are explicitly excluded.

* Add the new validator rule in this [folder](https://github.com/commercetools/rmf-codegen/tree/main/ctp-validators/src/main/kotlin/com/commercetools/rmf/validators). A new `case` method should be added to the rule class which validates the test examples added above.