data class Note(
    val nid: Int,
    val uid: Int,
    var title: String,
    var text: String,
    var privacy: Int,
    var commentPrivacy: String,
    var privacyView: String,
    var privacyComment: String,
)

data class Comment(
    val cid: Int,
    val uid: Int,
    val nid: Int,
    val oid: Int,
    val date: Int,
    var message: String,
    val replyTo: Int,
    var commentIsDelete: Boolean = false
)

object NoteService {
    private var listNotes: MutableList<Note> = mutableListOf()
    private var listComments: MutableList<Comment> = mutableListOf()
    private var lastNid: Int = 0
    private var lastCommentId: Int = 0

    //Создает новую заметку у текущего пользователя.
    fun add(userId: Int, title: String, text: String, privacy: Int, commentPrivacy: String, privacyView: String, privacyComment: String): Int{
        listNotes.add(Note(
            lastNid++,
            userId,
            title,
            text,
            privacy,
            commentPrivacy,
            privacyView,
            privacyComment
        ))
        return listNotes.last().nid
    }

    //Добавляет новый комментарий к заметке.
    fun createComment(noteId: Int, userId: Int, message: String): Int{
        getById(noteId) ?: throw NullPointerException("Нет заметки с номером $noteId")
        listComments.add (Comment(
                    cid = lastCommentId++,
                    uid = userId,
                    nid = noteId,
                    oid = 0,
                    date = 0,
                    message = message,
                    replyTo = 0,
                ))
        return listComments.last().cid
    }

    //Удаляет заметку текущего пользователя.
    fun delete(noteId: Int): Boolean{
        val deleteNote = getById(noteId) ?: throw NullPointerException("Нет заметки с номером $noteId")
        return listNotes.remove(deleteNote)
    }

    //Удаляет комментарий к заметке.
    fun deleteComment(commentId: Int): Boolean{
        for (comment in listComments){
            if (comment.cid == commentId){
                comment.commentIsDelete = true
                return true
            }
        }
        throw NullPointerException("Нет комментария с номером $commentId")
    }

    //Редактирует заметку текущего пользователя.
    fun edit(noteId: Int, title: String, text: String, privacy: Int, commentPrivacy: String, privacyView: String, privacyComment: String): Boolean{
        val note = getById(noteId) ?:  throw NullPointerException("Нет заметки с номером $noteId")
        note.title = title
        note.text = text
        note.privacy = privacy
        note.commentPrivacy = commentPrivacy
        note.privacyView = privacyView
        note.privacyComment = privacyComment
        return true
    }

    //Редактирует указанный комментарий у заметки.
    fun editComment(commentId: Int, message: String): Boolean{
        for (comment in listComments) {
            if (comment.cid == commentId) {
                comment.message = message
                return true
            }
        }
        throw NullPointerException("Нет заметки с номером $commentId")
    }

    //Возвращает список заметок, созданных пользователем.
    fun get (userId: Int, offset: Int?, count: Int?, sort: Int = 0): MutableList<Note> {
        val result: MutableList<Note> = mutableListOf()
        for (note in listNotes){
            if (note.uid == userId){
                result.add(note)
            }
        }
        return if (result.isEmpty()) throw NullPointerException("Список пуст") else result
    }

    //Возвращает заметку по её id.
    fun getById(noteId: Int): Note?{
        for (note in listNotes){
            if (note.nid == noteId){
                return note
            }
        }
        return null
    }

    //Возвращает список комментариев к заметке.
    fun getComments(noteId: Int): MutableList<Comment>{
        val result: MutableList<Comment> = mutableListOf()
        for(comment in listComments){
            if (comment.nid == noteId && !comment.commentIsDelete){
                result.add(comment)
            }
        }
        return if (result.isEmpty()) throw NullPointerException("Список пуст") else result
    }

    //Восстанавливает удалённый комментарий.
    fun restoreComment(commentId: Int): Boolean{
        for (comment in listComments){
            if (comment.cid == commentId){
                comment.commentIsDelete = false
                return true
            }
        }
        throw NullPointerException("Нет комментария с номером $commentId")
    }

    fun clearAllList(){
        listNotes.clear()
        listComments.clear()
        lastNid = 0
        lastCommentId = 0
    }
}

fun main(){
    val noteId = NoteService.add(
        userId = 1,
        title = "Заголовок",
        text = "Текст",
        privacy = 0,
        commentPrivacy = "",
        privacyView = "",
        privacyComment = ""
    )
    NoteService.createComment(noteId, 1,"Комментарий 1")
    NoteService.createComment(noteId, 1,"Комментарий 2")
    val deleteCommentId = NoteService.createComment(noteId, 1, "Комментарий 3")
    NoteService.deleteComment(deleteCommentId)

    val comments = NoteService.getComments(noteId)
    NoteService.restoreComment(deleteCommentId)
    val commentsRecovery = NoteService.getComments(noteId)

    return
}