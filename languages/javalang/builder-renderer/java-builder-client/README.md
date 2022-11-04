## Contents

### JavaModelInterfaceRenderer

Renders for every object definition an interface. Implements an ObjectTypeRenderer.

The interfaces compose the inheritance as specified by the RAML specification. For each property a getter and setter is defined.

### JavaStringTypeRenderer

Renders named string types with an Enum. Implements a StringTypeRenderer.

The enumerations are implemented in a forward compatible way. The enum values are rendered as an enum, additionally a type
interface is created which exposes the enum values as static fields. In case the enum value is not found an anonymous instance
is created holding the JSON value which allows the usage of the value even if the SDK is not yet supporting is as an Enum implementation.

### JavaTraitRenderer

Renders the traits as defined by the RAML specification. Implements a TraitRenderer.

The trait implements methods for query parameters of a method endpoint and allows the creation of more generic applications.
The trait renderer also supports the rendering of query parameters with patterns when they specify a `placeholderParam` annotation.

### JavaApiRootFileProducer

Renders the ApiRoot class as entry point for the request builders. Implements a FileProducer.

### JavaModelClassFileProducer

Renders for every object definition the implementation of the model. Implements a FileProducer.

The implementation class are not inheriting from a class only implement the interface of the type. Therefor
all properties of a type must be rendered.

### JavaModelDraftBuilderFileProducer

Renders for every object definition a builder class of the model. Implements a FileProducer.

### JavaHttpRequestRenderer

Renders the request for an HTTP method as specified in RAML. Implements a MethodRenderer.

The rendered request class provides methods for each declared query parameter to set them. The request renderer also supports
the rendering of query parameters with patterns when they specify a `placeholderParam` annotation. The request supports
`application/json`, `x-www-form-urlencoded` and file format requests.

The name of each class is built by transforming the full URI to a unique class name. Any URI parameters is transformed using the
form `By<UriParameterName>`.

### JavaRequestBuilderResourceRenderer

Renders the resources as defined by the RAML specification. Implements a ResourceRenderer.

The resource renderer renders for every defined resource a method in the implementation. This method returns
a new instance of the subresource. For an HTTP method of a resource the respective methods are also rendered. They instantiate
the request class of this HTTP method. This way a chainable DSL for accessing the Api is built.

The name of each class is built by transforming the full URI to a unique class name. Any URI parameters is transformed using the
form `By<UriParameterName>`.

### JavaRequestTestRenderer

Render tests for testing the call chain of each resource and method with the query parameters. Implements a ResourceRenderer 

## Modules

For Java there are two generator modules existing.

The `JavaCompleteModule` renders the types using the `JavaModelInterfaceRenderer`,
`JavaStringTypeRenderer`, `JavaTraitRenderer`, `JavaApiRootFileProducer`, `JavaModelClassFileProducer`,
`JavaModelDraftBuilderFileProducer`, `JavaModelDraftBuilderFileProducer`, `JavaHttpRequestRenderer`,
and the `JavaRequestBuilderResourceRenderer`
The `JavaTestModule` renders the tests for requests and methods using the `JavaRequestTestRenderer`
