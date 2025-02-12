import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class NoteServiceTest {

    @Before
    fun setUp() {
        NoteService.clearAllList()
        val noteId = NoteService.add(
            userId = 1,
            title = "Заголовок",
            text = "Текст",
            privacy = 0,
            commentPrivacy = "",
            privacyView = "",
            privacyComment = ""
        )
        NoteService.createComment(noteId, 1,"test")
    }

    @Test
    fun add() {
        val noteId = NoteService.add(
            userId = 1,
            title = "Заголовок2",
            text = "Текст2",
            privacy = 0,
            commentPrivacy = "",
            privacyView = "",
            privacyComment = ""
        )
        assertEquals(noteId, 1)
    }

    @Test
    fun createComment() {
        assertEquals(NoteService.createComment(0, 1, "test"), 1)
    }

    @Test(expected = NullPointerException::class)
    fun createCommentFail() {
        NoteService.createComment(2,  1,"test")
    }

    @Test
    fun delete() {
        assertTrue(NoteService.delete(0))
    }

    @Test(expected = NullPointerException::class)
    fun deleteFail() {
        NoteService.delete(2)
    }

    @Test
    fun deleteComment() {
        assertTrue(NoteService.deleteComment(0))
    }

    @Test(expected = NullPointerException::class)
    fun deleteCommentFail() {
        NoteService.deleteComment(1)
    }

    @Test
    fun edit() {
        NoteService.edit(
            noteId = 0,
            title = "Заголовок отредактирован",
            text = "Текст отредактирован",
            privacy = 0,
            commentPrivacy = "",
            privacyView = "",
            privacyComment = ""
        )
        assertEquals(NoteService.getById(0)!!.title, "Заголовок отредактирован")
    }

    @Test(expected = NullPointerException::class)
    fun editFail() {
        NoteService.edit(
            noteId = 2,
            title = "Заголовок отредактирован",
            text = "Текст отредактирован",
            privacy = 0,
            commentPrivacy = "",
            privacyView = "",
            privacyComment = ""
        )
    }

    @Test
    fun editComment() {
        assertTrue(NoteService.editComment(0,"Текст отредактирован"))
    }

    @Test(expected = NullPointerException::class)
    fun editCommentFail() {
        NoteService.editComment(2,"Текст отредактирован")
    }

    @Test
    fun get() {
        assertEquals(NoteService.get(1, 0, 0)?.last()?.text, "Текст")
    }

    @Test(expected = NullPointerException::class)
    fun getFail() {
        NoteService.get(0, 0, 0)
    }

    @Test
    fun getComments() {
        assertEquals(NoteService.getComments(0).count(), 1)
    }

    @Test(expected = NullPointerException::class)
    fun getCommentsFail() {
        NoteService.getComments(1)
    }

    @Test
    fun restoreComment() {
        assertTrue(NoteService.restoreComment(0))
    }

    @Test(expected = NullPointerException::class)
    fun restoreCommentFail() {
        NoteService.restoreComment(1)
    }
}