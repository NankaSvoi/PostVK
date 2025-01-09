
data class Post(
    val id: Int,                                                        //Уникальный идентификатор поста
    val authorId: Int,                                                  //Уникальный идентификатор автора
    val author: String = " ",                                           //имя автора
    val published: Int,                                                 //Время публикации
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
    private var posts = mutableListOf<Post>()                           //функция создает новый изменяемый список (MutableList) и возвращает его. Хранилище постов
    private var nextID = 1                                              // переменная для хранения уникального id

    fun add(post: Post): Post {
        val newPost = post.copy (id = nextID)                          // присваиваем новый уник. id посту
        posts += newPost                                               // Добавляет новый пост в список posts
        nextID ++                                                      // увеличиваем id для след. поста
        return newPost                                                 // возвращает последний добавленный пост
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
        for ((index, existsPost) in posts.withIndex()) {                    // Перебираем посты с индексами
            if (existsPost.id == post.id) {                                 // Если нашли пост с нужным id
                posts[index] = post                                         // Обновляем пост на переданный
                return true                                                 // возвращаем true если обновление успешно
            }
        }
        return false                                                        // если пост с таким id не найден, возвр. false
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
            id = 1,
            authorId = 1,
            author = "Murat",
            published = 300,
            content = "Первый пост"
        )
        val post2 = Post(
            id = 2,
            authorId = 2,
            author = "Nana",
            published = 500,
            content = "Победа",
        )

        WallService.add(post1)                                    // добавили первый пост
        WallService.add(post2)                                    // добавили второй пост

        WallService.likeById(1)                                // лайкаем пост с id 1
        WallService.printPosts()                                  // выводим список постов
        val updatedPost = Post(

            id = 1,
            authorId = 1,
            author = "Murat",
            published = 300,
            content = "Обновленный текст",
        )
        val result = WallService.update(updatedPost)
        println(result)                                            // tru если обновление прошло успешно
        WallService.printPosts()
    }
