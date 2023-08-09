# Projekt JavaFX – Zarządzanie Lotami

## Opis aplikacji

Aplikacja Zarządzania lotami umożliwia wiele funkcjonalności, aby zapewnić możliwość wygodnego zarządzania lotami na małym lotnisku. Aplikacja ma wyszczególnione cztery główne moduły, które zapewniają możliwość zarządzania kontami pracowników, oraz pozycjami i przypisanymi do nich uprawnień. Możliwość zarządzania samolotami, lotami, jak i sprzedażą biletów.

### Opis poszczególnych sekcji aplikacji:
* I moduł – Account Manager:  
Zarządzanie kontami – możliwość dodawania, usuwania, edycji, filtrowania i sortowania kont w aplikacji, konto jest przypisywane do osoby/pracownika korzystającego z aplikacji. Możliwość dodawania, usuwania, edycji, filtrowania i sortowania pozycji, do których są przypisywane konta przy ich tworzeniu. Różne pozycje mają różne uprawnienia dostępu do poszczególnych modułów. Możliwość ustawiania grafiku pracy dla wskazanego pracownika/konta zarejestrowanego w aplikacji.

* II moduł – Technical Efficiency:  
Stan techniczny samolotów - możliwość dodawania, usuwania, edycji, filtrowania i sortowania samolotów. Możliwość oznaczania samolotów z poziomu zakładki „Aircraft checkup”, jako: do przeglądu, gotowy do lotu, samolot nie gotowy, zatankowany lub niezatankowany. Do zakładki „Aircraft checkup” trafiają samoloty, gdy ich lot się kończy i są oznaczone z poziomu modułu trzeciego, jako wylądowane. 

* III moduł – Air Traffic:  
Ruch powietrzny – możliwość dodawania, usuwania, edycji, filtrowania i sortowania lotów. Możliwość przypisania samolotu do lotu, który ma mniej niż 30 min do ustalonego czasu odlotu z poziomu zakładki „Take-off service”, w tym samym momencie możliwość kupna biletu na taki lot z poziomu modułu czwartego jest blokowana, co pozwala dopasować rozmiar samolotu do potrzeb biorąc pod uwagę ilość sprzedanych biletów. Lot z przypisanym samolotem można oznaczyć jako „w locie”, gdy zostanie tak oznaczony trafi na listę lotów do zakładki „Landing service”. Możliwość z poziomu zakładki „Landing service” oznaczenia lotu za zakończony, w takim przypadku lot zostaje usunięty, a samolot przypisany do tego lotu trafia na listę zakładki „Aircaft checkup” w module drugim.

* IV moduł – Customer Service:  
Obsługa klienta – możliwość dodawania, usuwania, edycji, filtrowania i sortowania biletów. Bilet można przypisać do lotu, który ma nie mniej niż 30 minut do odlotu. Możliwość wygenerowania pliku PDF zawierającego dane biletu. Po wygenerowaniu plik automatycznie zapisuje się w folderze „zl_pdfs” znajdującym się w folderze „Dokumenty”. Po zapisie plik zostaje uruchomiony w domyślnie ustawionym w systemie programie do otwierania plików typu PDF.

* Sekcja Home  
Okno startowe/domowe – możliwość podglądu przypisanego grafiku pracy dla obecnie zalogowanego konta/pracownika w aplikacji z możliwością wyboru daty, która nas interesuje.
Sekcja Time Simulation
Symulacja czasu – możliwość ustawienia daty i czasu w aplikacji, który będzie traktowany jako rzeczywisty w obrębie całej aplikacji.

* Sekcja wczytania bazy danych wypełnionej przykładowymi danymi  
Obecnie wykorzystywana baza danych zostaje nadpisana nową.

* Sekcja opróżnienia bazy danych.  
Opróżnia wykorzystywaną bazę danych pozostawiając pozycje i konto administratora.

* Sekcja Logout  
Obecnie zalogowany użytkownik zostaje wylogowany, a okno z możliwością przelogowania/ponownego zalogowania zostaje ukazane na ekranie. 

