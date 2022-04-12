# Тестовое задание для Android-стажеров в команду мобильного приложения Сравни.ру

<table>
  <tr>
    <td><img src="https://user-images.githubusercontent.com/55259346/163024217-4959e6f2-9a34-483a-983a-694af8b677e4.jpg" alt="Main Screen" width="250"/></td>
    <td><img src="https://user-images.githubusercontent.com/55259346/163024224-45938c5a-05d4-4615-b15a-b868f5bdd568.jpg" alt="Main Screen with data" width="250"/></td>
  </tr>
  <tr>
    <td><img src="https://user-images.githubusercontent.com/55259346/163024232-d0bbd9d1-e548-4a05-a18e-4c81d9b20641.jpg" alt="" width="250"/></td>
    <td><img src="https://user-images.githubusercontent.com/55259346/163024243-6519bb71-a714-4ad3-85cf-674e43ca3af0.jpg" alt="Your image title" width="250"/></td>
  </tr>
</table>

Актуальные ошибки:
* Анимация EditText+TextView (EditorBottomSheet) - нашёл 2 решения: PropertyAnimation и ViewPager2.
* Поддержка альбомной ориентации в EditorBottomSheet - проблема в фиксированном отступе кнопки "Далее". Решение вижу такое: программно поменять margin исходя из конкретных размеров экрана.
* Мерцание EditText (CalculatorFragment) - по видимому вызвано использованием NotifyDataSetChanged, от которого не удалось избавиться. DiffUtils не любит ArrayList'ы по видимому :(
* Касаемо проблемы отправки запроса на сервер - нужно было придумать признак отправки: в голову пришло только проверять заполнены ли все данные + сравнивать новый список со старым.
* Частично разобрался с ViewModel: теперь функции ничего не возвращают а лишь обновляют liveData.
