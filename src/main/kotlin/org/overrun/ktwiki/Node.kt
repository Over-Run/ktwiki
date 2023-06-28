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

import java.lang.StringBuilder

/**
 * @author squid233
 * @since 0.1.0
 */
interface Node {
    fun append(builder: StringBuilder)
}

fun literalNode(string: String): Node = object : Node {
    override fun append(builder: StringBuilder) {
        builder.append(string)
    }
}

fun paragraphNode(string: String): Node = literalNode("<p>$string</p>\n")

fun linkNode(link: String, content: String): Node = linkNode(link, literalNode(content))
fun linkNode(link: String, content: Node): Node = object : Node {
    override fun append(builder: StringBuilder) {
        builder.append("<a href=\"$link\" target=\"_blank\" rel=\"noopener noreferrer\">")
        content.append(builder)
        builder.appendLine("</a>")
    }
}