* Sekcja Quit  
Wylogowywuje użytkownika z aplikacji, po czym ją zamyka.


## Narzędzia i technologie
* Narzędzie programistyczne - IntelliJ IDEA (JavaFX)
* Język programowania - Java
* Testy jednostkowe - JUnit 4
* Narzędzie generowania dokumentacji - Javadoc
* Rozwiązanie bazodanowe –  XAMPP, MySQL MariaDB, interfejs JDBC
* Narzędzia do kreacji diagramów – Draw.io, phpMyAdmin, IntelliJ IDEA
* Narzędzie do kreacji instalatora – instal4j

## Diagram ERD
|![erd_diagram](/diagrams/erd.png) | 
|:--:| 
| *Rysunek 1. Diagram ERD.* |

## Diagram przypadków użycia
|![use_case_diagram](/diagrams/use_case.png) | 
|:--:| 
| *Rysunek 2. Diagram przypadków użycia.* |

## Diagram klas
|![class_diagram](/diagrams/class.png) | 
|:--:| 
| *Rysunek 3. Diagram klas.* |

## GUI
### Ekran logowania
Po uruchomieniu aplikacji wyświetli się ukazane poniżej okienko, za pomocą, którego będzie możliwość zalogowania się. Aby zalogować się na konto administratora należy wprowadzić następujące dane:
* Username: administrator
* Password: admin123

|![login_screen](/screens/1.png) | 
|:--:| 
| *Rysunek 4. Zrzut ekranu prezentujący ekran logowania w aplikacji.* |

### Sekcja startowa „Home”:
Uruchamiana jest domyślnie po poprawnym zalogowaniu się do aplikacji, z tego miejsca mamy dwie zakładki „Welcome”, przedstawiona na zdjęciu poniżej oraz „Work schedule”.

|![home_screen_1](/screens/2.png) | 
|:--:| 
| *Rysunek 5. Zrzut ekranu prezentujący sekcję "Home -> Welcome" .* |

Po lewej stronie możemy zaobserwować **panel nawigacyjny aplikacji**, w którym:
* Przycisk „Home” pozwala przejść do sekcji okna startowego „Home”.
* Przycisk „Account Manager” pozwala przejść do sekcji modułu pierwszego, z funkcjonalnościami związanymi z administracją kont w aplikacji.
* Przycisk „Technical Efficiency” pozwala przejść do sekcji modułu drugiego, z funkcjonalnościami związanymi z zarządzaniem samolotami.
* Przycisk „Air Traffic” pozwala przejść do sekcji modułu trzeciego, z funkcjonalnościami związanymi z zarządzaniem lotami, obsługą startów i lądowania.
* Przycisk „Customer Service” pozwala przejść do sekcji modułu czwartego, z funkcjonalnościami związanymi z przypisywaniem biletów na konkretne loty.
* Przycisk „Load populated DB” pozwala wczytać przykładowe dane do obecnie wykorzystanej bazy. (Uwaga! Po kliknięciu obecnie zapisane dane w bazie zostaną nadpisane i nie będzie możliwości ich przywrócenia.)
* Przycisk „Empty DB” pozwala opróżnić bazę danych z wszystkich danych pozostawiając tylko pozycję i konto administratora.
* Przycisk „Time simulation” pozwala przejść do sekcji „Symulacji czasu”.
* Przycisk „Logout” pozwala użytkownikowi wylogować się z aplikacji. Po kliknięciu zostajemy automatycznie przeniesieni do okna logowania.
* Przycisk „Quit” pozwala użytkownikowi zamknąć aplikację.

Po przejściu na zakładkę „Work schedule” ukaże nam się okienko z poziomu, którego obecnie zalogowany użytkownik ma możliwość podglądu przypisanego grafiku godzin pracy do swojego konta: 

|![home_screen_2](/screens/3.png) | 
|:--:| 
| *Rysunek 6. Zrzut ekranu prezentujący sekcję "Home -> Work schedule".* |

