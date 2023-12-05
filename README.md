# Дипломное задание "NeWork" (Профессия Android Developer)
### Краткое описание:

В рамках дипломного проекта разработано мобильное приложение, напоминающее формат LinkedIn, в котором пользователи
могут создавать посты и события с медиаресурсами, а также указывать места работы

### Инструменты:

- архитектура MVVM
- библиотеки:
  - Material Design
  - Coroutines
  - Room
  - Retrofit
  - LiveData, Flow
  - Paging
  - Hilt
  - ImagePicker, Glide

### Описание ключевых функций и возможностей:

- **Функционал, доступный неавтоизованному пользователю**
<img src="img/unauthorized.png" width="500" height="500"/>

Точка входа в приложение - список постов всех пользователей, которые доступны для просмотра всем, в том числе неавторизованным пользователям.
Также неавторизованный пользователь может просматривать списки событий и пользователей (переход осуществляется через панель навигации).
Через верхнее меню осуществляется переход на экраны авторизации и регистрации пользователя.
Также на экран авторизации неавторизованный пользователь попадает при попытке создать новый пост/новое событие либо лайкнуть пост/событие либо нажать иконку Принять участие (для события).
Со страницы авторизации доступен переход на страницу регистрации.

- **Функционал, доступный неавтоизованному пользователю**
<img src="img/unauthorized.png" width="500" height="500"/>

Точка входа в приложение - также список постов всех пользователей.
Авторизованному пользователю доступно добавление постов, а также редактирование и удаление своих постов через меню поста.
При нажатии на кнопку Добавить или при выборе пункта меню Редактировать осуществляется переход на экран Добавления/Редактирования поста
Аналогичный функционал доступен для списка событий.
Доступен для просмотра список пользователей
