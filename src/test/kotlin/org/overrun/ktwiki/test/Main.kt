package org.overrun.ktwiki.test

import org.overrun.ktwiki.generateDoc

/**
 * @author squid233
 * @since 0.1.0
 */
fun main() {
    generateDoc("index.html") {
        doctype("html")
        html(lang = "zh-CN") {
            head {
                meta(charset = "UTF-8")
                title("Title")
            }
            body {  }
        }
    }
}
