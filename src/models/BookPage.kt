package models

import org.jsoup.Jsoup

data class BookPage(
        private var status: Int,
        var data: String,
        val totalPages: Int,
        val chapterTitle: String,
        val page: Int,
        val isLastPage: Boolean,
        var prevChapter: Chapter?,
        var nextChapter: Chapter?
) {

    init {
        data = clearTrash(data)
    }

    fun clearTrash(page: String): String {
        var fragment = Jsoup.parse(page)
        var paragraphs = fragment.select("div[style=\"display: block; text-align: justify; margin-bottom: 12px;\"]")
                .map { it.select("p").first() }
        paragraphs.forEach { it.select("span[style=\"font-size: 0; display: inline; letter-spacing : -14px;\"]").remove() }
        var builder = StringBuilder()
        paragraphs.forEach { builder.append(it.outerHtml()) }
        return builder.toString();
    }
}