package io.vrap.codegen.languages.csharp.extensions

import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.resources.impl.ResourceImpl

/**
 * Returns this string as standard C# enum name.
 */
fun ResourceImpl.GetNameAsPlurar(): String {
    return this.resourcePath.GetDomainNameAsPlurar()
}
fun Resource.GetNameAsPlurar(): String {
    return this.resourcePath.GetDomainNameAsPlurar()
}
fun String.GetDomainNameAsPlurar(): String
{
    var name = ""
    var path = this.split("/")
    if(path.size > 2)
        name = path[2]
    else
        name = "projects"

    if(name.contains("-"))
    {
        name = name.split("-")[0]+name.split("-")[1].capitalize()
    }
    return name.capitalize()
}