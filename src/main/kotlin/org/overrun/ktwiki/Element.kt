package org.overrun.ktwiki

/**
 * The HTML element.
 *
 * @author squid233
 * @since 0.1.0
 */
abstract class Element {
    /**
     * Convert this element to string.
     *
     * @return the string
     */
    abstract fun asString(): String

    /**
     * Write the string of this element to the destination.
     *
     * @param[dest] the destination to store the result
     */
    abstract fun writeString(dest: Appendable)

    fun tryProperty(any: Any?, name: String): String =
        if (any != null) " $name=\"$any\""
        else ""
}

/**
 * The HTML element list.
 *
 * @author squid233
 * @since 0.1.0
 */
open class ElementList : Element() {
    val elements: MutableList<Element> = ArrayList()

    /**
     * Adds an element to the list.
     *
     * @param[element] the element to be added
     * @param[block] the action to be performed
     * @return `this`
     */
    fun <T : Element> add(element: T, block: T.() -> Unit = { }): ElementList {
        elements.add(element)
        block(element)
        return this
    }

    override fun asString(): String = buildString { elements.forEach(::appendLine) }

    override fun writeString(dest: Appendable) = elements.forEach { it.writeString(dest) }
}

/**
 * The HTML element html.
 *
 * @param[lang] the language or `null`
 * @author squid233
 * @since 0.1.0
 */
open class Html(var lang: String? = null) : ElementList() {
    fun head(lang: String? = null, block: Head.() -> Unit = { }) = add(Head(lang = lang), block)
    fun body(lang: String? = null, block: Body.() -> Unit = { }) = add(Body(lang = lang), block)
    fun meta(charset: String? = null, block: Meta.() -> Unit = { }) = add(Meta(charset = charset), block)

    override fun asString(): String = buildString {
        appendLine("<html${tryProperty(lang, "lang")}>")
        appendLine(super.asString())
        appendLine("\n</html>")
    }

    override fun writeString(dest: Appendable) {
        dest.appendLine("<html${tryProperty(lang, "lang")}>")
        super.writeString(dest)
        dest.appendLine("</html>")
    }
}

/**
 * The HTML element head.
 *
 * @param[lang] the language or `null`
 * @author squid233
 * @since 0.1.0
 */
open class Head(var lang: String? = null) : ElementList() {
    fun meta(charset: String? = null, block: Meta.() -> Unit = { }) = add(Meta(charset = charset), block)
    fun title(text: String, block: Title.() -> Unit = { }) = add(Title(text), block)

    override fun asString(): String = buildString {
        appendLine("<head${tryProperty(lang, "lang")}>")
        appendLine(super.asString())
        appendLine("\n</head>")
    }

    override fun writeString(dest: Appendable) {
        dest.appendLine("<head${tryProperty(lang, "lang")}>")
        super.writeString(dest)
        dest.appendLine("</head>")
    }
}

/**
 * The HTML element body.
 *
 * @param[lang] the language or `null`
 * @author squid233
 * @since 0.1.0
 */
open class Body(var lang: String? = null) : ElementList() {
    fun meta(charset: String? = null, block: Meta.() -> Unit = { }) = add(Meta(charset = charset), block)

    override fun asString(): String = buildString {
        appendLine("<body${tryProperty(lang, "lang")}>")
        appendLine(super.asString())
        appendLine("\n</body>")
    }

    override fun writeString(dest: Appendable) {
        dest.appendLine("<body${tryProperty(lang, "lang")}>")
        super.writeString(dest)
        dest.appendLine("</body>")
    }
}

/**
 * The HTML element meta.
 *
 * @param[charset] the charset or `null`
 * @author squid233
 * @since 0.1.0
 */
open class Meta(var charset: String? = null) : Element() {
    override fun asString(): String = "<meta${tryProperty(charset, "charset")}/>\n"

    override fun writeString(dest: Appendable) {
        dest.appendLine("<meta${tryProperty(charset, "charset")}/>")
    }
}

/**
 * The HTML element title.
 *
 * @param[text] the text
 * @author squid233
 * @since 0.1.0
 */
open class Title(var text: String) : Element() {
    override fun asString(): String = "<title>$text</title>\n"

    override fun writeString(dest: Appendable) {
        dest.appendLine("<title>$text</title>")
    }
}

/**
 * The literal text element.
 *
 * @param[text] the text
 * @author squid233
 * @since 0.1.0
 */
open class Literal(var text: String) : Element() {
    override fun asString(): String = text

    override fun writeString(dest: Appendable) {
        dest.append(text)
    }
}
