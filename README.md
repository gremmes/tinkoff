Тестирование сайта: https://www.tinkoff.ru/ с использованием Selenium (3.7.1) и библиотеки jUnit.

Для запуска тестов необходимо установить:
1) Apache Maven (https://maven.apache.org/download.cgi , при использовании IdeaProjects устанавливается автоматически с разрешения пользователя)
2) ChromeDriver (https://sites.google.com/a/chromium.org/chromedriver/downloads , расположение  по пути C:\Program Files (x86)\Google\Chrome\Application\chromedriver.exe

Запуск можно осуществить из командной строки посредством команды “mvn test” или с помощью среды разработки.

Пакет: ru.tinkoff.jobapplication
Класс: SiteTest
Методы:
 - payment — основной метод теста, в теле которого происходит описание и запуск пунктов тест-кейса, из этого метода происходит вызов вспомогательных методов;

	Переменные метода payment:
           - wanted - “искомый» (в нее осуществляется запись значения «ЖКУ-Москва»; 
           - urlMsk – переменная, куда осуществляется запись адреса страницу оплаты «ЖКУ-Москва».

 - openBrowser — создает и настраивает экземпляр webdriver, здесь прописывается путь к chromedriver.exe и в случае необдимости изменяется;
 - openSite — выполняется до теста, осуществляет открытие страницы по заданному адресу (https://www.tinkoff.ru/);
 - closeBrowser — осуществляет закрытие браузера по завершению выполнения теста;
 - sendKeysToField — осуществляет введение значения переменной типа String в поле страницы по указанному XPath;
 - sendKeysToFieldMass — заполнение текстового поля посимвольно, принимает на вход переменную типа String, которую нужно разбить на символы, и XPath, по которому нужно передавать символы;
 - openUtilities — выполняет клик по п. «Коммунальные платежи»  в списке категорий платежей на странице «Платежи»;
 - openRegion — метод осуществляет замену региона, принимает на вход два значения типа String, содержащие информацию об отображаемом требуемом регионе и выбираемом регионе в том случае, если текущий регион не совпадает с требуемым. 

Исходное задание было поделено условно на три части, что отображено в комментариях (первый, второй и третий тесты), но т.к. переменная wanted задействована во всех трех частях, то не представляется целесообразным разделение всех пунктов задания на отдельные тест-кейсы ввиду сложности установления очередности выполнения тестов. Поэтому был реализован единый тест-кейс. Далее ниже приводится описание исходного задания:

Первый тест:
1. Переходом по адресу https://www.tinkoff.ru/ загрузить стартовую страницу Tinkoff Bank.
2. Из верхнего меню, нажатием на пункт меню “Платежи“, перейти на страницу “Платежи“.
3. В списке категорий платежей, нажатием на пункт “Коммунальные платежи“, перейти на страницу выбора поставщиков услуг.
4. Убедиться, что текущий регион – “г. Москва” (в противном случае выбрать регион “г. Москва” из списка регионов).
5. Со страницы выбора поставщиков услуг, выбрать 1-ый из списка (Должен быть “ЖКУ-Москва”). Сохранить его наименование (далее “искомый”) и нажатием на соответствующий элемент перейти на страницу оплаты “ЖКУ-Москва“.
6. На странице оплаты, перейти на вкладку “Оплатить ЖКУ в Москве“.
7. Выполнить проверки на невалидные значения для обязательных полей: проверить все текстовые сообщения об ошибке (и их содержимое), которые появляются под соответствующим полем ввода в результате ввода некорректных данных.

Второй тест:
8. Повторить шаг (2).
9. В строке быстрого поиска поставщика услуг ввести наименование искомого (ранее сохраненного).
10. Убедиться, что в списке предложенных провайдеров искомый поставщик первый.
11. Нажатием на элемент, соответствующий искомому, перейти на страницу “Оплатить ЖКУ в Москве“. Убедиться, что загруженная страница та же, что и страница, загруженная в результате шага (5).

Третий тест:
12. Выполнить шаги (2) и (3).
13. В списке регионов выбрать “г. Санкт-Петербург”.
14. Убедится, что в списке поставщиков на странице выбора поставщиков услуг отсутствует искомый.



