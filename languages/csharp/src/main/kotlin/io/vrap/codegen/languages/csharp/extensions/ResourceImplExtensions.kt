package io.vrap.codegen.languages.csharp.extensions

import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.resources.impl.ResourceImpl

/**
 * Returns this string as standard C# enum name.
 */
fun ResourceImpl.GetNameAsPlural(): String {
    return this.resourcePath.GetDomainNameAsPlural()
}
fun Resource.GetNameAsPlural(): String {
    return this.resourcePath.GetDomainNameAsPlural()
}
fun String.GetDomainNameAsPlural(): String
{
    var name = ""
    var path = this.split("/")
    if(path.size > 2)
        name = path[2]
    else
        name = path[1]

    if (name.contains("{") && name.contains("}")) {
        name = name.replace("{", "").replace("}", "")
    }
    if (name == "projectKey")
        name = "projects"
    if(name.contains("-"))
    {
        name = name.split("-")[0]+name.split("-")[1].capitalize()
    }
    return name.capitalize()
}
