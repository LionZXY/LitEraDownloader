import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.jsoup.Jsoup
import models.BookPage
import models.Chapter
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException
import okhttp3.MultipartBody
import java.util.*


private val client = OkHttpClient()
private val gson = Gson()

fun main(args: Array<String>) {
    var book = downloadBook(Chapter(68015, null))
    println(book.size)
}

fun downloadBook(firstChapter: Chapter): List<Chapter> {
    var book = ArrayList<Chapter>()
    var chapter: Chapter? = firstChapter
    do {
        book.add(chapter!!)
        downloadChapter(chapter)
        chapter = chapter.next
    } while (chapter != null)
    return book
}

fun downloadChapter(chapter: Chapter) {
    var page = downloadPage(1, chapter)
    var totalPage = page.totalPages
    chapter.title = page.chapterTitle
    chapter.pages.add(page)
    for (i in 2..totalPage) {
        if (page.nextChapter != null) chapter.next = page.nextChapter
        if (page.prevChapter != null) chapter.prev = page.prevChapter
        if (page.isLastPage)
            break
        page = downloadPage(i, chapter)
        chapter.pages.add(page)
    }
}

fun downloadPage(page: Int, chapter: Chapter): BookPage {
    Thread.sleep(Random().nextLong() % 2000)
    val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("chapterId", chapter.id.toString())
            .addFormDataPart("page", page.toString())
            .addFormDataPart("_csrf", "Lm9QMThFZFMcW2UACxMBHkgmEghRDlEpdiA9AQwtLzwXFh8HQAAdYg==")
            .build()

    val request = Request.Builder()
            .url("https://lit-era.com/reader/get-page")
            .method("POST", RequestBody.create(null, ByteArray(0)))
            .post(requestBody)
            .addHeader("Cookie", Constants.COOKIE)
            .build()

    val response = client.newCall(request).execute()
    if (!response.isSuccessful) throw IOException("Unexpected code " + response)
    return gson.fromJson(response.body().charStream(), BookPage::class.java)
}