### Sekcja symulacji czasu „Time simulation”:
W tej sekcji mamy możliwość zmiany czasu dla aplikacji. Czas ustalony z poziomu tej sekcji jest traktowany, jako rzeczywisty czas w obrębie całej aplikacji. Za pomocą przycisku „Assign” zmieniamy datę i czas na podany w polach daty i czasu wybieranych za pomocą DataPicker'a i TimePicker'a

|![time_simulation_screen](/screens/4.png) | 
|:--:| 
| *Rysunek 7. Zrzut ekranu prezentujący sekcję "Time simulation".* |

### Sekcja modułu "Account Manager":
Na poniższym zdjęciu mamy podgląd pierwszej dostępnej zakładki „Account list”. Z poziomu tej zakładki mamy możliwość:
* Dodawania nowych kont użytkowników/pracowników.
* Edycji oraz usuwania już istniejących kont w aplikacji (konta administratora nie można usunąć!).
* Możliwość przeszukania zasobów wyświetlanych danych w tabeli za pomocą podanej frazy.
* Możliwość sortowania zasobów wyświetlanych danych w tabeli klikając w interesującą nas kolumnę.

|![account_manager_screen_1](/screens/5.png) | 
|:--:| 
| *Rysunek 8. Zrzut ekranu prezentujący sekcję "Account Manager -> Account list".* |

Aby edytować dane istniejącego konta należy dwukrotnie kliknąć w interesujące nas pole w tabeli, które chcemy zmienić. Takie pole staje się edytowalne i jest możliwość zmiany zawartości w polu. Aby potwierdzić i przesłać zmiany do bazy należy się upewnić, że mamy zaznaczony interesujący nas rekord w tabeli a następnie kliknąć przycisk „Update”. Stosowna informacja zwrotna potwierdzająca wykonaną czynność lub błąd przy jej wykonywaniu zostanie wyświetlona zaraz nad widocznymi przyciskami.

Aby usunąć widoczny w tabeli rekord/konto należy się upewnić, iż interesujący nas rekord jest zaznaczony, a następnie kliknąć przycisk „Delete”. Stosowna informacja zwrotna potwierdzająca wykonaną czynność lub błąd przy jej wykonywaniu zostanie wyświetlona zaraz nad widocznymi przyciskami.

Aby odświeżyć i ponownie wczytać dane z bazy do widocznej w aplikacji tabeli, należy kliknąć przycisk „Refresh”, ten przycisk czyści również wszelkie informacje zwrotne odnośnie wcześniej wykonanej czynności.
Aby dodać nowe konto użytkownika należy kliknąć przycisk „Add”, wyświetli nam się wtedy nowe okienko przedstawione poniżej, gdzie po wypełnieniu wszystkich pól poprawnymi danymi oraz kliknięciu przycisku „Add”, nowe konto zostanie utworzone i zapisane w bazie danych. Aby zamknąć okno dodawania nowych użytkowników należy kliknąć przycisk „Close”.

|![account_manager_screen_2](/screens/6.png) | 
|:--:| 
| *Rysunek 9. Zrzut ekranu prezentujący sekcję "Account Manager -> Account list -> Add new user".* |

Z poziomu drugiej dostępnej zakładki w pierwszym module „Positions list & privileges” przedstawionej na zdjęciu poniżej, mamy możliwość zarządzania pozycjami w aplikacji (pozycji „Administrator” nie można usunąć ani edytować!). Dostępne funkcjonalności to:
* Dodawania nowych pozycji.
* Edycji oraz usuwania już istniejących pozycji w aplikacji.
* Możliwość przeszukania zasobów wyświetlanych danych w tabeli za pomocą podanej frazy.
* Możliwość sortowania zasobów wyświetlanych danych w tabeli klikając w interesującą nas kolumnę.

|![account_manager_screen_3](/screens/7.png) | 
|:--:| 
| *Rysunek 10. Zrzut ekranu prezentujący sekcję "Account Manager -> Positions list & privileges".* |

