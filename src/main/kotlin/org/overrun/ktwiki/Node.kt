/*
 * MIT License
 *
 * Copyright (c) 2023 Overrun Organization
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 */

package org.overrun.ktwiki

/**
 * @author squid233
 * @since 0.1.0
 */
interface Node {
    /** Attempts to generate string without using the page id. */
    override fun toString(): String
    fun generate(pageID: PageID): String
}

/**
 * @author squid233
 * @since 0.1.0
 */
abstract class ListBackedNode : Node {
    protected val contentList: MutableList<Node> = ArrayList()

    operator fun Node.unaryPlus() {
        contentList += this
    }

    operator fun String.unaryPlus() {
        +p(this)
    }

    operator fun String.unaryMinus() {
        +literal(this)
    }

    operator fun String.not() {
        +codeBlock(this)
    }

    @JvmName("addLink")
    operator fun Pair<String, String>.unaryPlus() {
        +a(href = this.second, content = this.first)
    }

    @JvmName("addHeading")
    operator fun Pair<String, Int>.unaryPlus() {
        +h(level = this.second, content = this.first)
    }
}

/**
 * @author squid233
 * @since 0.1.0
 */
open class TagListNode(
    protected val tag: String,
    protected val id: String? = null,
    protected val `class`: String? = null,
    protected val style: String? = null,
    protected val content: TagListNode.(PageID) -> Unit
) : ListBackedNode() {
    override fun toString(): String = throw UnsupportedOperationException()
    override fun generate(pageID: PageID): String = buildString {
        content(pageID)
        appendLine("<$tag${id(id)}${`class`(`class`)}${style(style)}>")
        contentList.forEach { append(it.generate(pageID)) }
        appendLine("</$tag>")
        contentList.clear()
    }
}

/**
 * @author squid233
 * @since 0.1.0
 */
class ListNode(
    tag: String,
    id: String? = null,
    `class`: String? = null,
    style: String? = null,
    content: TagListNode.(PageID) -> Unit
) : TagListNode(tag, id, `class`, style, content) {
    override fun generate(pageID: PageID): String = buildString {
        content(pageID)
        appendLine("<$tag${id(id)}${`class`(`class`)}${style(style)}>")
        contentList.forEach {
            append("<li>")
            append(it.generate(pageID))
            appendLine("</li>")
        }
        appendLine("</$tag>")
        contentList.clear()
    }
}

/**
 * @author squid233
 * @since 0.1.0
 */
@Suppress("ClassName")
object br : Node {
    override fun toString(): String = "<br>"
    override fun generate(pageID: PageID): String = toString()
}

/**
 * A relative link that auto-converts with the current page.
 *
 * @param[classCurr] the CSS class used when [href] returns `null`.
 * @author squid233
 * @since 0.1.0
 */
class RelativeLink(
    private val href: (PageID) -> String?,
    private val content: String,
    private val classCurr: String? = null
) : Node {
    override fun toString(): String = throw UnsupportedOperationException()
    override fun generate(pageID: PageID): String {
        val s = href(pageID)
        return if (s != null)
            "<a href=\"$s\" target=\"_blank\" rel=\"noopener noreferrer\">$content</a>"
        else "<b${classCurr?.let { " class=\"$it\"" } ?: ""}>$content</b>"
    }
}

private fun id(id: String?): String = if (id != null) " id=\"$id\"" else ""
private fun `class`(`class`: String?): String = if (`class` != null) " class=\"$`class`\"" else ""
private fun style(style: String?): String = if (style != null) " style=\"$style\"" else ""

private fun singleTag(
    tag: String,
    string: String,
    id: String?,
    `class`: String?,
    style: String?,
    newLine: Boolean = true
): Node = literal("<$tag${id(id)}${`class`(`class`)}${style(style)}>$string</$tag>${if (newLine) "\n" else ""}")

fun literal(string: String): Node = object : Node {
    override fun toString(): String = string
    override fun generate(pageID: PageID): String = toString()
}

fun literal(string: (PageID) -> String): Node = object : Node {
    override fun toString(): String = throw UnsupportedOperationException()
    override fun generate(pageID: PageID): String = string(pageID)
}

