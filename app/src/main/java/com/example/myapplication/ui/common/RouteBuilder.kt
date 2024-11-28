package com.example.myapplication.ui.common

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

interface RouteBuilder {
    fun buildRoute(): String
    fun buildArgs(): List<NamedNavArgument>
}

fun RouteBuilder(
    path: String,
    args: (NavArgBuilder.() -> Unit)? = null,
): RouteBuilder = RouteBuilderImpl(path, args)

private class RouteBuilderImpl(
    private val path: String,
    private val argumentBuilder: (NavArgBuilder.() -> Unit)? = null,
) : RouteBuilder {
    private var builtArgs: List<NavArgSpec>? = null

    private fun getArgs(): List<NavArgSpec> {
        return builtArgs ?: run {
            val builder = NavArgBuilderImpl()
            argumentBuilder?.let { builder.it() }
            val args = builder.args
            builtArgs = args
            return args
        }
    }

    override fun buildRoute(): String {
        val args = getArgs()
        val sb = StringBuilder(path)
        var first = true
        for (arg in args) {
            if (path.contains("{${arg.name}}")) {
                continue
            }
            if (first) {
                sb.append("?")
                first = false
            } else {
                sb.append("&")
            }
            sb.append("${arg.name}={${arg.name}}")
        }
        return sb.toString()
    }

    override fun buildArgs(): List<NamedNavArgument> {
        return getArgs().map { arg ->
            navArgument(arg.name) {
                type = arg.navType
                nullable = arg.nullable
                if (arg.default != null) {
                    defaultValue = arg.default
                }
            }
        }
    }
}

interface NavArgBuilder {
    fun enum(name: String): NavArgSpec
    fun long(name: String): NavArgSpec
    fun string(name: String): NavArgSpec
    fun bool(name: String): NavArgSpec
    fun longArray(name: String): NavArgSpec
    fun stringList(name: String): NavArgSpec
}

private class NavArgBuilderImpl : NavArgBuilder {
    private val _args: MutableList<NavArgSpec> = mutableListOf()

    val args: List<NavArgSpec> get() = _args

    override fun enum(name: String): NavArgSpec {
        val arg = NavArgSpec(
            name = name,
            navType = NavType.IntType,
        )
        _args.add(arg)
        return arg
    }

    override fun long(name: String): NavArgSpec {
        val arg = NavArgSpec(
            name = name,
            navType = NullableLongType,
        )
        _args.add(arg)
        return arg
    }

    override fun string(name: String): NavArgSpec {
        val arg = NavArgSpec(
            name = name,
            navType = NavType.StringType,
        )
        _args.add(arg)
        return arg
    }

    override fun bool(name: String): NavArgSpec {
        val arg = NavArgSpec(
            name = name,
            navType = NullableBoolType,
        )
        _args.add(arg)
        return arg
    }

    override fun longArray(name: String): NavArgSpec {
        val arg = NavArgSpec(
            name = name,
            navType = LongArraySafeNavType,
        )
        _args.add(arg)
        return arg
    }

    override fun stringList(name: String): NavArgSpec {
        val arg = NavArgSpec(
            name = name,
            navType = StringListType,
        )
        _args.add(arg)
        return arg
    }
}

data class NavArgSpec(
    val name: String,
    val navType: NavType<*>,
) {
    var nullable: Boolean = false
        private set
    var default: Any? = null
        private set

    infix fun default(default: Any?): NavArgSpec {
        this.default = default
        return this
    }

    infix fun nullable(nullable: Boolean): NavArgSpec {
        this.nullable = nullable
        return this
    }
}

interface RouteParameterBuilder {
    infix fun String.set(value: Int)
    infix fun String.set(value: Long)
    infix fun String.set(value: String?)
    infix fun String.set(value: Boolean)
    infix fun <T : Enum<out T>> String.set(value: T)
    infix fun String.set(value: LongArray)
    infix fun String.set(value: Iterable<CharSequence>)
}