Aby edytować nazwę lub istniejącej pozycji należy dwukrotnie kliknąć w pole nazwy w tabeli, które chcemy zmienić. Takie pole staje się edytowalne i jest możliwość zmiany zawartości w polu. Aby potwierdzić i przesłać zmiany do bazy należy się upewnić, że mamy zaznaczony interesujący nas rekord w tabeli a następnie kliknąć przycisk „Update”. Aby zmienić uprawnieni pozycji, należy kliknąć w interesującą nas pozycję w tabeli po lewej, uprawnienia przypisane do pozycji zostaną wyświetlone w tabeli po prawej, następnie postępując analogicznie do poprzednich sytuacji edytowania pola (wartości przy uprawnieniach powinny przyjmować 1 lub 0). Stosowna informacja zwrotna potwierdzająca wykonaną czynność lub błąd przy jej wykonywaniu zostanie wyświetlona zaraz nad widocznymi przyciskami.

Aby usunąć widoczny w tabeli rekord/pozycję należy się upewnić, iż interesujący nas rekord jest zaznaczony, a następnie kliknąć przycisk „Delete”. Stosowna informacja zwrotna potwierdzająca wykonaną czynność lub błąd przy jej wykonywaniu zostanie wyświetlona zaraz nad widocznymi przyciskami. Po usunięciu pozycji wszystkie uprawnienia przypisane do niej również zostają usunięte.

Aby odświeżyć i ponownie wczytać dane z bazy do widocznej w aplikacji tabeli, należy kliknąć przycisk „Refresh”, ten przycisk czyści również wszelkie informacje zwrotne odnośnie wcześniej wykonanej czynności.
Aby dodać nową pozycję należy kliknąć przycisk „Add”, wyświetli nam się wtedy nowe okienko przedstawione poniżej, gdzie po wypełnieniu pola z nazwą, zaznaczeniu uprawnień do poszczególnych modułów i kliknięciu przycisku „Add”, nowa pozycja zostanie utworzona i zapisana w bazie danych. Aby zamknąć okno dodawania nowych pozycji należy kliknąć przycisk „Close”. 

|![account_manager_screen_4](/screens/8.png) | 
|:--:| 
| *Rysunek 11. Zrzut ekranu prezentujący sekcję "Account Manager -> Positions list & privileges -> Add new position".* |

