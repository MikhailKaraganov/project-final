## [REST API](http://localhost:8080/doc)

## Концепция:
- Spring Modulith
  - [Spring Modulith: достигли ли мы зрелости модульности](https://habr.com/ru/post/701984/)
  - [Introducing Spring Modulith](https://spring.io/blog/2022/10/21/introducing-spring-modulith)
  - [Spring Modulith - Reference documentation](https://docs.spring.io/spring-modulith/docs/current-SNAPSHOT/reference/html/)

```
  url: jdbc:postgresql://localhost:5432/jira
  username: jira
  password: JiraRush
```
- Есть 2 общие таблицы, на которых не fk
  - _Reference_ - справочник. Связь делаем по _code_ (по id нельзя, тк id привязано к окружению-конкретной базе)
  - _UserBelong_ - привязка юзеров с типом (owner, lead, ...) к объекту (таска, проект, спринт, ...). FK вручную будем проверять

## Аналоги
- https://java-source.net/open-source/issue-trackers

## Тестирование
- https://habr.com/ru/articles/259055/

Список выполненных задач:

1.Разобраться со структурой проекта (onboarding).

2.Удалить социальные сети: vk, yandex. 

![изображение](https://github.com/MikhailKaraganov/project-final/assets/77681385/22b5a706-ce58-421f-be97-d56f87a55a05)
![изображение](https://github.com/MikhailKaraganov/project-final/assets/77681385/45bceb89-433f-40c3-b398-f74490df4c1f)


3.Вынести чувствительную информацию (логин, пароль БД, идентификаторы для OAuth регистрации/авторизации, настройки почты) в отдельный проперти файл. Значения этих проперти должны считываться при старте сервера из переменных окружения машины. 

![изображение](https://github.com/MikhailKaraganov/project-final/assets/77681385/350fac61-1c86-46d8-a48d-022027c97afb)


4.Переделать тесты так, чтоб во время тестов использовалась in memory БД (H2), а не PostgreSQL. Для этого нужно определить 2 бина, и выборка какой из них использовать должно определяться активным профилем Spring. 

![изображение](https://github.com/MikhailKaraganov/project-final/assets/77681385/31f67f84-e894-43ca-8737-487a125e9d06)


5.Написать тесты для всех публичных методов контроллера ProfileRestController.

![изображение](https://github.com/MikhailKaraganov/project-final/assets/77681385/770cb668-06dc-43f2-a115-62a221a93554)


6.Добавить новый функционал: добавления тегов к задаче. Фронт делать необязательно.

![изображение](https://github.com/MikhailKaraganov/project-final/assets/77681385/d39ada33-0d31-439e-920c-5dafb5a94ab7)
![изображение](https://github.com/MikhailKaraganov/project-final/assets/77681385/555f6450-cb6e-4acc-bd14-2a49671ab92b)


7.Добавить возможность подписываться на задачи, которые не назначены на текущего пользователя. (Рассылку уведомлений/письма о смене статуса задачи делать не нужно). Фронт делать необязательно

![изображение](https://github.com/MikhailKaraganov/project-final/assets/77681385/ece15c04-e2fc-436d-a2ff-4d42c5019f5e)
![изображение](https://github.com/MikhailKaraganov/project-final/assets/77681385/d11be9c7-352d-4bb4-84b6-150f6fac8bea)


8.Добавить локализацию минимум на двух языках для шаблонов писем и стартовой страницы index.html.

![изображение](https://github.com/MikhailKaraganov/project-final/assets/77681385/c553ad84-853f-458e-8538-c22f424cdd81)
![изображение](https://github.com/MikhailKaraganov/project-final/assets/77681385/873e9898-77a8-4661-bc9c-f991c6aba4df)

9. Написать Dockerfile для основного сервера 

Для запуска так же необходимо создать сеть докера и запустить в ней контейнер postgres 

(docker network create jiranetwork

docker run --network=jiranetwork -p 5432:5432 --name postgres-db1 -e POSTGRES_USER=jira -e POSTGRES_PASSWORD=JiraRush -e POSTGRES_DB=jira -e PGDATA=/var/lib/postgresql/data/pgdata -v ./pgdata:/var/lib/postgresql/data -d postgres)

Так же нужно измменить URL базы данных в application.yaml (jdbc:postgresql://localhost:5432/jira -> jdbc:postgresql://postgres-db1:5432/jira)

Далее в Idea внести следущие настройки в конфигурацию запуска dockerfile:
![изображение](https://github.com/MikhailKaraganov/project-final/assets/77681385/5b3df693-c167-4a46-acf2-3b0cf67e071d)

10. Добавить автоматический подсчет времени сколько задача находилась в работе и тестировании. Написать 2 метода на уровни сервиса, который параметром принимает задачу и возвращают затраченное время


