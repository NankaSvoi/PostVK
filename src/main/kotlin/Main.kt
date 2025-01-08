
data class Post(
    val id: Int,                                                        //Уникальный идентификатор поста
    val authorId: Int,                                                  //Уникальный идентификатор автора
    val author: String = " ",                                           //имя автора
    val published: Int,                                                 //Время публикации (вероятно, в формате UnixTime).
    val content: String,                                                //Содержимое поста.
    val likes: Likes = Likes(),                                         //Количество лайков, по умолчанию равно 0.
    val comments: Comments = Comments(),
    val views: Int = 0,                                                 // Количество просмотров
    val reposts: Int = 0,                                               // Количество репостов
    val isFavorite: Boolean = false,                                    // Добавлен ли в избранное
)

data class Likes(
    val count: Int = 0,                                                 // кол-во лайков.
    val userLikes: Boolean = false,                                     // поставил ли лайк данный пользователь
    val canLike: Boolean = true                                         // Может ли текущий пользователь поставить лайк
)

data class Comments(
    val count: Int = 0,                                                 // Количество комментариев
    val canPosts: Boolean = true                                        //Может ли данный пользователь добавить комментарий
)

// componentN - для деструктуризации

object WallService {                                                    //object: Синглтон (WallService )
    private var posts =
        mutableListOf<Post>()                           //функция создает новый изменяемый список (MutableList) и возвращает его.

    fun add(post: Post): Post {                                        // ф-ция принимает параметр post типа Post и возвращает объект того же типа."
        posts += post.copy(                                            // Добавляет новый пост в список posts
            id = if (posts.isEmpty()) 1 else posts.last().id + 1       // Назначаем id для нов. поста. если список пуст, оставляем 1, если фолс, то будет на 1 больше, чем у последнего
        )
        return posts.last()                                             // возвращает последний добавленный пост
    }

    fun likeById(id: Int) {                                             // параметр id используем для поиска поста, который нужно "лайкнуть".
        for ((index, post) in posts.withIndex()) {                      // цикл перебирает элементы коллекции, withIndex() — метод, возрощающий последовательность пар, каждая из которых состоит из индекса элемента и самого элемента.
            if (post.id == id) {                                        // Проверяется, совпадает ли id текущего поста (post.id) с переданным в функцию параметром id.
                val updatedLikes = post.likes.copy(count = post.likes.count + 1, userLikes = true)
                posts[index] = post.copy(likes = updatedLikes)
            }
        }
    }

    fun update(post: Post): Boolean {
        for ((index, oldPost) in posts.withIndex()) {
            if (oldPost.id == post.id) {
                posts[index] = post.copy(authorId = oldPost.authorId, published = oldPost.published)
                return true
            }
        }
        return false
    }

    fun findById(id: Int): Post? {
        return posts.find {
            it.id == id
        }
    }

        fun printPosts() {
            for (post in posts) println(post)
        }
    }


    fun main() {

        val post1 = Post(
            id = 0,
            authorId = 1,
            author = "Murat",
            published = 300,
            content = "Первый пост"
        )
        val post2 = Post(
            id = 0,
            authorId = 2,
            author = "Nana",
            published = 500,
            content = "Победа",
            isFavorite = true
        )
        WallService.add(post1)
        WallService.add(post2)

        WallService.likeById(1)
        WallService.printPosts()
        
    }