Z poziomu trzeciej dostępnej zakładki w pierwszym module „Work schedule” przedstawionej na zdjęciu poniżej, mamy możliwość zarządzania grafikiem godzin pracy dla pracowników/kont znajdujących się w aplikacji. Dostępne funkcjonalności to:
* Wczytywanie obecnie ustalonych grafików z bazy dla wskazanego konta/pracownika.
* Ustawianie oraz edycja grafiku dla wskazanego konta/pracownika.
* Wczytanie templatki z interesującymi nas godzinami z bazy automatycznie zaznaczając pola. Klikając w przycisk „Load”, uprzednio wybierając z listy interesującą nas templatkę
* Tworzenie nowych templatek godzin i zapisu ich do bazy. Uzupełniając pole z nazwą templatk w prawym panelui, a następnie klikając w przycisk „Save”.
* Usówanie istniejących templatek godzin. Klikająć w przycisk „Delete”
* Zmiana widocznego tygodnia za pomocą DataPicker`a lub przycisków „Next week” i „Previous week”.
* Możliwość wyczyszczenia widocznego grafiku za pomocą przycisku „Reset visable schedule”.

|![account_manager_screen_5](/screens/9.png) | 
|:--:| 
| *Rysunek 12. Zrzut ekranu prezentujący sekcję "Account Manager -> Work schedule".* |

Aby przypisać grafik do konta należy zacząć od wyboru z prawego panelu w części „Select worker” interesującego nas konta w aplikacji z wszystkich dostępnych. Następnie aby zaznaczyć jakąś godzinę należy kliknąć w interesujący nas kafelek, powinien się on wtedy podświetlić jasno-zielonym kolorem. Oznacza to że zoztał oznaczony ale jeszcze nie przesłany do bazy. Kolorem morskim podświelane są kafelki już potwierdzone czyli takie o których informacja jest już przesłana do bazy.

Po zaznaczeniu wszystkich interesujących nas kafelków możemy przesłać grafik do bazy klikając przycisk „Assign schedule”.

### Sekcja modułu "Technical Efficiency":
Na poniższym zdjęciu mamy podgląd pierwszej dostępnej zakładki „List of aircrafts”. Z poziomu tej zakładki mamy dostępne funkcjonalności:
* Dodawania nowych samolotów.
* Edycji oraz usuwania już istniejących samolotów w aplikacji.
* Możliwość przeszukania zasobów wyświetlanych danych w tabeli za pomocą podanej frazy.
* Możliwość sortowania zasobów wyświetlanych danych w tabeli klikając w interesującą nas kolumnę.

|![technical_efficiency_screen_1](/screens/10.png) | 
|:--:| 
| *Rysunek 13. Zrzut ekranu prezentujący sekcję "Technical Efficiency -> List of aircrafts".* |

Aby edytować dane istniejącego samolotu należy dwukrotnie kliknąć w interesujące nas pole w tabeli, które chcemy zmienić. Takie pole staje się edytowalne i jest możliwość zmiany zawartości w polu. Aby potwierdzić i przesłać zmiany do bazy należy się upewnić, że mamy zaznaczony interesujący nas rekord w tabeli a następnie kliknąć przycisk „Update”. Stosowna informacja zwrotna potwierdzająca wykonaną czynność lub błąd przy jej wykonywaniu zostanie wyświetlona zaraz nad widocznymi przyciskami.

Aby usunąć widoczny w tabeli rekord/samolot należy się upewnić, iż interesujący nas rekord jest zaznaczony, a następnie kliknąć przycisk „Delete”. Stosowna informacja zwrotna potwierdzająca wykonaną czynność lub błąd przy jej wykonywaniu zostanie wyświetlona zaraz nad widocznymi przyciskami.

Aby odświeżyć i ponownie wczytać dane z bazy do widocznej w aplikacji tabeli, należy kliknąć przycisk „Refresh”, ten przycisk czyści również wszelkie informacje zwrotne odnośnie wcześniej wykonanej czynności.

Aby dodać nowy samolot w aplikacji należy kliknąć przycisk „Add”, wyświetli nam się wtedy nowe okienko przedstawione poniżej, gdzie po wypełnieniu wszystkich pól poprawnymi danymi oraz kliknięciu przycisku „Add”, nowy rekord zostanie utworzone i zapisany w bazie danych. Aby zamknąć okno dodawania samolotów należy kliknąć przycisk „Close”.

|![technical_efficiency_screen_2](/screens/11.png) | 
|:--:| 
| *Rysunek 14. Zrzut ekranu prezentujący sekcję "Technical Efficiency -> List of aircrafts -> Add new aircraft".* |

Z poziomu drugiej dostępnej zakładki w drugim module „Aircraft checkup” przedstawionej na zdjęciu poniżej, mamy możliwość oznaczania stanu technicznego samolotów, a także informacji czy samolot został zatankowany. Do widocznej tebeli w tej zakładce trafiają samoloty po ukończeniu lotu i znikają z niej jak tylko zostaną oznaczone jako sprawne i zatankowane. Dostępne funkcjonalności to:
* Oznaczanie samolotów, jako „operational” lub „not operational”
* Zmiana statusu samolotu odnośnie, czy został zatankowany.
* Możliwość przeszukania zasobów wyświetlanych danych w tabeli za pomocą podanej frazy.
* Możliwość sortowania zasobów wyświetlanych danych w tabeli klikając w interesującą nas kolumnę.

|![technical_efficiency_screen_3](/screens/12.png) | 
|:--:| 
| *Rysunek 15. Zrzut ekranu prezentujący sekcję "Technical Efficiency -> Aircraft checkup".* |

Aby edytować stan techniczny i status zatankowania konkretnego samolotu należy dwukrotnie kliknąć w interesujące nas pole w tabeli, które chcemy zmienić. Zostanie wyświetlona lista z możliwymi opcjami i wybrać tą opcję, która nas interesuje, do zaznaczenia dla stanu technicznego będą to „for_check_up”, „operational”, „not_operational”, a dla czy zatankowany „true” i „false”.  Aby potwierdzić i przesłać zmiany do bazy należy się upewnić, że mamy zaznaczony interesujący nas rekord w tabeli a następnie kliknąć przycisk „Update”. Stosowna informacja zwrotna potwierdzająca wykonaną czynność lub błąd przy jej wykonywaniu zostanie wyświetlona zaraz nad widocznymi przyciskami.

Aby odświeżyć i ponownie wczytać dane z bazy do widocznej w aplikacji tabeli, należy kliknąć przycisk „Refresh”, ten przycisk czyści również wszelkie informacje zwrotne odnośnie wcześniej wykonanej czynności.

### Sekcja modułu "Air Traffic":
Na poniższym zdjęciu mamy podgląd pierwszej dostępnej zakładki „Flight schedule”. Z poziomu tej zakładki mamy dostępne funkcjonalności:
* Dodawania nowych lotów.
* Edycji oraz usuwania już istniejących lotów w aplikacji.
* Możliwość przeszukania zasobów wyświetlanych danych w tabeli za pomocą podanej frazy.
* Możliwość sortowania zasobów wyświetlanych danych w tabeli klikając w interesującą nas kolumnę.

|![air_traffic_screen_1](/screens/13.png) | 
|:--:| 
| *Rysunek 16. Zrzut ekranu prezentujący sekcję "Air traffic -> Flight schedule".* |

Aby edytować dane istniejącego lotu należy dwukrotnie kliknąć w interesujące nas pole w tabeli, które chcemy zmienić. Takie pole staje się edytowalne i jest możliwość zmiany zawartości w polu. Aby potwierdzić i przesłać zmiany do bazy należy się upewnić, że mamy zaznaczony interesujący nas rekord w tabeli a następnie kliknąć przycisk „Update”. Stosowna informacja zwrotna potwierdzająca wykonaną czynność lub błąd przy jej wykonywaniu zostanie wyświetlona zaraz nad widocznymi przyciskami.

Aby usunąć widoczny w tabeli rekord/lot należy się upewnić, iż interesujący nas rekord jest zaznaczony, a następnie kliknąć przycisk „Delete”. Stosowna informacja zwrotna potwierdzająca wykonaną czynność lub błąd przy jej wykonywaniu zostanie wyświetlona zaraz nad widocznymi przyciskami.

Aby odświeżyć i ponownie wczytać dane z bazy do widocznej w aplikacji tabeli, należy kliknąć przycisk „Refresh”, ten przycisk czyści również wszelkie informacje zwrotne odnośnie wcześniej wykonanej czynności.

Aby dodać nowy lot w aplikacji należy kliknąć przycisk „Add”, wyświetli nam się wtedy nowe okienko przedstawione poniżej, gdzie po wypełnieniu wszystkich pól poprawnymi danymi oraz kliknięciu przycisku „Add”, nowy rekord zostanie utworzony i zapisany w bazie danych. Aby zamknąć okno dodawania samolotów należy kliknąć przycisk „Close”.

|![air_traffic_screen_2](/screens/14.png) | 
|:--:| 
| *Rysunek 17. Zrzut ekranu prezentujący sekcję "Air traffic -> Flight schedule -> Add new flight".* |

Z poziomu drugiej dostępnej zakładki w trzecim module „Take-off service” przedstawionej na zdjęciu poniżej, mamy możliwość przypisania samolotu dostosowując samolot do liczby sprzedanych biletów na lot. Po przypisaniu samolotu do lotu pojawia nam się możliwość wystartowania samolotu (oznaczenia jako „w locie”). Do widocznej tebeli w tej zakładce trafiają loty którym pozostało mniej niż 30 minut do odlotu i znikają z niej jak tylko zostaną oznaczone jako „w locie”.Jeżeli lot nie zostanie wystartowany a czas odlotu minie, zostanie automatycznie opóźniony o 15 minut, w przypadku gdy ni byłoby zakupionych biletów na dany lot, został by on całkowicie usunięty. Dostępne funkcjonalności:
* Przypisywanie samolotów z listy wszystkich sprawnych i zatankowanych samolotów.
* Oznaczanie lotów, jako „available” lub „in_flight”.
* Możliwość przeszukania zasobów wyświetlanych danych w tabeli za pomocą podanej frazy.
* Możliwość sortowania zasobów wyświetlanych danych w tabeli klikając w interesującą nas kolumnę.

|![air_traffic_screen_3](/screens/15.png) | 
|:--:| 
| *Rysunek 18. Zrzut ekranu prezentujący sekcję "Air traffic -> Take-off service".* |

Aby edytować lub przypisać samolot do lotu należy dwukrotnie kliknąć w interesujące nas pole w tabeli. Zostanie wyświetlona lista z możliwymi opcjami i wybieramy tą opcję, która nas interesuje. Aby zmienić status lotu należy postępować analogicznie.  Aby potwierdzić i przesłać zmiany do bazy należy się upewnić, że mamy zaznaczony interesujący nas rekord w tabeli a następnie kliknąć przycisk „Update”. Stosowna informacja zwrotna potwierdzająca wykonaną czynność lub błąd przy jej wykonywaniu zostanie wyświetlona zaraz nad widocznymi przyciskami.

Aby odświeżyć i ponownie wczytać dane z bazy do widocznej w aplikacji tabeli, należy kliknąć przycisk „Refresh”, ten przycisk czyści również wszelkie informacje zwrotne odnośnie wcześniej wykonanej czynności.

Z poziomu trzeciej dostępnej zakładki w trzecim module „Landing service” przedstawionej na zdjęciu poniżej, mamy możliwość oznaczenia samolotu przypisanego do trwającego lotu jako wylondowany. Do widocznej tebeli w tej zakładce trafiają loty po wystartowaniu ich z poziomu zakładki „Take-off service”. Dostępne funkcjonalności:
* Oznaczanie samolotów, jako wylądowane (lot zostaje usunięty a samolot trafia do zakładki w drugim module „Aircraft checkup”).
* Możliwość przeszukania zasobów wyświetlanych danych w tabeli za pomocą podanej frazy.
* Możliwość sortowania zasobów wyświetlanych danych w tabeli klikając w interesującą nas kolumnę. 

|![air_traffic_screen_4](/screens/16.png) | 
|:--:| 
| *Rysunek 19. Zrzut ekranu prezentujący sekcję "Air traffic -> Landing service".* |

Aby oznaczyć samolot trwającego lotu, jako wylądowany należy dwukrotnie kliknąć w interesujące nas pole w tabeli. Zostanie wyświetlona lista z możliwymi opcjami i wybieramy tą opcję, która nas interesuje, w tym przypadku będzie to „available” i „in_flight. Aby potwierdzić i przesłać zmiany do bazy należy się upewnić, że mamy zaznaczony interesujący nas rekord w tabeli a następnie kliknąć przycisk „Update”. Stosowna informacja zwrotna potwierdzająca wykonaną czynność lub błąd przy jej wykonywaniu zostanie wyświetlona zaraz nad widocznymi przyciskami.

Aby odświeżyć i ponownie wczytać dane z bazy do widocznej w aplikacji tabeli, należy kliknąć przycisk „Refresh”, ten przycisk czyści również wszelkie informacje zwrotne odnośnie wcześniej wykonanej czynności.

### Sekcja modułu "Customer Service":
Na poniższym zdjęciu mamy podgląd pierwszej dostępnej zakładki „List of flights”. Z poziomu tej zakładki mamy dostępne funkcjonalności:
* Możliwość przypisania biletu do wybranego w tabeli lotu, jeżeli pozostało więcej niż 30 minut do odlotu.
* Możliwość przeszukania zasobów wyświetlanych danych w tabeli za pomocą podanej frazy.
* Możliwość sortowania zasobów wyświetlanych danych w tabeli klikając w interesującą nas kolumnę

|![customer_service_screen_1](/screens/17.png) | 
|:--:| 
| *Rysunek 20. Zrzut ekranu prezentujący sekcję "Customer service -> List of flights".* |

Aby odświeżyć i ponownie wczytać dane z bazy do widocznej w aplikacji tabeli, należy kliknąć przycisk „Refresh”.

Aby przypisać bilet do lotu należy zaznaczyć interesujący nas lot w tabeli a następnie kliknąć przycisk „Assign ticket”. Otworzy nam się nowe okienko przedstawione poniżej, z automatycznie wypełnionymi danymi odnośnie wybranego przez nas lotu. Po wypełnieniu wszystkich pól poprawnymi danymi klikamy przycisk „Add”. Stosowna informacja zwrotna potwierdzająca wykonaną czynność lub błąd przy jej wykonywaniu zostanie wyświetlona zaraz nad widocznymi przyciskami. Aby zamknąć okienko dodawania biletu należy kliknąć przycisk „Close”.

|![customer_service_screen_2](/screens/18.png) | 
|:--:| 
| *Rysunek 21. Zrzut ekranu prezentujący sekcję "Customer service -> List of flights -> Assign ticket to person".* |

Z poziomu drugiej dostępnej zakładki w czwartym module „Assigned tickets” przedstawionej na zdjęciu poniżej, mamy możliwość wyświetlenia wszystkich biletów na jeszcze nieskończone loty. Dostępne funkcjonalności:
* Edycja lub usunięcie wybranego biletu.
* Możliwość generacji wskazanego biletu do pliku PDF.
* Możliwość przeszukania zasobów wyświetlanych danych w tabeli za pomocą podanej frazy.
* Możliwość sortowania zasobów wyświetlanych danych w tabeli klikając w interesującą nas kolumnę. 

|![customer_service_screen_3](/screens/19.png) | 
|:--:| 
| *Rysunek 22. Zrzut ekranu prezentujący sekcję "Customer service -> Assigned tickets".* |

Aby edytować dane istniejącego biletu należy dwukrotnie kliknąć w interesujące nas pole w tabeli, które chcemy zmienić. Takie pole staje się edytowalne i jest możliwość zmiany zawartości w polu. Aby potwierdzić i przesłać zmiany do bazy należy się upewnić, że mamy zaznaczony interesujący nas rekord w tabeli a następnie kliknąć przycisk „Update”. Stosowna informacja zwrotna potwierdzająca wykonaną czynność lub błąd przy jej wykonywaniu zostanie wyświetlona zaraz nad widocznymi przyciskami.

Aby usunąć widoczny w tabeli rekord/bilet należy się upewnić, iż interesujący nas rekord jest zaznaczony, a następnie kliknąć przycisk „Delete”. Stosowna informacja zwrotna potwierdzająca wykonaną czynność lub błąd przy jej wykonywaniu zostanie wyświetlona zaraz nad widocznymi przyciskami.

Aby odświeżyć i ponownie wczytać dane z bazy do widocznej w aplikacji tabeli, należy kliknąć przycisk „Refresh”, ten przycisk czyści również wszelkie informacje zwrotne odnośnie wcześniej wykonanej czynności.

Aby wygenerować bilet do pliku PDF należy zaznaczyć w tabeli interesujący nas bilet, a następnie klikamy przycisk „Generate PDF”. Plik PDF zostanie automatycznie zapisany w folderze „…Moje dokumenty/zl_pdfs” oraz automatycznie otworzony w domyślnie ustawionym programie w systemie do obsługi plików PDF.

Przykładowy bilet, wygenerowany do pliku PDF:
|![customer_service_screen_4](/screens/20.png) | 
|:--:| 
| *Rysunek 23. Zrzut ekranu prezentujący przykładowy bilet wygenerowany z poziomu sekcji "Customer service -> Assigned tickets -> Generate PDF".* |
