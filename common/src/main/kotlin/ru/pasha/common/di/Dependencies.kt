package ru.pasha.common.di

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment

inline fun <reified Dependency> Fragment.findDependency(): Dependency {
    return findDependenciesByClass(Dependency::class.java)
}

@Suppress("UNCHECKED_CAST")
fun <Dependency> Fragment.findDependenciesByClass(clazz: Class<Dependency>): Dependency {
    return requireNotNull(allParents.firstOrNull { it::class as? Dependency != null } as? Dependency) {
        "Required dependency [$clazz] was null"
    }
}

private val Fragment.allParents: Iterable<Any>
    get() = object : Iterable<Any> {
        override fun iterator() = object : Iterator<Any> {
            private var currentParentFragment: Fragment? = parentFragment
            private var parentActivity: Activity? = activity
            private var parentApplication: Application? = parentActivity?.application

            override fun hasNext(): Boolean {
                return currentParentFragment != null || parentActivity != null || parentApplication != null
            }

            override fun next(): Any {
                currentParentFragment?.let { parent ->
                    currentParentFragment = parent.parentFragment
                    return parent
                }

                parentActivity?.let { parent ->
                    parentActivity = null
                    return parent
                }

                parentApplication?.let { parent ->
                    parentApplication = null
                    return parent
                }

                throw NoSuchElementException()
            }
        }
    }
