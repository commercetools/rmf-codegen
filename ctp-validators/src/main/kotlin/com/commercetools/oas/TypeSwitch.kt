package com.commercetools.oas

import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EObject

abstract class Switch<T> {
    fun defaultCase(eObject: EObject?): T? {
        return null
    }

//    /**
//     * Calls `caseXXX` for each (super-)class of the model until one returns a non-null result;
//     * it yields that result.
//     * @param eClass the class or superclass of `eObject` to consider.
//     * The class's containing `EPackage` determines whether the receiving switch object can handle the request.
//     * @param eObject the model object to pass to the appropriate `caseXXX`.
//     * @return the first non-null result returned by a `caseXXX` call.
//     */
//    protected fun doSwitch(`class`: Class<T>, sObject: Any?): T? {
//        return if (isSwitchFor(sObject?.javaClass?.`package`)) {
//            doSwitch(`class`, sObject)
//        } else {
//            val eSuperTypes: List<EClass> = `class`..eSuperTypes
//            if (eSuperTypes.isEmpty()) defaultCase(sObject) else doSwitch(eSuperTypes[0], sObject)
//        }
//    }

//    /**
//     * Dispatches the target object to the appropriate `caseXXX` methods.
//     * @param eObject the model object to pass to the appropriate `caseXXX`.
//     * @return the first non-null result returned by a `caseXXX` call.
//     */
//    fun doSwitch(sObject: Object): T? {
//        return doSwitch(sObject.`class` as Class<T>, sObject)
//    }

    /**
     * Indicates whether the receiver is a switch for the specified package.
     * @param ePackage the package of interest.
     * @return `true` if the receiver is a switch for `package`.
     */
    protected abstract fun isSwitchFor(`package`: Package?): Boolean
}
