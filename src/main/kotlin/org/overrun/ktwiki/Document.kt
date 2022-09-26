package org.overrun.ktwiki

import java.io.File
import java.io.FileWriter
import java.io.Writer
import java.nio.charset.StandardCharsets

/**
 * The HTML document root.
 *
 * @author squid233
 * @since 0.1.0
 */
class Document {
    private val list = ElementList()

    fun <T : Element> add(element: T, block: T.() -> Unit = { }): T = element.also {
        block(it)
        list.add(it)
    }

    /**
     * Adds the element of `<!DOCTYPE type>`.
     *
     * @param[type] the document type
     * @return the literal element
     */
    fun doctype(type: String, block: Literal.() -> Unit = { }) = add(Literal("<!DOCTYPE $type>\n"), block)

    fun html(lang: String? = null, block: Html.() -> Unit = { }) = add(Html(lang = lang), block)

    fun writeToWriter(writer: Writer) {
        list.elements.forEach { it.writeString(writer) }
    }

    fun writeToFile(file: File) {
        FileWriter(file, StandardCharsets.UTF_8).use(::writeToWriter)
    }

    fun writeToFile(filename: String) {
        FileWriter(filename, StandardCharsets.UTF_8).use(::writeToWriter)
    }
}

fun document(block: Document.() -> Unit): Document = Document().apply(block)

fun generateDoc(filename: String, block: Document.() -> Unit): Document =
    document(block).apply { writeToFile(filename) }
