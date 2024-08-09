Проект ресепшена массажного салона

reception-main - основной rest-сервис позволяющий регистрировать записи на прием в салон
    Ведение записей на прием с учетом занятости комнат и мастеров
    Справочники клиентов, комнат, мастеров
    Отправка оповещений в брокер сообщений для последующей обработки сервисом оповещений

reception-notify - сервис оповещений клиентов
    Читает брокер сообщений и в зависимости от настроек клиента, делает рассылку(почта, телефон)

reception-online-record - rest-сервис и телеграм-бот для подачи и обработки онлайн заявок на запись в салон
    Телеграм-бот позволяет подать заявку с указанием контактных данных и описанием пожеланий
    Rest-сервис позволяет работать администраторам с данными полученными из бота

reception-data - общие данные используемые в разные сервисах
    разного рода DTO и т.д.

reception-management - rest-клиент для работы с сервисами
    добавлена функция работы со справочниками
    обработка онлайн заявок - в работе
    работа с записями на прием - в работе