fun p(
    string: String,
    id: String? = null,
    `class`: String? = null,
    style: String? = null
): Node = singleTag("p", string, id, `class`, style)

fun p(
    id: String? = null,
    `class`: String? = null,
    style: String? = null,
    content: TagListNode.(PageID) -> Unit
): Node = TagListNode("p", id, `class`, style, content)

fun b(
    string: String,
    id: String? = null,
    `class`: String? = null,
    style: String? = null
): Node = singleTag("b", string, id, `class`, style, false)

fun i(
    string: String,
    id: String? = null,
    `class`: String? = null,
    style: String? = null
): Node = singleTag("i", string, id, `class`, style, false)

fun s(
    string: String,
    id: String? = null,
    `class`: String? = null,
    style: String? = null
): Node = singleTag("s", string, id, `class`, style, false)

fun u(
    string: String,
    id: String? = null,
    `class`: String? = null,
    style: String? = null
): Node = singleTag("u", string, id, `class`, style, false)

fun strong(
    string: String,
    id: String? = null,
    `class`: String? = null,
    style: String? = null
): Node = singleTag("strong", string, id, `class`, style, false)

fun em(
    string: String,
    id: String? = null,
    `class`: String? = null,
    style: String? = null
): Node = singleTag("em", string, id, `class`, style, false)

fun small(
    string: String,
    id: String? = null,
    `class`: String? = null,
    style: String? = null
): Node = singleTag("small", string, id, `class`, style, false)

fun sub(
    string: String,
    id: String? = null,
    `class`: String? = null,
    style: String? = null
): Node = singleTag("sub", string, id, `class`, style, false)

fun sup(
    string: String,
    id: String? = null,
    `class`: String? = null,
    style: String? = null
): Node = singleTag("sup", string, id, `class`, style, false)

fun ins(
    string: String,
    id: String? = null,
    `class`: String? = null,
    style: String? = null
): Node = singleTag("ins", string, id, `class`, style, false)

fun del(
    string: String,
    id: String? = null,
    `class`: String? = null,
    style: String? = null
): Node = singleTag("del", string, id, `class`, style, false)

fun a(
    href: String,
    content: Node,
    id: String? = null,
    `class`: String? = null,
    style: String? = null,
    openInBlank: Boolean = true,
): Node = a(href, content.toString(), id, `class`, style, openInBlank)

fun a(
    href: String,
    content: String,
    id: String? = null,
    `class`: String? = null,
    style: String? = null,
    openInBlank: Boolean = true,
): Node = literal(
    "<a href=\"$href\"${
        if (openInBlank) " target=\"_blank\" rel=\"noopener noreferrer\"" else ""
    }${id(id)}${`class`(`class`)}${style(style)}>$content</a>"
)

fun h(
    level: Int,
    content: Node,
    id: String? = null,
    `class`: String? = null,
    style: String? = null
): Node = h(level, content.toString(), id, `class`, style)

fun h(
    level: Int,
    content: String,
    id: String? = null,
    `class`: String? = null,
    style: String? = null
): Node {
    check(level in 1..6) { "Must be 1 to 6" }
    return literal("<h$level${id(id)}${`class`(`class`)}${style(style)}>$content</h$level>\n")
}

fun div(
    id: String? = null,
    `class`: String? = null,
    style: String? = null,
    content: TagListNode.(PageID) -> Unit
): Node = TagListNode("div", id, `class`, style, content)

fun code(
    content: String,
    id: String? = null,
    `class`: String? = null,
    style: String? = null
): Node = literal("<code${id(id)}${`class`(`class`)}${style(style)}>$content</code>")

fun codeBlock(
    content: String,
    id: String? = null,
    `class`: String? = null,
    style: String? = null
): Node =
    literal("<pre${id(id)}${`class`(`class`)}${style(style)}><code>$content</code></pre>\n")

fun ul(
    id: String? = null,
    `class`: String? = null,
    style: String? = null,
    content: TagListNode.(PageID) -> Unit
): Node = ListNode("ul", id, `class`, style, content)

fun ol(
    id: String? = null,
    `class`: String? = null,
    style: String? = null,
    content: TagListNode.(PageID) -> Unit
): Node = ListNode("ol", id, `class`, style, content)
