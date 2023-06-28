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

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

/**
 * A CSS stylesheet.
 *
 * @param[name] the name of the stylesheet.
 * @author squid233
 * @since 0.1.0
 */
class Stylesheet(val name: String, action: Stylesheet.() -> Unit) {
    private val styles: MutableList<String> = ArrayList()

    init {
        action()
    }

    operator fun String.unaryPlus() {
        styles += this
    }

    fun generate(basePath: String) {
        val finalPath = Path.of(basePath, "css")
        Files.createDirectories(finalPath)
        Files.writeString(finalPath.resolve("$name.css"), buildString {
            appendLine("/* auto generated file. DO NOT EDIT */")
            styles.forEach { appendLine(it) }
        }, StandardCharsets.UTF_8)
    }
}