fun buildRoute(path: String, builder: (RouteParameterBuilder.() -> Unit)? = null): String {
    val route = ParameterBuilderImpl(path)
    if (builder != null) {
        route.builder()
    }
    return route.build()
}

private class ParameterBuilderImpl(path: String) : RouteParameterBuilder {
    private val builder = Uri.Builder().path(path)

    override infix fun String.set(value: Int) {
        builder.appendQueryParameter(this, value.toString())
    }

    override infix fun String.set(value: Long) {
        builder.appendQueryParameter(this, value.toString())
    }

    override infix fun String.set(value: String?) {
        if (value != null) {
            builder.appendQueryParameter(this, value)
        }
    }

    override infix fun String.set(value: Boolean) {
        builder.appendQueryParameter(this, if (value) "true" else "false")
    }

    override infix fun <T : Enum<out T>> String.set(value: T) {
        builder.appendQueryParameter(this, value.ordinal.toString())
    }

    override infix fun String.set(value: LongArray) {
        builder.appendQueryParameter(this, value.joinToString(",") { it.toString() })
    }

    override infix fun String.set(value: Iterable<CharSequence>) {
        val s = buildString {
            for (v in value) {
                if (isNotEmpty()) {
                    append(',')
                }
                append(Uri.encode(v.toString()))
            }
        }
        builder.appendQueryParameter(this, s)
    }

    fun build() = builder.build().toString()
}

@JvmField
val LongArraySafeNavType = object : NavType<LongArray?>(true) {
    override val name: String
        get() = "long[]"

    override fun put(bundle: Bundle, key: String, value: LongArray?) {
        bundle.putLongArray(key, value)
    }

    override fun get(bundle: Bundle, key: String): LongArray? {
        return bundle.getLongArray(key)
    }

    override fun parseValue(value: String): LongArray? {
        if (value.isEmpty()) {
            return null
        }
        return value.split(",").map { it.toLong() }.toLongArray()
    }
}

@JvmField
val StringListType = object : NavType<ArrayList<String>?>(true) {

    override fun put(bundle: Bundle, key: String, value: ArrayList<String>?) {
        bundle.putStringArrayList(key, value)
    }

    override fun get(bundle: Bundle, key: String): ArrayList<String>? {
        return bundle.getStringArrayList(key)
    }

    override fun parseValue(value: String): ArrayList<String>? {
        if (value.isEmpty()) {
            return null
        }
        return value.split(",").toCollection(ArrayList())
    }
}

@JvmField
val NullableBoolType = object : NavType<Boolean?>(true) {
    override val name: String
        get() = "boolean"

    override fun put(bundle: Bundle, key: String, value: Boolean?) {
        if (value != null) {
            bundle.putBoolean(key, value)
        }
    }

    override fun get(bundle: Bundle, key: String): Boolean? {
        // Nullable boolean is not supported natively
        @Suppress("DEPRECATION")
        return bundle[key] as Boolean?
    }

    override fun parseValue(value: String): Boolean? {
        return when (value) {
            "true" -> true
            "false" -> false
            else -> null
        }
    }
}

@JvmField
val NullableLongType = object : NavType<Long?>(true) {
    override val name: String
        get() = "long"

    override fun put(bundle: Bundle, key: String, value: Long?) {
        if (value != null) {
            bundle.putLong(key, value)
        }
    }

    override fun get(bundle: Bundle, key: String): Long? {
        // Nullable long is not supported natively
        @Suppress("DEPRECATION")
        return bundle[key] as Long?
    }

    override fun parseValue(value: String): Long? {
        if (value.isEmpty()) {
            return null
        }
        // At runtime the L suffix is optional, contrary to the Safe Args plugin.
        // This is in order to be able to parse long numbers passed as deep link URL
        // parameters
        var localValue = value
        if (value.endsWith("L")) {
            localValue = localValue.substring(0, value.length - 1)
        }
        return if (value.startsWith("0x")) {
            localValue.substring(2).toLong(16)
        } else {
            localValue.toLong()
        }
    }
}
