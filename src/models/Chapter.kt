package models

data class Chapter(
        val id: Int,
        var title: String?
) {
    var pages = ArrayList<BookPage>()
    var prev: Chapter? = null
    var next: Chapter? = null
}