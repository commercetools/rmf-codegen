@file:JvmName("RamlCodegenPlugin")

package io.vrap.rmf.codegen.plugin

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project

open class RamlCodegenPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        setupProject(project)
        createTasks(project)
    }

    fun setupProject(project: Project){
        val generators : NamedDomainObjectContainer<Generator>  =  project.container(Generator::class.java)
        generators.all { generator: Generator?->   generator?.targets = project.container(Target::class.java)}
        project.extensions.add("generateRamlStub", generators)
    }

    fun createTasks(project: Project){
        project.tasks.create("generateRamlStub",RamlCodeGeneratorTask::class.java)
    }
}
